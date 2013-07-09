package com.amazon.aiv.sulfur.factories.exceptions;

import com.amazon.aiv.sulfur.factories.SConsts;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class SInvalidPageConfigsLocationException extends RuntimeException {

    public SInvalidPageConfigsLocationException() {
        super();
    }

    @Override
    public String getMessage() {
        return String.format("NOTE: Value of System Property '%s' is invalid (non-existent/empty directory?).\n",
                SConsts.SYSPROP_PAGE_CONFIGS_DIR_PATH);
    }
}
