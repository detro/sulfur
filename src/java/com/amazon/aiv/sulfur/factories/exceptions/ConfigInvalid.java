package com.amazon.aiv.sulfur.factories.exceptions;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class ConfigInvalid extends RuntimeException {

    public ConfigInvalid(String invalidPageConfigPath) {
        super(invalidPageConfigPath);
    }

    @Override
    public String getMessage() {
        return String.format("Malformed/Invalid Sulfur Config file '%s'\n" +
                "NOTE: Check file format at TODO.\n",
                getMessage());
    }
}
