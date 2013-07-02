package com.amazon.aiv.sulfur;

import com.amazon.aiv.sulfur.factories.PageConfigFactory;
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
    private String              name = null;
    private String[]            components = null;
    private String              path = null;

    private transient String    filename = null;

    public String getName() {
        return null == name ? filename.replace(PageConfigFactory.PAGE_CONFIG_FILE_EXT, "") : name;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void logDebug(Logger logger) {
        logger.debug("name: " + getName());
        logger.debug("components: " + Arrays.toString(components));
        logger.debug("path: " + getPath());
    }
}
