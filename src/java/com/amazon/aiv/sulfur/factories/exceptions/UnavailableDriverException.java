package com.amazon.aiv.sulfur.factories.exceptions;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class UnavailableDriverException extends RuntimeException {

    public UnavailableDriverException(String driverName) {
        super(driverName);
    }

    @Override
    public String getMessage() {
        return String.format("The requested Driver is not currently available '%s'", getMessage());
    }
}
