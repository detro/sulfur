package com.amazon.aiv.sulfur.factories.exceptions;

import com.amazon.aiv.sulfur.factories.PageConfigFactory;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class PageConfigInvalid extends RuntimeException {

    public PageConfigInvalid(String invalidPageConfigPath) {
        super(invalidPageConfigPath);
    }

    @Override
    public String getMessage() {
        return String.format("Malformed/Invalid PageConfig file '%s'\n" +
                "NOTE: Check file format at TODO.\n",
                getMessage());
    }
}
