package com.amazon.aiv.sulfur.factories.exceptions;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class SUnavailablePageException extends RuntimeException {

    public SUnavailablePageException(String pageName) {
        super(pageName);
    }

    @Override
    public String getMessage() {
        return String.format("The requested SPage is not currently available '%s'", super.getMessage());
    }
}
