package com.amazon.aiv.sulfur.factories.exceptions;

import com.amazon.aiv.sulfur.factories.Consts;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class FailedToCreatePageException extends RuntimeException {

    public FailedToCreatePageException(Throwable throwable) {
        super(throwable);
    }
}
