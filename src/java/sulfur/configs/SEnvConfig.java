/*
This file is part of the Sulfur project by Ivan De Marino (http://ivandemarino.me).

Copyright (c) 2013, Ivan De Marino (http://ivandemarino.me)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package sulfur.configs;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;
import sulfur.factories.exceptions.SConfigNotProvidedException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is a POJO, intended to be used with Google GSON.
 *
 * @author Ivan De Marino
 *
 * #pojo
 * #json
 * #gson
 * #struct
 * #config
 *
 * TODO
 */
public class SEnvConfig {
    private static final Logger LOG = Logger.getLogger(SEnvConfig.class);

    /** MANDATORY System Property to instruct Sulfur where to look for the Config file */
    public static final String SYSPROP_CONFIG_FILE_PATH = "sulfur.config";

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

    /**
     * Utility method to read the Sulfur Config from Filesystem, based on the System Property SYSPROP_CONFIG_FILE_PATH.
     * It's essentially a Factory Method.
     *
     * @return the SEnvConfig object
     * @throws FileNotFoundException
     * @throws JsonSyntaxException
     */
    public static SEnvConfig getConfig() throws FileNotFoundException, JsonSyntaxException {
        // Read configuration file location
        String configFilePath = System.getProperty(SYSPROP_CONFIG_FILE_PATH);
        if (null == configFilePath) {
            throw new SConfigNotProvidedException();
        }

        // Parse configuration file
        Gson gson = new Gson();
        FileReader configFileReader = new FileReader(configFilePath);
        SEnvConfig newSulfurConfig = gson.fromJson(configFileReader, SEnvConfig.class);

        LOG.debug("FOUND Sulfur Config file: " + configFilePath);
        return newSulfurConfig;
    }
}
