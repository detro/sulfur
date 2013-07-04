package com.amazon.aiv.sulfur.factories.exceptions;

import com.amazon.aiv.sulfur.factories.Consts;
import com.amazon.aiv.sulfur.factories.PageConfigFactory;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class InvalidPageConfigsLocationException extends RuntimeException {

    public InvalidPageConfigsLocationException() {
        super();
    }

    @Override
    public String getMessage() {
        return String.format("NOTE: Value of System Property '%s' is invalid (non-existent/empty directory?).\n",
                Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH);
    }
}
