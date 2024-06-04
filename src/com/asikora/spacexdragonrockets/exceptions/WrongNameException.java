package com.asikora.spacexdragonrockets.exceptions;

public class WrongNameException extends RuntimeException {
    public WrongNameException(String errorMessage) {
        super(errorMessage);
    }
}
