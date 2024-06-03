package com.asikora.spacexdragonrockets.repository.impl;

import com.asikora.spacexdragonrockets.enums.MissionStatus;
import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.exceptions.DuplicatedNameException;
import com.asikora.spacexdragonrockets.exceptions.ErrorMessageConstants;
import com.asikora.spacexdragonrockets.exceptions.WrongStatusException;
import com.asikora.spacexdragonrockets.objects.Mission;
import com.asikora.spacexdragonrockets.objects.Rocket;
import com.asikora.spacexdragonrockets.repository.RocketRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class DragonRocketRepository implements RocketRepository {

    private final Map<String, Rocket> rockets = new HashMap<>();
    private final Map<String, Mission> missions = new HashMap<>();

    @Override
    public void addRocket(String name) {
        if (rockets.containsKey(name)) {
            throw new DuplicatedNameException(ErrorMessageConstants.ROCKET_ALREADY_EXISTS);
        }
        rockets.put(name, new Rocket(name));
    }

    @Override
    public void setRocketsMission(String rocketName, String missionName) {

    }

    @Override
    public void changeRocketStatus(String name, RocketStatus status) {
        if (!rockets.containsKey(name)) {
            throw new NoSuchElementException();
        }
        Rocket rocket = rockets.get(name);
        if (RocketStatus.IN_REPAIR.equals(status)) {
            rocket.setStatus(status);
            if (rocket.getMissionName() != null) {
                missions.get(rocket.getMissionName()).setStatus(MissionStatus.PENDING);
            }
        }
        else if (RocketStatus.IN_SPACE.equals(status) && rocket.getMissionName() != null) {
            rocket.setStatus(status);
            missions.get(rocket.getMissionName()).setStatus(MissionStatus.IN_PROGRESS);
        }
        else if (RocketStatus.IN_SPACE.equals(status) && rocket.getMissionName() == null) {
            throw new WrongStatusException(ErrorMessageConstants.CANNOT_CHANGE_STATUS_TO_IN_SPACE);
        }
        else if (RocketStatus.ON_GROUND.equals(status) && rocket.getMissionName() == null) {
            rocket.setStatus(status);
        }
        else if (RocketStatus.ON_GROUND.equals(status) && rocket.getMissionName() != null) {
            throw new WrongStatusException(ErrorMessageConstants.CANNOT_CHANGE_STATUS_TO_ON_GROUND);
        }
    }

    @Override
    public Map<String, Rocket> getRockets() {
        return rockets;
    }

    @Override
    public void addMission(String name) {
        if (missions.containsKey(name)) {
            throw new DuplicatedNameException(ErrorMessageConstants.MISSION_ALREADY_EXISTS);
        }
        missions.put(name, new Mission(name));
    }

    @Override
    public void assignRocketsToMission(String name, List<String> rockets) {

    }

    @Override
    public Map<String, Mission> getMissions() {
        return missions;
    }

    @Override
    public void getMissionsSummary() {

    }
}
