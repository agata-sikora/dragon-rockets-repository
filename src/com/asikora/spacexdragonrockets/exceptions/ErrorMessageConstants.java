package com.asikora.spacexdragonrockets.exceptions;

public class ErrorMessageConstants {

    private ErrorMessageConstants() {}

    public static final String ROCKET_ALREADY_EXISTS = "A rocket with such name already exists";
    public static final String MISSION_ALREADY_EXISTS = "A mission with such name already exists";
    public static final String CANNOT_CHANGE_STATUS_TO_IN_SPACE = "Rocket status cannot be changed to IN SPACE if it has no mission assigned";
    public static final String CANNOT_CHANGE_STATUS_TO_ON_GROUND = "Rocket status cannot be changed to ON GROUND if it has a mission assigned";
    public static final String MISSION_ALREADY_ASSIGNED = "A mission was already assigned to this rocket";
    public static final String MISSION_NOT_EXISTS = "A mission with such name does not exist";
}