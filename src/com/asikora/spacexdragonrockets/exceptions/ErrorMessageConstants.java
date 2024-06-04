package com.asikora.spacexdragonrockets.exceptions;

public class ErrorMessageConstants {

    private ErrorMessageConstants() {}

    public static final String NAME_CANNOT_BE_NULL = "Name cannot be null";
    public static final String ROCKET_ALREADY_EXISTS = "A rocket with such name already exists";
    public static final String MISSION_ALREADY_EXISTS = "A mission with such name already exists";
    public static final String CANNOT_CHANGE_STATUS_TO_IN_SPACE = "Rocket status cannot be changed to IN SPACE if it has no mission assigned";
    public static final String CANNOT_CHANGE_STATUS_TO_ON_GROUND = "Rocket status cannot be changed to ON GROUND if it has a mission assigned";
    public static final String MISSION_ALREADY_ASSIGNED = "A mission was already assigned to this rocket";
    public static final String CANNOT_ASSIGN_MISSION_TO_ROCKET_IN_REPAIR = "Cannot assign mission to rocket that is in repair";
    public static final String ROCKET_ALREADY_HAS_MISSION = "Some of the rockets already have missions assigned";
    public static final String NO_ROCKETS_TO_ASSIGN = "There are no rockets to assign";
    public static final String CANNOT_END_MISSION = "Cannot end mission that has not started or is pending";
}