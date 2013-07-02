package com.amazon.aiv.sulfur.factories;

import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * This is a POJO, intended to be used with Google GSON.
 *
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * #pojo
 * #json
 * #gson
 * #struct
 * #config
 *
 * TODO
 */
public class Config {
    private String              host = null;
    private int                 port = 80;
    private String[]            browsers = null;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String[] getBrowsers() {
        return browsers;
    }

    public void logDebug(Logger logger) {
        logger.debug("host: " + getHost());
        logger.debug("port: " + getPort());
        logger.debug("browsers: " + Arrays.toString(getBrowsers()));
    }
}
