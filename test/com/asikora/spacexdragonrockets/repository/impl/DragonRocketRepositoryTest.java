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
import java.util.HashMap;
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
        assertNull(result.getMission());
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
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_SPACE);
        Mission mission = createMission(repository, MissionStatus.IN_PROGRESS);
        rocket.setMission(mission);
        mission.addRockets(List.of(rocket));

        // when
        repository.changeRocketStatus(rocket, RocketStatus.IN_REPAIR);

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
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_REPAIR);
        Mission mission = createMission(repository, MissionStatus.PENDING);
        rocket.setMission(mission);
        mission.addRockets(List.of(rocket));

        // when
        repository.changeRocketStatus(rocket, RocketStatus.IN_SPACE);

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
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_REPAIR);

        // when then
        Throwable exception = assertThrows(WrongStatusException.class, () -> repository.changeRocketStatus(rocket, RocketStatus.IN_SPACE));
        assertEquals(ErrorMessageConstants.CANNOT_CHANGE_STATUS_TO_IN_SPACE, exception.getMessage());
    }

    @Test
    void shouldChangeRocketStatusToOnGroundIfItHasNoMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_REPAIR);

        // when
        repository.changeRocketStatus(rocket, RocketStatus.ON_GROUND);

        // then
        Rocket rocketResult = repository.getRockets().get(ROCKET_ONE_NAME);
        assertEquals(RocketStatus.ON_GROUND, rocketResult.getStatus());
    }

    @Test
    void shouldNotChangeRocketStatusToOnGroundIfItHasMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_REPAIR);
        Mission mission = createMission(repository, MissionStatus.PENDING);
        rocket.setMission(mission);
        mission.addRockets(List.of(rocket));

        // when then
        Throwable exception = assertThrows(WrongStatusException.class, () -> repository.changeRocketStatus(rocket, RocketStatus.ON_GROUND));
        assertEquals(ErrorMessageConstants.CANNOT_CHANGE_STATUS_TO_ON_GROUND, exception.getMessage());
    }

    @Test
    void shouldAssignMissionToRocketAndChangeStatuses() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.ON_GROUND);
        Mission mission = createMission(repository, MissionStatus.SCHEDULED);

        // when
        repository.setRocketsMission(rocket, mission);

        // then
        Rocket rocketResult = repository.getRockets().get(ROCKET_ONE_NAME);
        Mission missionResult = repository.getMissions().get(MISSION_ONE_NAME);
        assertEquals(mission, rocketResult.getMission());
        assertEquals(RocketStatus.IN_SPACE, rocketResult.getStatus());
        assertEquals(List.of(rocket), missionResult.getRockets());
        assertEquals(MissionStatus.IN_PROGRESS, missionResult.getStatus());
    }

    @Test
    void shouldNotAssignMissionToRocketIfItAlreadyHasMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_SPACE);
        Mission mission = createMission(repository, MissionStatus.IN_PROGRESS);
        rocket.setMission(mission);
        mission.addRockets(List.of(rocket));

        // when then
        Throwable exception = assertThrows(AssignMissionException.class, () -> repository.setRocketsMission(rocket, mission));
        assertEquals(ErrorMessageConstants.MISSION_ALREADY_ASSIGNED, exception.getMessage());
    }

    @Test
    void shouldNotAssignMissionIfRocketIsInRepair() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_REPAIR);
        Mission mission = createMission(repository, MissionStatus.SCHEDULED);

        // when then
        Throwable exception = assertThrows(AssignMissionException.class, () -> repository.setRocketsMission(rocket, mission));
        assertEquals(ErrorMessageConstants.CANNOT_ASSIGN_MISSION_TO_ROCKET_IN_REPAIR, exception.getMessage());
    }

    @Test
    void shouldAssignRocketsToMissionAndChangeStatuses() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket1 = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.ON_GROUND);
        Rocket rocket2 = createRocket(repository, rocketsMap, ROCKET_TWO_NAME, RocketStatus.ON_GROUND);
        Mission mission = createMission(repository, MissionStatus.SCHEDULED);

        // when
        repository.assignRocketsToMission(mission, List.of(rocket1, rocket2));

        // then
        Map<String, Rocket> rocketsResult = repository.getRockets();
        Mission missionResult = repository.getMissions().get(MISSION_ONE_NAME);
        assertEquals(List.of(rocket1, rocket2), missionResult.getRockets());
        assertEquals(MissionStatus.IN_PROGRESS, missionResult.getStatus());
        rocketsResult.values().forEach(rocket -> assertEquals(RocketStatus.IN_SPACE, rocket.getStatus()));
    }

    @Test
    void shouldNotAssignRocketToMissionIfItAlreadyHasMission() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket1 = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_SPACE);
        Rocket rocket2 = createRocket(repository, rocketsMap, ROCKET_TWO_NAME, RocketStatus.ON_GROUND);
        Mission mission = createMission(repository, MissionStatus.IN_PROGRESS);
        rocket1.setMission(mission);
        mission.addRockets(List.of(rocket1));

        // when then
        Throwable exception = assertThrows(AssignRocketsException.class, () -> repository.assignRocketsToMission(mission, List.of(rocket1, rocket2)));
        assertEquals(ErrorMessageConstants.ROCKET_ALREADY_HAS_MISSION, exception.getMessage());
    }

    @Test
    void shouldNotAssignRocketsIfOneIsInRepair() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_REPAIR);
        Mission mission = createMission(repository, MissionStatus.SCHEDULED);

        // when then
        Throwable exception = assertThrows(AssignRocketsException.class, () -> repository.assignRocketsToMission(mission, List.of(rocket)));
        assertEquals(ErrorMessageConstants.CANNOT_ASSIGN_MISSION_TO_ROCKET_IN_REPAIR, exception.getMessage());
    }

    @Test
    void shouldEndMissionThatIsInProgress() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_SPACE);
        Mission mission = createMission(repository, MissionStatus.IN_PROGRESS);
        rocket.setMission(mission);
        mission.addRockets(List.of(rocket));

        // when
        repository.endMission(mission);

        // then
        Mission missionResult = repository.getMissions().get(MISSION_ONE_NAME);
        Rocket rocketResult = repository.getRockets().get(ROCKET_ONE_NAME);
        assertEquals(MissionStatus.ENDED, missionResult.getStatus());
        assertEquals(Collections.emptyList(), missionResult.getRockets());
        assertEquals(RocketStatus.ON_GROUND, rocketResult.getStatus());
        assertNull(rocketResult.getMission());
    }

    @Test
    void shouldNotEndMissionThatIsNotInProgress() {
        // given
        RocketRepository repository = new DragonRocketRepository();
        Map<String, Rocket> rocketsMap = new HashMap<>();
        Rocket rocket = createRocket(repository, rocketsMap, ROCKET_ONE_NAME, RocketStatus.IN_REPAIR);
        Mission mission = createMission(repository, MissionStatus.PENDING);
        rocket.setMission(mission);
        mission.addRockets(List.of(rocket));

        // when then
        Throwable exception = assertThrows(WrongStatusException.class, () -> repository.endMission(mission));
        assertEquals(ErrorMessageConstants.CANNOT_END_MISSION, exception.getMessage());
    }

    private Rocket createRocket(RocketRepository repository, Map<String, Rocket> rocketsMap, String rocketName, RocketStatus rocketStatus) {
        Rocket rocket = new Rocket(rocketName);
        rocket.setStatus(rocketStatus);
        rocketsMap.put(rocket.getName(), rocket);
        Whitebox.setInternalState(repository, "rockets", rocketsMap);
        return rocket;
    }

    private Mission createMission(RocketRepository repository, MissionStatus missionStatus) {
        Mission mission = new Mission(MISSION_ONE_NAME);
        mission.setStatus(missionStatus);
        Map<String, Mission> missionsMap = new HashMap<>();
        missionsMap.put(mission.getName(), mission);
        Whitebox.setInternalState(repository, "missions", missionsMap);
        return mission;
    }
}
