package com.asikora.spacexdragonrockets.repository.impl;

import com.asikora.spacexdragonrockets.enums.MissionStatus;
import com.asikora.spacexdragonrockets.enums.RocketStatus;
import com.asikora.spacexdragonrockets.exceptions.*;
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
    private final String ROCKET_TWO_NAME = "Dragon2";
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
        Throwable exception = assertThrows(WrongNameException.class, () -> repository.addRocket(ROCKET_ONE_NAME));
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
        Throwable exception = assertThrows(WrongNameException.class, () -> repository.addMission(MISSION_ONE_NAME));
        assertEquals(ErrorMessageConstants.MISSION_ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    void shouldChangeRocketStatusToInRepairAndMissionStatusToPending() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocket(repository, RocketStatus.IN_SPACE, MISSION_ONE_NAME);
        createMission(repository, MissionStatus.IN_PROGRESS, List.of(ROCKET_ONE_NAME));

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
        createRocket(repository, RocketStatus.IN_REPAIR, MISSION_ONE_NAME);
        createMission(repository, MissionStatus.PENDING, List.of(ROCKET_ONE_NAME));

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
        createRocket(repository, RocketStatus.IN_REPAIR, null);

        // when then
        Throwable exception = assertThrows(WrongStatusException.class, () -> repository.changeRocketStatus(ROCKET_ONE_NAME, RocketStatus.IN_SPACE));
        assertEquals(ErrorMessageConstants.CANNOT_CHANGE_STATUS_TO_IN_SPACE, exception.getMessage());
    }

    @Test
    void shouldChangeRocketStatusToOnGroundIfItHasNoMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocket(repository, RocketStatus.IN_REPAIR, null);

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
        createRocket(repository, RocketStatus.IN_REPAIR, MISSION_ONE_NAME);
        createMission(repository, MissionStatus.PENDING, List.of(ROCKET_ONE_NAME));

        // when then
        Throwable exception = assertThrows(WrongStatusException.class, () -> repository.changeRocketStatus(ROCKET_ONE_NAME, RocketStatus.ON_GROUND));
        assertEquals(ErrorMessageConstants.CANNOT_CHANGE_STATUS_TO_ON_GROUND, exception.getMessage());
    }

    @Test
    void shouldAssignMissionToRocketAndChangeStatuses() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocket(repository, RocketStatus.ON_GROUND, null);
        createMission(repository, MissionStatus.SCHEDULED, Collections.emptyList());

        // when
        repository.setRocketsMission(ROCKET_ONE_NAME, MISSION_ONE_NAME);

        // then
        Rocket rocketResult = repository.getRockets().get(ROCKET_ONE_NAME);
        Mission missionResult = repository.getMissions().get(MISSION_ONE_NAME);
        assertEquals(MISSION_ONE_NAME, rocketResult.getMissionName());
        assertEquals(RocketStatus.IN_SPACE, rocketResult.getStatus());
        assertEquals(List.of(ROCKET_ONE_NAME), missionResult.getRockets());
        assertEquals(MissionStatus.IN_PROGRESS, missionResult.getStatus());
    }

    @Test
    void shouldNotAssignMissionToRocketIfItAlreadyHasMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocket(repository, RocketStatus.IN_SPACE, MISSION_ONE_NAME);
        createMission(repository, MissionStatus.IN_PROGRESS, List.of(ROCKET_ONE_NAME));

        // when then
        Throwable exception = assertThrows(AssignMissionException.class, () -> repository.setRocketsMission(ROCKET_ONE_NAME, MISSION_ONE_NAME));
        assertEquals(ErrorMessageConstants.MISSION_ALREADY_ASSIGNED, exception.getMessage());
    }

    @Test
    void shouldNotAssignMissionIfRocketIsInRepair() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocket(repository, RocketStatus.IN_REPAIR, null);
        createMission(repository, MissionStatus.SCHEDULED, Collections.emptyList());

        // when then
        Throwable exception = assertThrows(AssignMissionException.class, () -> repository.setRocketsMission(ROCKET_ONE_NAME, MISSION_ONE_NAME));
        assertEquals(ErrorMessageConstants.CANNOT_ASSIGN_MISSION_TO_ROCKET_IN_REPAIR, exception.getMessage());
    }

    @Test
    void shouldAssignRocketsToMissionAndChangeStatuses() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createTwoRockets(repository, RocketStatus.ON_GROUND, RocketStatus.ON_GROUND, null, null);
        createMission(repository, MissionStatus.SCHEDULED, Collections.emptyList());

        // when
        repository.assignRocketsToMission(MISSION_ONE_NAME, List.of(ROCKET_ONE_NAME, ROCKET_TWO_NAME));

        // then
        Map<String, Rocket> rocketsResult = repository.getRockets();
        Mission missionResult = repository.getMissions().get(MISSION_ONE_NAME);
        assertEquals(List.of(ROCKET_ONE_NAME, ROCKET_TWO_NAME), missionResult.getRockets());
        assertEquals(MissionStatus.IN_PROGRESS, missionResult.getStatus());
        rocketsResult.values().forEach(rocket -> assertEquals(RocketStatus.IN_SPACE, rocket.getStatus()));
    }

    @Test
    void shouldNotAssignRocketToMissionIfItAlreadyHasMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createTwoRockets(repository, RocketStatus.IN_SPACE, RocketStatus.ON_GROUND, MISSION_ONE_NAME, null);
        createMission(repository, MissionStatus.IN_PROGRESS, List.of(ROCKET_ONE_NAME));

        // when then
        Throwable exception = assertThrows(AssignRocketsException.class, () -> repository.assignRocketsToMission(MISSION_ONE_NAME, List.of(ROCKET_ONE_NAME, ROCKET_TWO_NAME)));
        assertEquals(ErrorMessageConstants.ROCKET_ALREADY_HAS_MISSION, exception.getMessage());
    }

    @Test
    void shouldNotAssignRocketsIfOneIsInRepair() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocket(repository, RocketStatus.IN_REPAIR, null);
        createMission(repository, MissionStatus.SCHEDULED, List.of());

        // when then
        Throwable exception = assertThrows(AssignRocketsException.class, () -> repository.assignRocketsToMission(MISSION_ONE_NAME, List.of(ROCKET_ONE_NAME)));
        assertEquals(ErrorMessageConstants.CANNOT_ASSIGN_MISSION_TO_ROCKET_IN_REPAIR, exception.getMessage());
    }

    @Test
    void shouldEndMissionThatIsInProgress() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocket(repository, RocketStatus.IN_SPACE, MISSION_ONE_NAME);
        createMission(repository, MissionStatus.IN_PROGRESS, List.of(ROCKET_ONE_NAME));

        // when
        repository.endMission(MISSION_ONE_NAME);

        // then
        Mission missionResult = repository.getMissions().get(MISSION_ONE_NAME);
        Rocket rocketResult = repository.getRockets().get(ROCKET_ONE_NAME);
        assertEquals(MissionStatus.ENDED, missionResult.getStatus());
        assertEquals(Collections.emptyList(), missionResult.getRockets());
        assertEquals(RocketStatus.ON_GROUND, rocketResult.getStatus());
        assertNull(rocketResult.getMissionName());
    }

    @Test
    void shouldNotEndMissionThatIsNotInProgress() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        createRocket(repository, RocketStatus.IN_REPAIR, MISSION_ONE_NAME);
        createMission(repository, MissionStatus.PENDING, List.of(ROCKET_ONE_NAME));

        // when then
        Throwable exception = assertThrows(WrongStatusException.class, () -> repository.endMission(MISSION_ONE_NAME));
        assertEquals(ErrorMessageConstants.CANNOT_END_MISSION, exception.getMessage());
    }

    private void createRocket(RocketRepository repository, RocketStatus rocketStatus, String missionName) {
        Rocket rocket = new Rocket(ROCKET_ONE_NAME);
        rocket.setStatus(rocketStatus);
        rocket.setMissionName(missionName);
        Map<String, Rocket> rocketsMap = Map.of(ROCKET_ONE_NAME, rocket);
        Whitebox.setInternalState(repository, "rockets", rocketsMap);
    }

    private void createMission(RocketRepository repository, MissionStatus missionStatus, List<String> rockets) {
        Mission mission = new Mission(MISSION_ONE_NAME);
        mission.setStatus(missionStatus);
        mission.addRockets(rockets);
        Map<String, Mission> missionsMap = Map.of(MISSION_ONE_NAME, mission);
        Whitebox.setInternalState(repository, "missions", missionsMap);
    }

    private void createTwoRockets(RocketRepository repository, RocketStatus status1, RocketStatus status2, String mission1Name, String mission2Name) {
        Rocket rocket1 = new Rocket(ROCKET_ONE_NAME);
        rocket1.setStatus(status1);
        rocket1.setMissionName(mission1Name);
        Rocket rocket2 = new Rocket(ROCKET_TWO_NAME);
        rocket2.setStatus(status2);
        rocket2.setMissionName(mission2Name);
        Map<String, Rocket> rocketsMap = Map.of(ROCKET_ONE_NAME, rocket1, ROCKET_TWO_NAME, rocket2);
        Whitebox.setInternalState(repository, "rockets", rocketsMap);
    }
}
