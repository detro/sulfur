package com.amazon.aiv.sulfur.factories.exceptions;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class SMissingPathParamException extends RuntimeException {

    public SMissingPathParamException(String pathParamName) {
        super(pathParamName);
    }

    @Override
    public String getMessage() {
        return String.format("The MANDATORY Path Parameter '%s' is missing", super.getMessage());
    }
}
