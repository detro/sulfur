package com.amazon.aiv.sulfur.factories.exceptions;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class SInvalidPageConfigException extends RuntimeException {

    public SInvalidPageConfigException(String invalidPageConfigPath) {
        super(invalidPageConfigPath);
    }

    @Override
    public String getMessage() {
        return String.format("Malformed/Invalid Sulfur SPageConfig file '%s'\n" +
                "NOTE: Check file format at TODO.\n",
                super.getMessage());
    }
}
