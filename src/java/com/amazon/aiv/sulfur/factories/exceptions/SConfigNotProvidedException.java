package com.amazon.aiv.sulfur.factories.exceptions;

import com.amazon.aiv.sulfur.factories.SConsts;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class SConfigNotProvidedException extends RuntimeException {

    public SConfigNotProvidedException() {
        super();
    }

    @Override
    public String getMessage() {
        return String.format("NOTE: It's MANDATORY to set the System Property '%s'.\n",
                SConsts.SYSPROP_CONFIG_FILE_PATH);
    }
}
