package com.asikora.spacexdragonrockets.repository;

import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.objects.Mission;
import com.asikora.spacexdragonrockets.objects.Rocket;

import java.util.List;
import java.util.Map;

public interface RocketRepository {

    void addRocket(String name);

    void setRocketsMission(String rocketName, String missionName);

    void changeRocketStatus(String name, RocketStatus status);

    Map<String, Rocket> getRockets();

    void addMission(String name);

    void assignRocketsToMission(String missionName, List<String> rockets);

    Map<String, Mission> getMissions();

    void getMissionsSummary();
}
