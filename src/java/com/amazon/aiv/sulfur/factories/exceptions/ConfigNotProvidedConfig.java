package com.amazon.aiv.sulfur.factories.exceptions;

import com.amazon.aiv.sulfur.factories.Consts;
import com.amazon.aiv.sulfur.factories.PageConfigFactory;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class ConfigNotProvidedConfig extends RuntimeException {

    public ConfigNotProvidedConfig() {
        super();
    }

    @Override
    public String getMessage() {
        return String.format("NOTE: It's MANDATORY to set the System Property '%s'.\n",
                Consts.SYSPROP_CONFIG_FILE_PATH);
    }
}
