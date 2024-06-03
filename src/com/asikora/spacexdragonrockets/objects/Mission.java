package com.asikora.spacexdragonrockets.objects;

import com.asikora.spacexdragonrockets.enums.MissionStatus;

import java.util.Map;

public class Mission {
    private final String name;
    private MissionStatus status;
    private Map<String, Rocket> rockets;

    public Mission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public Map<String, Rocket> getRockets() {
        return rockets;
    }

    public void setRockets(Map<String, Rocket> rockets) {
        this.rockets = rockets;
    }
}
