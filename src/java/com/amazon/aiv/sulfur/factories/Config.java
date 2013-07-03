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
    private String      host = null;
    private int         port = 80;
    private String[]    drivers = null;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String[] getDrivers() {
        return drivers;
    }

    public void logDebug(Logger logger) {
        logger.debug("host: " + getHost());
        logger.debug("port: " + getPort());
        logger.debug("drivers: " + Arrays.toString(getDrivers()));
    }
}
