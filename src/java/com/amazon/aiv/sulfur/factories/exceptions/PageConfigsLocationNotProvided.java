package com.amazon.aiv.sulfur.factories.exceptions;

import com.amazon.aiv.sulfur.factories.PageConfigFactory;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class PageConfigsLocationNotProvided extends Exception {

    public PageConfigsLocationNotProvided() {
        super();
    }

    @Override
    public String getMessage() {
        return String.format("NOTE: It's MANDATORY to set the System Property '%s'.\n",
                PageConfigFactory.PAGE_CONFIGS_DIR_PATH);
    }
}
