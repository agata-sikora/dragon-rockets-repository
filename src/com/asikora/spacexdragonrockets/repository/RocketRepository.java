package com.asikora.spacexdragonrockets.repository;

import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.objects.Mission;
import com.asikora.spacexdragonrockets.objects.Rocket;

import java.util.List;
import java.util.Map;

public interface RocketRepository {

    void addRocket(String name);

    void setRocketsMission(Rocket rocket, Mission mission);

    void changeRocketStatus(Rocket rocket, RocketStatus status);

    Map<String, Rocket> getRockets();

    void addMission(String missionName);

    void assignRocketsToMission(Mission mission, List<Rocket> rockets);

    void endMission(Mission mission);

    Map<String, Mission> getMissions();

    void getMissionsSummary();
}
