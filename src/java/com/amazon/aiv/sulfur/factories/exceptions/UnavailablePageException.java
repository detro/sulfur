package com.amazon.aiv.sulfur.factories.exceptions;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class UnavailablePageException extends RuntimeException {

    public UnavailablePageException(String pageName) {
        super(pageName);
    }

    @Override
    public String getMessage() {
        return String.format("The requested Page is not currently available '%s'", getMessage());
    }
}
