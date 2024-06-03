package com.asikora.spacexdragonrockets.exceptions;

public class WrongStatusException extends RuntimeException {
    public WrongStatusException(String errorMessage) {
        super(errorMessage);
    }
}
