package com.aoh4.frontline;

import aoc.kingdoms.lukasz.jakowski.Game;
import aoc.kingdoms.lukasz.map.army.ArmyDivision;
import aoc.kingdoms.lukasz.map.civilization.Civilization;
import aoc.kingdoms.lukasz.map.diplomacy.DiplomacyManager;
import aoc.kingdoms.lukasz.map.province.Province;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An adapter targeting methods confirmed in the supplied AoH3 Android DEX.
 *
 * IMPORTANT: Requires AoH3 classes on the compile classpath, and game integration.
 * Command issuance is intentionally delegated: the precise meaning of the
 * 5 parameters to Civilization.newMove(...) is not yet validated.
 */
public final class AoH3WorldAdapter implements FrontlineSystem.World {
    public interface MovementHook {
        boolean issueMove(int civilizationId, String armyKey, int destinationProvince, boolean offensive);
    }

    private final MovementHook movement;

    public AoH3WorldAdapter(MovementHook movement) {
        this.movement = movement;
    }

    @Override public int ownerOf(int provinceId) {
        if (provinceId < 0 || provinceId >= Game.getProvincesSize()) return -1;
        return Game.getProvince(provinceId).getCivID();
    }

    @Override public Collection<Integer> neighborsOf(int provinceId) {
        List<Integer> neighbors = new ArrayList<>();
        if (provinceId < 0 || provinceId >= Game.getProvincesSize()) return neighbors;
        Province province = Game.getProvince(provinceId);
        for (int i = 0; i < province.getNeighboringProvincesSize(); i++) {
            neighbors.add(province.getNeighboringProvinces(i));
        }
        return neighbors;
    }

    @Override public Collection<Integer> provincesOf(int civilizationId) {
        List<Integer> provinces = new ArrayList<>();
        Civilization civ = Game.getCiv(civilizationId);
        if (civ == null) return provinces;
        for (int i = 0; i < civ.getNumOfProvinces(); i++) {
            provinces.add(civ.getProvinceID(i));
        }
        return provinces;
    }

    @Override public int locationOf(int civilizationId, String armyKey) {
        Civilization civ = Game.getCiv(civilizationId);
        if (civ == null) return -1;
        for (int i = 0; i < civ.iArmyPositionSize; i++) {
            if (armyKey.equals(civ.getArmyPositionKey(i))) {
                return civ.getArmyPosition(i);
            }
        }
        return -1;
    }

    @Override public int armyStrength(int civilizationId, String armyKey) {
        int provinceId = locationOf(civilizationId, armyKey);
        if (provinceId < 0) return 0;
        ArmyDivision army = Game.getProvince(provinceId).getArmy(armyKey);
        return army == null ? 0 : Math.max(0, army.iArmyRegimentSize);
    }

    @Override public int distance(int fromProvinceId, int toProvinceId) {
        return Math.max(0, Math.round(Game.getDistanceFromProvinceToProvince_Simpler(
                fromProvinceId, toProvinceId)));
    }

    @Override public boolean isMoving(int civilizationId, String armyKey) {
        Civilization civ = Game.getCiv(civilizationId);
        return civ != null && civ.isInMoveUnits_ArmyKey(armyKey);
    }

    @Override public boolean isAtWar(int civilizationA, int civilizationB) {
        return DiplomacyManager.isAtWar(civilizationA, civilizationB);
    }

    @Override public boolean issueMove(
            int civilizationId, String armyKey, int toProvinceId, boolean offensive) {
        return movement != null
                && movement.issueMove(civilizationId, armyKey, toProvinceId, offensive);
    }
}
