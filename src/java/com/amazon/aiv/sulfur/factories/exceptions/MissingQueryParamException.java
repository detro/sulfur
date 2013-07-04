package com.amazon.aiv.sulfur.factories.exceptions;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class MissingQueryParamException extends RuntimeException {

    public MissingQueryParamException(String queryParamName) {
        super(queryParamName);
    }

    @Override
    public String getMessage() {
        return String.format("The MANDATORY Query Parameter '%s' is missing", getMessage());
    }
}
