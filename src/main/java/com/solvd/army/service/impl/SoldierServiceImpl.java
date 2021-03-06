package com.solvd.army.service.impl;

import com.solvd.army.domain.exception.NoDataException;
import com.solvd.army.domain.soldier.Soldier;
import com.solvd.army.persistence.ISoldierRepository;
import com.solvd.army.persistence.impl.SolderMapperImpl;
import com.solvd.army.service.ISoldierService;

import java.util.List;

public class SoldierServiceImpl implements ISoldierService {

    private final ISoldierRepository soldierRepository;

    public SoldierServiceImpl() {
        /*soldierRepository = new SoldierRepositoryImpl();*/
        soldierRepository = new SolderMapperImpl();
    }

    @Override
    public List<Soldier> getByMilitaryUnitName(String militaryUnitName) {
        List<Soldier> soldiers = soldierRepository.getByMilitaryUnitName(militaryUnitName);
        if (soldiers == null) {
            throw new NoDataException("soldierRepository.getByMilitaryUnitName() was returned null-value in SoldierServiceImpl.");
        }
        return soldiers;
    }

    @Override
    public void delete(Soldier soldier) {
        soldierRepository.delete(soldier);
    }

    @Override
    public Soldier createSoldier(Soldier soldier, Long militaryUnitId) {
        soldierRepository.createRecruit(soldier, militaryUnitId);
        soldierRepository.createSoldier(soldier, militaryUnitId);
        return soldier;
    }

    @Override
    public Soldier createRecruit(Soldier soldier, Long militaryUnitId) {
        soldierRepository.createRecruit(soldier, militaryUnitId);
        return soldier;
    }

    @Override
    public void updateSoldier(Soldier soldier, Long militaryUnitId) {
        soldierRepository.updateSoldier(soldier, militaryUnitId);
    }

    @Override
    public void updateRecruit(Soldier soldier) {
        soldierRepository.updateRecruit(soldier);
    }

    @Override
    public Soldier getById(Long id) {
        Soldier soldier = soldierRepository.getById(id);
        return soldier;
    }

    @Override
    public void deleteById(Long id) {
        soldierRepository.deleteById(id);
    }

    @Override
    public Integer getCount() {
        return soldierRepository.getCount();
    }

}
