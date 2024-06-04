package com.asikora.spacexdragonrockets.enums;

public enum MissionStatus {
    SCHEDULED("Scheduled"),
    PENDING("Pending"),
    IN_PROGRESS("In progress"),
    ENDED("Ended");

    private final String name;

    MissionStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
