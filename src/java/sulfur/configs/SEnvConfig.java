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

import org.apache.log4j.Logger;
import sulfur.factories.SEnvConfigFactory;

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
    // JSON
    private String                  name = null;
    private String                  protocol = null;
    private String                  host = null;
    private int                     port = 80;
    private LinkedHashSet<String>   drivers = null; //< makes "drivers" lookup fast
    private String                  seleniumhub = null;

    // Derived
    private transient String        filename = null;

    public String getName() {
        return null == name ? filename.replace(SEnvConfigFactory.EXTENSION_ENV_CONFIG_FILE, "") : name;
    }

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void logDebug(Logger logger) {
        logger.debug("  filename: " + getFilename());
        logger.debug("  name: " + getName());
        logger.debug("  protocol: " + getProtocol());
        logger.debug("  host: " + getHost());
        logger.debug("  port: " + getPort());
        logger.debug("  driver: " + getDrivers());
        logger.debug("  seleniumhub: " + getSeleniumHub());
    }

    @Override
    public String toString() {
        return String.format("Env '%s' ('%s')", getName(), getFilename());
    }
}
