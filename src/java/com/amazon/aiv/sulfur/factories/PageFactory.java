package com.amazon.aiv.sulfur.factories;

import com.amazon.aiv.sulfur.Page;
import com.amazon.aiv.sulfur.factories.exceptions.ConfigInvalid;
import com.amazon.aiv.sulfur.factories.exceptions.ConfigNotProvided;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * #factory
 * #singleton
 *
 * TODO
 */
public class PageFactory {
    private static final Logger LOG = Logger.getLogger(PageFactory.class);

    private final Config mConfig;
    private final PageConfigFactory mPageConfigFactory;

    private static PageFactory singleton = null;

    private PageFactory() {
        // Read configuration file location
        String configFilePath = System.getProperty(Consts.SYSPROP_CONFIG_FILE_PATH);
        if (null == configFilePath) {
            throw new ConfigNotProvided();
        }

        // Parse configuration file
        Gson gson = new Gson();
        try {
            FileReader configFileReader = new FileReader(configFilePath);
            mConfig = gson.fromJson(configFileReader, Config.class);
            mConfig.logDebug(LOG);
        } catch (FileNotFoundException fnfe) {
            LOG.error("INVALID Config (not found)");
            throw new ConfigInvalid(configFilePath);
        } catch (JsonSyntaxException jse) {
            LOG.error("INVALID Config (malformed)");
            throw jse;
        }

        // Fetch a PageConfigFactory
        mPageConfigFactory = PageConfigFactory.getInstance();
    }

    /**
     * Factory Method
     *
     * @return The PageFactory
     */
    public synchronized static PageFactory getInstance() {
        if (null == singleton) {
            singleton = new PageFactory();
        }

        return singleton;
    }

    /**
     * Creates a Page.
     * @param driverName
     * @param pageName
     * @param pathParams
     * @param queryParams
     * @return
     */
    public Page createPage(String driverName, String pageName,
                           Map<String, String> pathParams,
                           Map<String, String> queryParams) {
        // TODO
        // 1. validate driver name
        // 2. validate page name
        // 3. validate path params
        // 4. validate query params
        // IFF all the above are valid, CREATE the PAGE
        return null;
    }

    public Map<String, PageConfig> availablePageConfigs() {
        return mPageConfigFactory.getPageConfigs();
    }

    /**
     * @return The Sulfur Configuration currently used by the PageFactory
     */
    public Config getConfig() {
        return mConfig;
    }
}
