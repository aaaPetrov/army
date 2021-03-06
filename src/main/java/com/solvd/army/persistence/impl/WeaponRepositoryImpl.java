package com.solvd.army.persistence.impl;

import com.solvd.army.domain.MilitaryUnit;
import com.solvd.army.domain.exception.ProcessingException;
import com.solvd.army.domain.resources.Weapon;
import com.solvd.army.persistence.ConnectionPool;
import com.solvd.army.persistence.IWeaponRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WeaponRepositoryImpl implements IWeaponRepository {

    private final static String SQL_COMMAND = SELECT_COMMAND();

    @Override
    public List<Weapon> getByMilitaryUnitName(String militaryUnitName) {
        Connection connection = ConnectionPool.CONNECTION_POOL.getConnection();
        List<Weapon> weapons = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_COMMAND)) {
            preparedStatement.setString(1, militaryUnitName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Weapon newWeapon = new Weapon();
                Weapon.WeaponType weaponType = Weapon.WeaponType.valueOf(resultSet.getString("type"));
                newWeapon.setWeaponType(weaponType);
                newWeapon.setAmount(resultSet.getInt("amount"));
                weapons.add(newWeapon);
            }
        } catch (SQLException exception) {
            throw new ProcessingException(exception.getMessage());
        } finally {
            ConnectionPool.CONNECTION_POOL.releaseConnection(connection);
        }
        return weapons;
    }

    @Override
    public List<Long> getId(Long militaryUnitId) {
        Connection connection = ConnectionPool.CONNECTION_POOL.getConnection();
        String sqlCommandSelect = "select weapon_id from military_unit_weapon where military_unit_id = ?;";
        List<Long> ids = null;
        try (PreparedStatement preparedStatementSelect = connection.prepareStatement(sqlCommandSelect)) {
            preparedStatementSelect.setLong(1, militaryUnitId);
            ResultSet resultSet = preparedStatementSelect.executeQuery();
            if (resultSet != null) {
                ids = new ArrayList<>();
            }
            while (resultSet.next()) {
                ids.add(resultSet.getLong("weapon_id"));
            }
        } catch (SQLException exception) {
            throw new ProcessingException(exception.getMessage());
        } finally {
            ConnectionPool.CONNECTION_POOL.releaseConnection(connection);
        }
        return ids;
    }

    @Override
    public void update(Weapon weapon, Long weaponId, Long militaryUnitId) {
        Connection connection = ConnectionPool.CONNECTION_POOL.getConnection();
        String sqlCommandUpdate = "update military_unit_weapon set weapon_id = ?, amount = ? where military_unit_id = ? and weapon_id = ?;";
        try (PreparedStatement preparedStatementUpdate = connection.prepareStatement(sqlCommandUpdate)) {
            preparedStatementUpdate.setLong(1, weapon.getWeaponType().getWeaponId());
            preparedStatementUpdate.setInt(2, weapon.getAmount());
            preparedStatementUpdate.setLong(3, militaryUnitId);
            preparedStatementUpdate.setLong(4, weaponId);
            preparedStatementUpdate.executeUpdate();
        } catch (SQLException exception) {
            throw new ProcessingException(exception.getMessage());
        } finally {
            ConnectionPool.CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void create(Weapon weapon, Long militaryUnitId) {
        Connection connection = ConnectionPool.CONNECTION_POOL.getConnection();
        String sqlCommand = "insert into military_unit_weapon(weapon_id, military_unit_id, amount) value(?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setLong(1, weapon.getWeaponType().getWeaponId());
            preparedStatement.setLong(2, militaryUnitId);
            preparedStatement.setLong(3, weapon.getAmount());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new ProcessingException(exception.getMessage());
        } finally {
            ConnectionPool.CONNECTION_POOL.releaseConnection(connection);
        }
    }

    public static List<Weapon> fillWeapons(ResultSet resultSet, List<MilitaryUnit> militaryUnits) {
        List<Weapon> weapons = null;
        try {
            Long militaryUnitWeaponId = resultSet.getLong("military_unit_weapon_id");
            Long militaryUnitWeaponMilitaryUnitId = resultSet.getLong("military_unit_weapon_military_unit_id");
            for (MilitaryUnit militaryUnit : militaryUnits) {
                if (militaryUnit.getId() == militaryUnitWeaponMilitaryUnitId) {
                    weapons = militaryUnit.getWeapons();
                    Weapon weapon = createIfNotExist(militaryUnitWeaponId, weapons);
                    Weapon.WeaponType weaponType = Weapon.WeaponType.valueOf(resultSet.getString("weapon_type"));
                    weapon.setWeaponType(weaponType);
                    weapon.setAmount(resultSet.getInt("amount"));
                }
            }
        } catch (SQLException exception) {
            throw new ProcessingException(exception.getMessage());
        }
        return weapons;
    }

    private static Weapon createIfNotExist(Long militaryUnitWeaponId, List<Weapon> weapons) {
        Weapon result = null;
        if (!weapons.isEmpty()) {
            for (Weapon weapon : weapons) {
                if (militaryUnitWeaponId.equals(weapon.getWeaponType().getWeaponId())) {
                    result = weapon;
                }
            }
            if (result == null) {
                Weapon newWeapon = new Weapon();
                weapons.add(newWeapon);
                result = newWeapon;
            }
        } else {
            Weapon newWeapon = new Weapon();
            weapons.add(newWeapon);
            result = newWeapon;
        }
        return result;
    }

    private static String SELECT_COMMAND() {
        return "select MUW.weapon_id, MUW.military_unit_id, MUW.amount, W.id, W.type from military_unit_weapon as MUW "
                + "inner join weapons as W on MUW.weapon_id = W.id "
                + "where military_unit_id = (select id from military_units where name = ?);";
    }

}
