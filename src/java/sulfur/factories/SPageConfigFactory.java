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

package sulfur.factories;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;
import sulfur.configs.SPageConfig;
import sulfur.factories.exceptions.SInvalidPageConfigException;
import sulfur.factories.exceptions.SInvalidPageConfigsLocationException;
import sulfur.factories.exceptions.SPageConfigsLocationNotProvidedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ivan De Marino
 *
 * #factory
 * #singleton
 *
 * TODO
 */
public class SPageConfigFactory {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(SPageConfigFactory.class);

    /** MANDATORY System Property to instruct Sulfur where to look for SPage Configs files */
    public static final String SYSPROP_PAGE_CONFIGS_DIR_PATH    = "sulfur.page.configs";
    /** MANDATORY Extension that SPage Config files have to use */
    public static final String EXTENSION_PAGE_CONFIG_FILE = ".sulfur.page.json";

    private static SPageConfigFactory singleton = null;

    /** Holds a reference to the Filename of PageConfigs that have been found */
    private Map<String, SPageConfig> mPageConfigs = new HashMap <String, SPageConfig>();

    /**
     * Factory method
     *
     * @return Singleton SPageConfigFactory
     * @throws sulfur.factories.exceptions.SPageConfigsLocationNotProvidedException
     */
    public synchronized static SPageConfigFactory getInstance() {
        if (null == singleton) {
            singleton = new SPageConfigFactory();
        }

        return singleton;
    }

    /**
     * Utility method to get rid of the SPageConfigFactory Singleton Instance.
     * NOTE: Make sure you know what you are doing when using this.
     */
    public synchronized static void clearInstance() {
        singleton = null;
    }

    /**
     * Creates a SPageConfigFactory, checking the given SPageConfigFactory#SYSPROP_PAGE_CONFIGS_DIR_PATH.
     *
     * @throws SPageConfigsLocationNotProvidedException
     * @throws SInvalidPageConfigsLocationException
     * @throws SInvalidPageConfigException
     * @throws JsonSyntaxException
     */
    private SPageConfigFactory() {
        // Read path to SPageConfig Files directory
        String pageConfigsDirPath = System.getProperties().getProperty(SYSPROP_PAGE_CONFIGS_DIR_PATH);
        if (null == pageConfigsDirPath) {           //< check validity
            throw new SPageConfigsLocationNotProvidedException();
        }

        // Scan the given directory for files with extension EXTENSION_PAGE_CONFIG_FILE
        File pageConfigsDir = new File(pageConfigsDirPath);
        File[] pageConfigsFiles = pageConfigsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File fileDir, String fileName) {
                return fileName.endsWith(EXTENSION_PAGE_CONFIG_FILE);
            }
        });
        if (null == pageConfigsFiles || pageConfigsFiles.length == 0) {             //< check validity
            throw new SInvalidPageConfigsLocationException();
        }

        Gson gson = new Gson();
        for (int i = pageConfigsFiles.length -1; i >= 0; --i) {
            LOG.debug("FOUND SPageConfig file: " + pageConfigsFiles[i].getPath());

            try {
                FileReader fReader = new FileReader(pageConfigsFiles[i]);
                SPageConfig pc = gson.fromJson(fReader, SPageConfig.class);

                // Set the "filename" on the SPageConfig
                pc.setFilename(pageConfigsFiles[i].getName());

                // Ask the SPageConfig to log itself
                pc.logDebug(LOG);

                // Store the new SPageConfig
                mPageConfigs.put(pc.getName(), pc);
            } catch (FileNotFoundException fnfe) {
                LOG.error("INVALID SPageConfig (not found)");
                throw new SInvalidPageConfigException(pageConfigsFiles[i].getPath());
            } catch (JsonSyntaxException jse) {
                LOG.error("INVALID SPageConfig (malformed)");
                throw jse;
            }
        }
    }

    /**
     * Return a SPageConfig if found, null otherwise.
     *
     * @param name Name of the required SPageConfig
     * @return A SPageConfig or null
     */
    public SPageConfig getPageConfig(String name) {
        return mPageConfigs.get(name);
    }

    /**
     * Return all the SPageConfig the factory could find
     *
     * @return All the SPageConfig objects in a map (key is the page name)
     */
    public Map<String, SPageConfig> getPageConfigs() {
        return mPageConfigs;
    }
}
