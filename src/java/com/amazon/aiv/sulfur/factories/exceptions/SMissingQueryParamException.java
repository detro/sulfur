package com.amazon.aiv.sulfur.factories.exceptions;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class SMissingQueryParamException extends RuntimeException {

    public SMissingQueryParamException(String queryParamName) {
        super(queryParamName);
    }

    @Override
    public String getMessage() {
        return String.format("The MANDATORY Query Parameter '%s' is missing", super.getMessage());
    }
}
