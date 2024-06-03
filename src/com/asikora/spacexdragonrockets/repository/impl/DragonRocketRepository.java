package com.asikora.spacexdragonrockets.repository.impl;

import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.objects.Mission;
import com.asikora.spacexdragonrockets.objects.Rocket;
import com.asikora.spacexdragonrockets.repository.RocketRepository;

import java.util.Collections;
import java.util.Map;

public class DragonRocketRepository implements RocketRepository {

    @Override
    public void addRocket(String name) {

    }

    @Override
    public void setRocketsMission(String rocketName, String missionName) {

    }

    @Override
    public void changeRocketStatus(String name, RocketStatus status) {

    }

    @Override
    public Map<String, Rocket> getRockets() {
        return Collections.emptyMap();
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
