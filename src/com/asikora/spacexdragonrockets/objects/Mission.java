package com.asikora.spacexdragonrockets.objects;

import com.asikora.spacexdragonrockets.enums.MissionStatus;

import java.util.ArrayList;
import java.util.List;

public class Mission {
    private final String name;
    private MissionStatus status = MissionStatus.SCHEDULED;
    private final List<String> rockets = new ArrayList<>();

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

    public void addRockets(List<String> rockets) {
        this.rockets.addAll(rockets);
    }

    public void deleteRockets() {
        this.rockets.clear();
    }
}
