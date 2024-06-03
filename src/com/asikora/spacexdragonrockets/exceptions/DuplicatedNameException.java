package com.asikora.spacexdragonrockets.exceptions;

public class DuplicatedNameException extends RuntimeException {
    public DuplicatedNameException(String errorMessage) {
        super(errorMessage);
    }
}
