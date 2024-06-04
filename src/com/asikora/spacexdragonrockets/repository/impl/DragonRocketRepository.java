package com.asikora.spacexdragonrockets.repository.impl;

import com.asikora.spacexdragonrockets.enums.MissionStatus;
import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.exceptions.*;
import com.asikora.spacexdragonrockets.objects.Mission;
import com.asikora.spacexdragonrockets.objects.Rocket;
import com.asikora.spacexdragonrockets.repository.RocketRepository;

import java.util.*;

public class DragonRocketRepository implements RocketRepository {

    private final Map<String, Rocket> rockets = new HashMap<>();
    private final Map<String, Mission> missions = new HashMap<>();

    @Override
    public void addRocket(String name) {
        if (name == null) {
            throw new WrongNameException(ErrorMessageConstants.NAME_CANNOT_BE_NULL);
        }
        if (rockets.containsKey(name)) {
            throw new WrongNameException(ErrorMessageConstants.ROCKET_ALREADY_EXISTS);
        }
        rockets.put(name, new Rocket(name));
    }

    @Override
    public void setRocketsMission(String rocketName, String missionName) {
        if (rocketName == null || missionName == null || !rockets.containsKey(rocketName) || !missions.containsKey(missionName)) {
            throw new NoSuchElementException();
        }
        Rocket rocket = rockets.get(rocketName);
        Mission mission = missions.get(missionName);
        if (rocket.getMissionName() != null) {
            throw new AssignMissionException(ErrorMessageConstants.MISSION_ALREADY_ASSIGNED);
        }
        if (RocketStatus.IN_REPAIR.equals(rocket.getStatus())) {
            throw new AssignMissionException(ErrorMessageConstants.CANNOT_ASSIGN_MISSION_TO_ROCKET_IN_REPAIR);
        }

        rocket.setMissionName(missionName);
        mission.addRockets(List.of(rocketName));
        rocket.setStatus(RocketStatus.IN_SPACE);
        mission.setStatus(MissionStatus.IN_PROGRESS);
    }

    @Override
    public void changeRocketStatus(String name, RocketStatus status) {
        if (name == null || !rockets.containsKey(name)) {
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
        if (name == null) {
            throw new WrongNameException(ErrorMessageConstants.NAME_CANNOT_BE_NULL);
        }
        if (missions.containsKey(name)) {
            throw new WrongNameException(ErrorMessageConstants.MISSION_ALREADY_EXISTS);
        }
        missions.put(name, new Mission(name));
    }

    @Override
    public void assignRocketsToMission(String missionName, List<String> rocketsList) {
        if (missionName == null || !missions.containsKey(missionName)) {
            throw new NoSuchElementException();
        }
        if (rocketsList == null || rocketsList.isEmpty()) {
            throw new AssignRocketsException(ErrorMessageConstants.NO_ROCKETS_TO_ASSIGN);
        }
        Mission mission = missions.get(missionName);
        rocketsList.forEach(rocketName -> {
            if (rocketName == null || !rockets.containsKey(rocketName)) {
                throw new NoSuchElementException();
            }
            Rocket rocket = rockets.get(rocketName);
            if (rocket.getMissionName() != null) {
                throw new AssignRocketsException(ErrorMessageConstants.ROCKET_ALREADY_HAS_MISSION);
            }
            if (RocketStatus.IN_REPAIR.equals(rocket.getStatus())) {
                throw new AssignRocketsException(ErrorMessageConstants.CANNOT_ASSIGN_MISSION_TO_ROCKET_IN_REPAIR);
            }
            rocket.setStatus(RocketStatus.IN_SPACE);
        });
        mission.setStatus(MissionStatus.IN_PROGRESS);
        mission.addRockets(rocketsList);
    }

    @Override
    public void endMission(String missionName) {
        if (missionName == null || !missions.containsKey(missionName)) {
            throw new NoSuchElementException();
        }
        Mission mission = missions.get(missionName);
        if (MissionStatus.PENDING.equals(mission.getStatus()) || MissionStatus.SCHEDULED.equals(mission.getStatus())) {
            throw new WrongStatusException(ErrorMessageConstants.CANNOT_END_MISSION);
        }
        mission.setStatus(MissionStatus.ENDED);
        mission.getRockets().forEach(rocketName -> {
            Rocket rocket = rockets.get(rocketName);
            rocket.setStatus(RocketStatus.ON_GROUND);
            rocket.setMissionName(null);
        });
        mission.deleteRockets();
    }

    @Override
    public Map<String, Mission> getMissions() {
        return missions;
    }

    @Override
    public void getMissionsSummary() {

    }
}
