package com.solvd.army.service.impl;

import com.solvd.army.domain.exception.NoDataException;
import com.solvd.army.domain.resources.Weapon;
import com.solvd.army.persistence.IWeaponRepository;
import com.solvd.army.persistence.impl.WeaponMapperImpl;
import com.solvd.army.service.IWeaponService;

import java.util.List;

public class WeaponServiceImpl implements IWeaponService {

    private final IWeaponRepository weaponRepository;

    public WeaponServiceImpl() {
        /*weaponRepository = new WeaponRepositoryImpl();*/
        weaponRepository = new WeaponMapperImpl();
    }

    @Override
    public List<Weapon> getByMilitaryUnitName(String militaryUnitName) {
        List<Weapon> weapons = weaponRepository.getByMilitaryUnitName(militaryUnitName);
        if (weapons == null) {
            throw new NoDataException("weaponRepository.getByMilitaryUnitName() was returned null-value in WeaponServiceImpl.");
        }
        return weapons;
    }

    @Override
    public Weapon update(Weapon weapon, Long weaponId, Long militaryUnitId) {
        weaponRepository.update(weapon, weaponId, militaryUnitId);
        return weapon;
    }

    @Override
    public List<Long> getId(Long militaryUnitId) {
        List<Long> ammoIds = weaponRepository.getId(militaryUnitId);
        if (ammoIds == null) {
            throw new NoDataException("weaponRepository.getId() was returned null-value in WeaponServiceImpl.");
        }
        return ammoIds;
    }

    @Override
    public Weapon create(Weapon weapon, Long militaryUnitId) {
        weaponRepository.create(weapon, militaryUnitId);
        return weapon;
    }

}
