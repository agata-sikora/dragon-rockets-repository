package com.asikora.spacexdragonrockets.repository.impl;

import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.exceptions.DuplicatedNameException;
import com.asikora.spacexdragonrockets.objects.Rocket;
import com.asikora.spacexdragonrockets.repository.RocketRepository;
import org.junit.jupiter.api.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DragonRocketRepositoryTest {

    private final String ROCKET_ONE_NAME = "Dragon1";

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

}
