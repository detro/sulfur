package com.amazon.aiv.sulfur;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * This is a POJO, intended to be used with Google GSON.
 *
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * TODO
 */
public class PageConfig {
    private String      name = null;
    private String[]    components = null;
    private String      path = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getComponents() {
        return components;
    }

    public String getPath() {
        return path;
    }

    public void logDebug(Logger logger) {
        logger.debug("name: " + getName());
        logger.debug("components: " + Arrays.toString(components));
        logger.debug("path: " + getPath());
    }
}
