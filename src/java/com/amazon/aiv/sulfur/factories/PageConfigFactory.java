package com.amazon.aiv.sulfur.factories;

import com.amazon.aiv.sulfur.factories.exceptions.InvalidPageConfigException;
import com.amazon.aiv.sulfur.factories.exceptions.InvalidPageConfigsLocationException;
import com.amazon.aiv.sulfur.factories.exceptions.PageConfigsLocationNotProvidedException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * #factory
 * #singleton
 *
 * TODO
 */
public class PageConfigFactory {
    private static final Logger LOG = Logger.getLogger(PageConfigFactory.class);

    private static PageConfigFactory singleton = null;

    /** Holds a reference to the Filename of PageConfigs that have been found */
    private Map<String, PageConfig> mPageConfigs = new HashMap <String, PageConfig>();

    /**
     * Factory method
     *
     * @return Singleton PageConfigFactory
     * @throws com.amazon.aiv.sulfur.factories.exceptions.PageConfigsLocationNotProvidedException
     */
    public synchronized static PageConfigFactory getInstance() {

        if (null == singleton) {
            singleton = new PageConfigFactory();
        }

        return singleton;
    }

    /**
     * Utility method to get rid of the PageConfigFactory Singleton Instance.
     * NOTE: Make sure you know what you are doing when using this.
     */
    public synchronized static void clearInstance() {
        singleton = null;
    }

    /**
     * Creates a PageConfigFactory, checking the given PageConfigFactory#PAGE_CONFIGS_DIR_PATH.
     *
     * @throws com.amazon.aiv.sulfur.factories.exceptions.PageConfigsLocationNotProvidedException
     */
    private PageConfigFactory() {
        // Read path to PageConfig Files directory
        String pageConfigsDirPath = System.getProperties().getProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH);
        if (null == pageConfigsDirPath) {           //< check validity
            throw new PageConfigsLocationNotProvidedException();
        }

        // Scan the given directory for files with extension EXTENSION_PAGE_CONFIG_FILE
        File pageConfigsDir = new File(pageConfigsDirPath);
        File[] pageConfigsFiles = pageConfigsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File fileDir, String fileName) {
                return fileName.endsWith(Consts.EXTENSION_PAGE_CONFIG_FILE);
            }
        });
        if (null == pageConfigsFiles || pageConfigsFiles.length == 0) {             //< check validity
            throw new InvalidPageConfigsLocationException();
        }

        Gson gson = new Gson();
        for (int i = pageConfigsFiles.length -1; i >= 0; --i) {
            LOG.debug("FOUND PageConfig file: " + pageConfigsFiles[i].getPath());

            try {
                FileReader fReader = new FileReader(pageConfigsFiles[i]);
                PageConfig pc = gson.fromJson(fReader, PageConfig.class);

                // Set the "filename" on the PageConfig
                pc.setFilename(pageConfigsFiles[i].getName());

                // Ask the PageConfig to log itself
                pc.logDebug(LOG);

                // Store the new PageConfig
                mPageConfigs.put(pc.getName(), pc);
            } catch (FileNotFoundException fnfe) {
                LOG.error("INVALID PageConfig (not found)");
                throw new InvalidPageConfigException(pageConfigsFiles[i].getPath());
            } catch (JsonSyntaxException jse) {
                LOG.error("INVALID PageConfig (malformed)");
                throw jse;
            }
        }
    }

    /**
     * Return a PageConfig if found, null otherwise.
     *
     * @param name Name of the required PageConfig
     * @return A PageConfig or null
     */
    public PageConfig getPageConfig(String name) {
        return mPageConfigs.get(name);
    }

    /**
     * Return all the PageConfig the factory could find
     *
     * @return All the PageConfig objects in a map (key is the page name)
     */
    public Map<String, PageConfig> getPageConfigs() {
        return mPageConfigs;
    }
}
