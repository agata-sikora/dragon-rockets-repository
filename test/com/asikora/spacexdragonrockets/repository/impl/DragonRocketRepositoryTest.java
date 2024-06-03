package com.asikora.spacexdragonrockets.repository.impl;

import com.asikora.spacexdragonrockets.enums.MissionStatus;
import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.exceptions.DuplicatedNameException;
import com.asikora.spacexdragonrockets.exceptions.ErrorMessageConstants;
import com.asikora.spacexdragonrockets.exceptions.WrongStatusException;
import com.asikora.spacexdragonrockets.objects.Mission;
import com.asikora.spacexdragonrockets.objects.Rocket;
import com.asikora.spacexdragonrockets.repository.RocketRepository;
import org.junit.jupiter.api.*;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Collections;
import java.util.List;
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
        assertNull(result.getMissionName());
    }

    @Test
    void shouldThrowExceptionWhenTryingToAddTwoRocketsWithTheSameName() {
        // given
        RocketRepository repository = new DragonRocketRepository();

        // when then
        repository.addRocket(ROCKET_ONE_NAME);
        Throwable exception = assertThrows(DuplicatedNameException.class, () -> repository.addRocket(ROCKET_ONE_NAME));
        assertEquals(ErrorMessageConstants.ROCKET_ALREADY_EXISTS, exception.getMessage());
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
        assertEquals(Collections.emptyList(), result.getRockets());
    }

    @Test
    void shouldThrowExceptionWhenTryingToAddTwoMissionsWithTheSameName() {
        // given
        RocketRepository repository = new DragonRocketRepository();

        // when
        repository.addMission(MISSION_ONE_NAME);

        // then
        Throwable exception = assertThrows(DuplicatedNameException.class, () -> repository.addMission(MISSION_ONE_NAME));
        assertEquals(ErrorMessageConstants.MISSION_ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    void shouldChangeRocketStatusToInRepairAndMissionStatusToPending() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocketWithAssignedMission(repository, RocketStatus.IN_SPACE, MissionStatus.IN_PROGRESS);

        // when
        repository.changeRocketStatus(ROCKET_ONE_NAME, RocketStatus.IN_REPAIR);

        // then
        Rocket rocketResult = repository.getRockets().get(ROCKET_ONE_NAME);
        Mission missionResult = repository.getMissions().get(MISSION_ONE_NAME);
        assertEquals(RocketStatus.IN_REPAIR, rocketResult.getStatus());
        assertEquals(MissionStatus.PENDING, missionResult.getStatus());
    }

    @Test
    void shouldChangeRocketStatusToInSpaceIfItHasMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocketWithAssignedMission(repository, RocketStatus.IN_REPAIR, MissionStatus.IN_PROGRESS);

        // when
        repository.changeRocketStatus(ROCKET_ONE_NAME, RocketStatus.IN_SPACE);

        // then
        Rocket rocketResult = repository.getRockets().get(ROCKET_ONE_NAME);
        Mission missionResult = repository.getMissions().get(MISSION_ONE_NAME);
        assertEquals(RocketStatus.IN_SPACE, rocketResult.getStatus());
        assertEquals(MissionStatus.IN_PROGRESS, missionResult.getStatus());
    }

    @Test
    void shouldNotChangeRocketStatusToInSpaceIfItHasNoMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocketWithNoMission(repository, RocketStatus.IN_REPAIR);

        // when then
        Throwable exception = assertThrows(WrongStatusException.class, () -> repository.changeRocketStatus(ROCKET_ONE_NAME, RocketStatus.IN_SPACE));
        assertEquals(ErrorMessageConstants.CANNOT_CHANGE_STATUS_TO_IN_SPACE, exception.getMessage());
    }

    @Test
    void shouldChangeRocketStatusToOnGroundIfItHasNoMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocketWithNoMission(repository, RocketStatus.IN_REPAIR);

        // when
        repository.changeRocketStatus(ROCKET_ONE_NAME, RocketStatus.ON_GROUND);

        // then
        Rocket rocketResult = repository.getRockets().get(ROCKET_ONE_NAME);
        assertEquals(RocketStatus.ON_GROUND, rocketResult.getStatus());
    }

    @Test
    void shouldNotChangeRocketStatusToOnGroundIfItHasMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocketWithAssignedMission(repository, RocketStatus.IN_REPAIR, MissionStatus.PENDING);

        // when then
        Throwable exception = assertThrows(WrongStatusException.class, () -> repository.changeRocketStatus(ROCKET_ONE_NAME, RocketStatus.ON_GROUND));
        assertEquals(ErrorMessageConstants.CANNOT_CHANGE_STATUS_TO_ON_GROUND, exception.getMessage());
    }

    private void createRocketWithAssignedMission(RocketRepository repository, RocketStatus rocketStatus, MissionStatus missionStatus) {
        Rocket rocket = new Rocket(ROCKET_ONE_NAME);
        rocket.setStatus(rocketStatus);
        Mission mission = new Mission(MISSION_ONE_NAME);
        mission.setStatus(missionStatus);
        rocket.setMissionName(MISSION_ONE_NAME);
        mission.setRockets(List.of(ROCKET_ONE_NAME));
        Map<String, Rocket> rocketsMap = Map.of(ROCKET_ONE_NAME, rocket);
        Map<String, Mission> missionMap = Map.of(MISSION_ONE_NAME, mission);
        Whitebox.setInternalState(repository, "rockets", rocketsMap);
        Whitebox.setInternalState(repository, "missions", missionMap);
    }

    private void createRocketWithNoMission(RocketRepository repository, RocketStatus rocketStatus) {
        Rocket rocket = new Rocket(ROCKET_ONE_NAME);
        rocket.setStatus(rocketStatus);
        Map<String, Rocket> rocketsMap = Map.of(ROCKET_ONE_NAME, rocket);
        Whitebox.setInternalState(repository, "rockets", rocketsMap);
    }
}
