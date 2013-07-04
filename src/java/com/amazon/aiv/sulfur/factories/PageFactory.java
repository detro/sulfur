package com.amazon.aiv.sulfur.factories;

import com.amazon.aiv.sulfur.Page;
import com.amazon.aiv.sulfur.factories.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

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
            throw new ConfigNotProvidedException();
        }

        // Parse configuration file
        Gson gson = new Gson();
        try {
            FileReader configFileReader = new FileReader(configFilePath);
            mConfig = gson.fromJson(configFileReader, Config.class);

            // Logging
            LOG.debug("FOUND Sulfur Config file: " + configFilePath);
            mConfig.logDebug(LOG);
        } catch (FileNotFoundException fnfe) {
            LOG.error("INVALID Config (not found)");
            throw new InvalidConfigException(configFilePath);
        } catch (JsonSyntaxException jse) {
            LOG.error("INVALID Config (malformed)");
            throw new InvalidConfigException(configFilePath, jse);
        }

        // Fetch a PageConfigFactory
        mPageConfigFactory = PageConfigFactory.getInstance();

        LOG.debug("Available Pages: " + getAvailablePageConfigs());
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
     * Utility method to get rid of the PageFactory Singleton Instance.
     * NOTE: Make sure you know what you are doing when using this.
     */
    public synchronized static void clearInstance() {
        singleton = null;
    }

    /**
     * Creates a Page.
     * It validates the input parameters, checking if the requested driver exists, if the page exists and the
     * mandatory path and query parameters are all provided.
     *
     * NOTE: The returned Page hasn't loaded yet, so the User can still operate on it before the initial HTTP GET.
     *
     * @param driverName Possible values are listed in @see Consts interface
     * @param pageName Name of the Page we want to open. It must be part of the given PageConfig(s)
     * @param pathParams Map of parameters that will be set in the Page URL Path (@see PageConfig)
     * @param queryParams Map of parameters that will be set in the Page URL Query (@see PageConfig)
     * @return A "ready to open" Page object
     */
    public Page createPage(String driverName,
                           String pageName,
                           Map<String, String> pathParams,
                           Map<String, String> queryParams) {

        // Validate Driver Name
        if (!getConfig().getDrivers().contains(driverName)) {
            throw new UnavailableDriverException(driverName);
        }
        // Validate Page Name
        if (!getAvailablePageConfigs().contains(pageName)) {
            throw new UnavailablePageException(pageName);
        }

        // Fetch required PageConfig
        PageConfig pageConfig = mPageConfigFactory.getPageConfig(pageName);

        // Compose URL Path & Query to the Page
        String urlPath = pageConfig.composeUrlPath(pathParams);
        String urlQuery = pageConfig.composeUrlQuery(queryParams);

        // Create the requested driver
        WebDriver driver = WebDriverFactory.createDriver(driverName);

        // Create the destination URL
        String url;
        try {
            url = new URL(mConfig.getProtocol(), mConfig.getHost(), mConfig.getPort(), urlPath + "?" + urlQuery).toString();
        } catch (MalformedURLException mue) {
            LOG.fatal(String.format("FAILED to compose the URL to the Page '%s'", pageName), mue);
            throw new FailedToCreatePageException(mue);
        }

        return new Page(driver, url);
    }

    public Set<String> getAvailablePageConfigs() {
        return mPageConfigFactory.getPageConfigs().keySet();
    }

    /**
     * @return The Sulfur Configuration currently used by the PageFactory
     */
    public Config getConfig() {
        return mConfig;
    }
}
