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
    public void setRocketsMission(Rocket rocket, Mission mission) {
        if (rocket== null || mission== null || !rockets.containsKey(rocket.getName())|| !missions.containsKey(mission.getName())) {
            throw new NoSuchElementException();
        }
        if (rocket.getMission() != null) {
            throw new AssignMissionException(ErrorMessageConstants.MISSION_ALREADY_ASSIGNED);
        }
        if (RocketStatus.IN_REPAIR.equals(rocket.getStatus())) {
            throw new AssignMissionException(ErrorMessageConstants.CANNOT_ASSIGN_MISSION_TO_ROCKET_IN_REPAIR);
        }

        rocket.setStatus(RocketStatus.IN_SPACE);
        mission.setStatus(MissionStatus.IN_PROGRESS);
        rocket.setMission(mission);
        mission.addRockets(List.of(rocket));

        rockets.put(rocket.getName(), rocket);
        missions.put(mission.getName(), mission);
    }

    @Override
    public void changeRocketStatus(Rocket rocket, RocketStatus status) {
        if (rocket == null || !rockets.containsKey(rocket.getName())) {
            throw new NoSuchElementException();
        }
        Mission mission = rocket.getMission();
        if (RocketStatus.IN_REPAIR.equals(status)) {
            rocket.setStatus(status);
            if (rocket.getMission() != null) {
                mission.setStatus(MissionStatus.PENDING);
                missions.put(mission.getName(), mission);
            }
            rockets.put(rocket.getName(), rocket);
        }
        else if (RocketStatus.IN_SPACE.equals(status)) {
            if (rocket.getMission() == null) {
                throw new WrongStatusException(ErrorMessageConstants.CANNOT_CHANGE_STATUS_TO_IN_SPACE);
            }
            rocket.setStatus(status);
            mission.setStatus(MissionStatus.IN_PROGRESS);
            rockets.put(rocket.getName(), rocket);
            missions.put(mission.getName(), mission);
        }
        else if (RocketStatus.ON_GROUND.equals(status)) {
            if (rocket.getMission() != null) {
                throw new WrongStatusException(ErrorMessageConstants.CANNOT_CHANGE_STATUS_TO_ON_GROUND);
            }
            rocket.setStatus(status);
            rockets.put(rocket.getName(), rocket);
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
    public void assignRocketsToMission(Mission mission, List<Rocket> rocketsList) {
        if (mission == null || !missions.containsKey(mission.getName())) {
            throw new NoSuchElementException();
        }
        if (rocketsList == null || rocketsList.isEmpty()) {
            throw new AssignRocketsException(ErrorMessageConstants.NO_ROCKETS_TO_ASSIGN);
        }
        rocketsList.forEach(rocket -> {
            if (rocket == null || !rockets.containsKey(rocket.getName())) {
                throw new NoSuchElementException();
            }
            if (rocket.getMission() != null) {
                throw new AssignRocketsException(ErrorMessageConstants.ROCKET_ALREADY_HAS_MISSION);
            }
            if (RocketStatus.IN_REPAIR.equals(rocket.getStatus())) {
                throw new AssignRocketsException(ErrorMessageConstants.CANNOT_ASSIGN_MISSION_TO_ROCKET_IN_REPAIR);
            }
            rocket.setMission(mission);
            rocket.setStatus(RocketStatus.IN_SPACE);
            rockets.put(rocket.getName(), rocket);
        });
        mission.setStatus(MissionStatus.IN_PROGRESS);
        mission.addRockets(rocketsList);
        missions.put(mission.getName(), mission);
    }

    @Override
    public void endMission(Mission mission) {
        if (mission == null || !missions.containsKey(mission.getName())) {
            throw new NoSuchElementException();
        }
        if (MissionStatus.PENDING.equals(mission.getStatus()) || MissionStatus.SCHEDULED.equals(mission.getStatus())) {
            throw new WrongStatusException(ErrorMessageConstants.CANNOT_END_MISSION);
        }
        mission.setStatus(MissionStatus.ENDED);
        mission.getRockets().forEach(rocket -> {
            rocket.setStatus(RocketStatus.ON_GROUND);
            rocket.setMission(null);
            rockets.put(rocket.getName(), rocket);
        });
        mission.deleteRockets();
        missions.put(mission.getName(), mission);
    }

    @Override
    public Map<String, Mission> getMissions() {
        return missions;
    }

    @Override
    public void getMissionsSummary() {
        List<Mission> sortedMissions = missions.values()
                .stream()
                .sorted(Comparator.comparing((Mission m) ->
                        m.getRockets().size())
                        .thenComparing(Mission::getName)
                        .reversed())
                .toList();

        sortedMissions.forEach(mission -> System.out.println(mission.toString()));
    }
}
