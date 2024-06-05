package com.asikora.spacexdragonrockets;

import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.objects.Mission;
import com.asikora.spacexdragonrockets.objects.Rocket;
import com.asikora.spacexdragonrockets.repository.RocketRepository;
import com.asikora.spacexdragonrockets.repository.impl.DragonRocketRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        RocketRepository repository = new DragonRocketRepository();

        repository.addRocket("Dragon1");
        repository.addRocket("Dragon2");
        repository.addMission("Mars");

        Mission marsMission = repository.getMissions().get("Mars");
        Rocket dragon1Rocket = repository.getRockets().get("Dragon1");
        Rocket dragon2Rocket = repository.getRockets().get("Dragon2");

        repository.assignRocketsToMission(marsMission, List.of(dragon1Rocket, dragon2Rocket));

        repository.addRocket("Red Dragon");
        repository.addMission("Luna1");

        Mission luna1Mission = repository.getMissions().get("Luna1");
        Rocket redDragonRocket = repository.getRockets().get("Red Dragon");

        repository.setRocketsMission(redDragonRocket, luna1Mission);

        repository.changeRocketStatus(dragon1Rocket, RocketStatus.IN_REPAIR);

        repository.getMissionsSummary();
    }
}