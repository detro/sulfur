package com.amazon.aiv.sulfur.factories;

import com.amazon.aiv.sulfur.factories.exceptions.PageConfigsLocationInvalid;
import com.amazon.aiv.sulfur.factories.exceptions.PageConfigsLocationNotProvided;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;

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

    public static final String PAGE_CONFIGS_DIR_PATH    = "sulfur.page.configs";
    public static final String PAGE_CONFIG_FILE_EXT     = ".pageconfig.json";

    /**
     * Factory method
     *
     * @return Singleton PageConfigFactory
     * @throws PageConfigsLocationNotProvided
     */
    public static PageConfigFactory  getInstance()
            throws PageConfigsLocationNotProvided, PageConfigsLocationInvalid {

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
    private PageConfigFactory()
            throws PageConfigsLocationNotProvided, PageConfigsLocationInvalid {

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

        for (int i = pageConfigsFiles.length -1; i >= 0; --i) {
            LOG.debug(pageConfigsFiles[i].getPath());
        }
    }
}
