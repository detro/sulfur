package com.amazon.aiv.sulfur.factories.exceptions;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class InvalidDriverNameException extends RuntimeException {

    public InvalidDriverNameException(String driverName) {
        super(driverName);
    }

    @Override
    public String getMessage() {
        return String.format("Invalid Driver Name '%s'", getMessage());
    }
}
