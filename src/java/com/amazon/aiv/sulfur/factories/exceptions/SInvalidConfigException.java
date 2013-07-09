package com.amazon.aiv.sulfur.factories.exceptions;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class SInvalidConfigException extends RuntimeException {

    public SInvalidConfigException(String invalidPageConfigPath) {
        super(invalidPageConfigPath);
    }

    public SInvalidConfigException(String invalidPageConfigPath, Throwable cause) {
        super(invalidPageConfigPath, cause);
    }

    @Override
    public String getMessage() {
        return String.format("Malformed/Invalid Sulfur Config file '%s'\n" +
                "NOTE: Check file format at TODO.\n",
                super.getMessage());
    }
}
