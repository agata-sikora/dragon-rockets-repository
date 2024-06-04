package com.asikora.spacexdragonrockets.enums;

public enum RocketStatus {
    ON_GROUND("On ground"),
    IN_SPACE("In space"),
    IN_REPAIR("In repair");

    private final String name;

    RocketStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
