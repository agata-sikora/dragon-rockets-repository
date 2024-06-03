package com.asikora.spacexdragonrockets.objects;

import com.asikora.spacexdragonrockets.enums.MissionStatus;

import java.util.Collections;
import java.util.List;

public class Mission {
    private final String name;
    private MissionStatus status = MissionStatus.SCHEDULED;
    private List<String> rockets = Collections.emptyList();

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

    public List<String> getRockets() {
        return rockets;
    }

    public void setRockets(List<String> rockets) {
        this.rockets = rockets;
    }
}
