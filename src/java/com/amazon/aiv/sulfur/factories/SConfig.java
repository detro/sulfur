package com.amazon.aiv.sulfur.factories;

import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

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
public class SConfig {
    // JSON
    private String                  protocol = null;
    private String                  host = null;
    private int                     port = 80;
    private LinkedHashSet<String>   drivers = null; //< makes "drivers" lookup fast
    private String                  seleniumhub = null;

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Set<String> getDrivers() {
        return drivers;
    }

    /**
     * Returns the URL to the Selenium HUB to use for our tests.
     *
     * @return URL to Selenium HUB to use, or "null" in case parameter not configured or malformed
     */
    public URL getSeleniumHub() {
        if (null != seleniumhub) {
            try {
                return new URL(seleniumhub);
            } catch(MalformedURLException mue) {
                // swallow the exception: we fall back to "null" in case the URL is malformed
            }
        }
        return null;
    }

    public void logDebug(Logger logger) {
        logger.debug("  protocol: " + getProtocol());
        logger.debug("  host: " + getHost());
        logger.debug("  port: " + getPort());
        logger.debug("  driver: " + getDrivers());
        logger.debug("  seleniumhub: " + getSeleniumHub());
    }
}
