package com.asikora.spacexdragonrockets.objects;

import com.asikora.spacexdragonrockets.enums.RocketStatus;

public class Rocket {
    private final String name;
    private RocketStatus status = RocketStatus.ON_GROUND;
    private String missionName;

    public Rocket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public RocketStatus getStatus() {
        return status;
    }

    public void setStatus(RocketStatus status) {
        this.status = status;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }
}
