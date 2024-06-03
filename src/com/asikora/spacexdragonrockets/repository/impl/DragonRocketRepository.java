package com.asikora.spacexdragonrockets.repository.impl;

import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.exceptions.DuplicatedNameException;
import com.asikora.spacexdragonrockets.objects.Mission;
import com.asikora.spacexdragonrockets.objects.Rocket;
import com.asikora.spacexdragonrockets.repository.RocketRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DragonRocketRepository implements RocketRepository {

    private final Map<String, Rocket> rockets = new HashMap<>();

    @Override
    public void addRocket(String name) {
        if (rockets.containsKey(name)) {
            throw new DuplicatedNameException("A rocket with such name already exists");
        }
        rockets.put(name, new Rocket(name));
    }

    @Override
    public void setRocketsMission(String rocketName, String missionName) {

    }

    @Override
    public void changeRocketStatus(String name, RocketStatus status) {

    }

    @Override
    public Map<String, Rocket> getRockets() {
        return rockets;
    }

    @Override
    public void addMission(String name) {

    }

    @Override
    public void assignRocketsToMission(String name, Map<String, Rocket> rockets) {

    }

    @Override
    public Map<String, Mission> getMissions() {
        return Collections.emptyMap();
    }

    @Override
    public void getMissionsSummary() {

    }
}
