package com.asikora.spacexdragonrockets.repository.impl;

import com.asikora.spacexdragonrockets.enums.MissionStatus;
import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.exceptions.DuplicatedNameException;
import com.asikora.spacexdragonrockets.objects.Mission;
import com.asikora.spacexdragonrockets.objects.Rocket;
import com.asikora.spacexdragonrockets.repository.RocketRepository;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DragonRocketRepositoryTest {

    private final String ROCKET_ONE_NAME = "Dragon1";
    private final String MISSION_ONE_NAME = "Mission1";

    @Test
    void shouldAddRocketWithOnGroundStatusAndNoMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();

        // when
        repository.addRocket(ROCKET_ONE_NAME);

        // then
        Map<String, Rocket> resultsMap = repository.getRockets();
        assertEquals(1, resultsMap.size());
        Rocket result = resultsMap.get(ROCKET_ONE_NAME);
        assertEquals(ROCKET_ONE_NAME, result.getName());
        assertEquals(RocketStatus.ON_GROUND, result.getStatus());
        assertNull(result.getMission());
    }

    @Test
    void shouldThrowExceptionWhenTryingToAddTwoRocketsWithTheSameName() {
        // given
        RocketRepository repository = new DragonRocketRepository();

        // when
        repository.addRocket(ROCKET_ONE_NAME);

        // then
        Throwable exception = assertThrows(DuplicatedNameException.class, () -> repository.addRocket(ROCKET_ONE_NAME));
        assertEquals("A rocket with such name already exists", exception.getMessage());
    }

    @Test
    void shouldAddMissionWithScheduledStatusAndNoRockets() {
        // given
        RocketRepository repository = new DragonRocketRepository();

        // when
        repository.addMission(MISSION_ONE_NAME);

        // then
        Map<String, Mission> resultsMap = repository.getMissions();
        assertEquals(1, resultsMap.size());
        Mission result = resultsMap.get(MISSION_ONE_NAME);
        assertEquals(MISSION_ONE_NAME, result.getName());
        assertEquals(MissionStatus.SCHEDULED, result.getStatus());
        assertEquals(Collections.emptyMap(), result.getRockets());
    }

    @Test
    void shouldThrowExceptionWhenTryingToAddTwoMissionsWithTheSameName() {
        // given
        RocketRepository repository = new DragonRocketRepository();

        // when
        repository.addMission(MISSION_ONE_NAME);

        // then
        Throwable exception = assertThrows(DuplicatedNameException.class, () -> repository.addMission(MISSION_ONE_NAME));
        assertEquals("A mission with such name already exists", exception.getMessage());
    }

}
