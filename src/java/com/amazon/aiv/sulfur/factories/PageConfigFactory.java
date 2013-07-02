package com.amazon.aiv.sulfur.factories;

import com.amazon.aiv.sulfur.PageConfig;
import com.amazon.aiv.sulfur.factories.exceptions.PageConfigInvalid;
import com.amazon.aiv.sulfur.factories.exceptions.PageConfigsLocationInvalid;
import com.amazon.aiv.sulfur.factories.exceptions.PageConfigsLocationNotProvided;
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

    /** MANDATORY System Property to instruct Sulfur where to look for Page Configs files */
    public static final String PAGE_CONFIGS_DIR_PATH    = "sulfur.page.configs";
    /** MANDATORY Extension that Page Config files have to use */
    public static final String PAGE_CONFIG_FILE_EXT     = ".pageconfig.json";

    /**
     * Factory method
     *
     * @return Singleton PageConfigFactory
     * @throws PageConfigsLocationNotProvided
     */
    public static PageConfigFactory  getInstance() {

        if (null == singleton) {
            singleton = new PageConfigFactory();
        }

        return singleton;
    }

    /**
     * Creates a PageConfigFactory, checking the given PageConfigFactory#PAGE_CONFIGS_DIR_PATH.
     *
     * @throws PageConfigsLocationNotProvided
     */
    private PageConfigFactory() {

        String pageConfigsDirPath = System.getProperties().getProperty(PAGE_CONFIGS_DIR_PATH);

        // PAGE_CONFIGS_DIR_PATH must be provided, otherwise we throw an exception
        if (null == pageConfigsDirPath) {
            throw new PageConfigsLocationNotProvided();
        }

        // Scan the given directory for files with extension PAGE_CONFIG_FILE_EXT
        File pageConfigsDir = new File(pageConfigsDirPath);
        File[] pageConfigsFiles = pageConfigsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File fileDir, String fileName) {
                return fileName.endsWith(PAGE_CONFIG_FILE_EXT);
            }
        });

        // PAGE_CONFIGS_DIR_PATH must point to a valid directory
        if (null == pageConfigsFiles) {
            throw new PageConfigsLocationInvalid();
        }

        Gson gson = new Gson();
        for (int i = pageConfigsFiles.length -1; i >= 0; --i) {
            LOG.debug("FOUND PageConfig file: " + pageConfigsFiles[i].getPath());

            // By default, the PageConfig "name" is derived by the filename
            String defaultPageName = pageConfigsFiles[i].getName().replace(PAGE_CONFIG_FILE_EXT, "");

            try {
                FileReader fReader = new FileReader(pageConfigsFiles[i]);
                PageConfig pc = gson.fromJson(fReader, PageConfig.class);

                // Normalize PageConfig "name"
                if (null == pc.getName()) {
                    pc.setName(defaultPageName);
                }

                // Ask the PageConfig to log itself
                pc.logDebug(LOG);

                // Store the new PageConfig
                mPageConfigs.put(pc.getName(), pc);
            } catch (FileNotFoundException fnfe) {
                LOG.error("INVALID PageConfig (not found)");
                throw new PageConfigInvalid(pageConfigsFiles[i].getPath());
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
