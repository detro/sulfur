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

package sulfur;

import com.google.common.base.Function;
import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import sulfur.configs.SEnvConfig;
import sulfur.configs.SPageConfig;
import sulfur.factories.SWebDriverFactory;
import sulfur.factories.exceptions.SFailedToCreatePageComponentException;
import sulfur.factories.exceptions.SFailedToCreatePageException;
import sulfur.factories.exceptions.SUnavailableComponentException;
import sulfur.factories.exceptions.SUnavailableDriverException;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Ivan De Marino
 *
 * Sulfur Page.
 * This class is the central "piece of the puzzle".
 *
 * Sulfur Pages are collection of Sulfur Components, and contain a basic set of methods
 * to test the different parts of a page, looking at it as a composition of "widgets".
 *
 * The page contains also methods to control the loading flux of a page: to make sure HTML/JS has
 * been fully loaded, the internal status of all the composing Components can be considered while
 * "waiting for load".
 *
 * Also, the creation of a Page is based on the following input parameters:
 * <ul>
 *     <li>an Environment Configuration</li>
 *     <li>a Page Configuration</li>
 *     <li>a WebDriver name</li>
 *     <li>a map of URL Path Parameters</li>
 *     <li>a map of URL Query Parameters</li>
 * </ul>
 *
 * Those together contribute to the setting up of the Page, letting the client code focus on the testing aspects,
 * abstracting away "environmental parameters".
 */
public class SPage {
    private static final Logger LOG = Logger.getLogger(SPage.class);

    /** Default Timout time unit for SPage#waitForLoad  */
    public static final TimeUnit PAGELOAD_WAIT_DEFAULT_TIMEOUT_UNIT     = TimeUnit.SECONDS;
    /** Default Polling time for SPage#waitForLoad */
    public static final long PAGELOAD_WAIT_DEFAULT_POLLING_TIME         = 100;
    /** Default Polling time unit for SPage#waitForLoad  */
    public static final TimeUnit PAGELOAD_WAIT_DEFAULT_POLLING_UNIT     = TimeUnit.MILLISECONDS;

    private boolean mOpened;

    private WebDriver mDriver = null;
    private final String mInitialUrl;
    private final Map<String, SPageComponent> mPageComponents;
    private final SPageConfig mPageConfig;

    /**
     * Copy-constructor.
     *
     * @param page SPage to copy from. It will also copy the "opened" status.
     */
    public SPage(SPage page) {
        // Straight copy
        mDriver = page.getDriver();
        mPageComponents = page.mPageComponents;
        mPageConfig = page.mPageConfig;

        // If the page is already open, grab the "currentUrl" as "initialUrl" for this new page
        if (page.isOpen()) {
            mInitialUrl = page.getCurrentUrl();
            mOpened = true;
        } else {
            mInitialUrl = page.getInitialUrl();
            mOpened = false;
        }
    }

    /**
     * Construct a Page Object, already opened.
     * This is used when a Page interaction causes navigation to another page.
     *
     * NOTE: Calls to {@link SPage#open()} will have no effect.
     *
     * @param driver WebDriver intance, initialised and with the page already loaded.
     * @param config SPageConfig to use for this new SPage
     */
    public SPage(WebDriver driver, SPageConfig config) {
        mDriver = driver;
        mOpened = true;
        mInitialUrl = mDriver.getCurrentUrl();
        mPageConfig = config;
        mPageComponents = createPageComponentInstances(mPageConfig.getComponentClassnames(), this);
    }

    /**
     * Construct a Page Object, using a pre-existing one as "blueprint".
     * This is used when a Page interaction causes navigation to another page.
     * The original page will become "stale", as it will be deprived of it's original driver, to pass it to
     * the new page.
     *
     * NOTE: Calls to {@link SPage#open()} will have no effect.
     *
     * @param oldPage Page Object to start from
     * @param newConfig SPageConfig to use for this new Page
     */
    public SPage(SPage oldPage, SPageConfig newConfig) {
        mDriver = oldPage.getDriver();
        mOpened = oldPage.isOpen();
        mInitialUrl = oldPage.getCurrentUrl();
        mPageConfig = newConfig;
        mPageComponents = createPageComponentInstances(mPageConfig.getComponentClassnames(), this);

        oldPage.mDriver = null;         //< NOTE: Takes ownership of the Driver from the "old page"
    }

    /**
     * Construct a Page Object, not yet opened.
     * It validates the input parameters, checking if the requested driver exists, if the page exists and the
     * mandatory path and query parameters are all provided.
     *
     * NOTE: The returned SPage hasn't loaded yet, so the User can still operate on it before the initial HTTP GET.
     *
     * @param envConfig Sulfur Environment Configuration
     * @param driverName Desired WebDriver Name 
     * @param pageConfig Sulfur Page Configuration
     * @param pathParams Map of parameters that will be set in the SPage URL Path (@see SPageConfig)
     * @param queryParams Map of parameters that will be set in the SPage URL Query (@see SPageConfig)
     */
    public SPage(SEnvConfig envConfig,
                 String driverName,
                 SPageConfig pageConfig,
                 Map<String, String> pathParams,
                 Map<String, String> queryParams) {
        // A bit of input validation
        if (null == envConfig) {
            throw new RuntimeException("Missing/NULL parameter 'envConfig'");
        }
        if (null == pageConfig) {
            throw new RuntimeException("Missing/NULL parameter 'pageConfig'");
        }

        // New Page, not yet "opened"
        mOpened = false;

        // Validate Driver Name
        if (!envConfig.getDrivers().contains(driverName)) {
            LOG.fatal(String.format("FAILED to find WedDriver '%s'", driverName));
            throw new SUnavailableDriverException(driverName);
        }

        // Compose URL Path & Query to the SPage
        String urlPath = pageConfig.composeUrlPath(pathParams);
        String urlQuery = pageConfig.composeUrlQuery(queryParams);

        // Create the destination URL
        try {
            mInitialUrl = new URL(
                    envConfig.getProtocol(),            //< protocol
                    envConfig.getHost(),                //< host
                    envConfig.getPort(),                //< port
                    urlPath + urlQuery).toString();     //< path + query
        } catch (MalformedURLException mue) {
            LOG.fatal(String.format("FAILED to compose the URL to the Page '%s'", pageConfig.getName()), mue);
            throw new SFailedToCreatePageException(mue);
        }

        // Store the Page Configuration
        mPageConfig = pageConfig;

        // Create the requested driver
        mDriver = new SWebDriverFactory(envConfig).createDriver(driverName);

        try {
            // Create Page Components based on the configuration
            mPageComponents = createPageComponentInstances(pageConfig.getComponentClassnames(), this);
        } catch(Exception e) {
            // Case something goes wrong, we must make sure the WebDriver is quit
            LOG.fatal(String.format("FAILED to Create Components for the Page '%s'", pageConfig.getName()), e);
            mDriver.quit();
            throw e;
        }
    }

    /**
     * Adds a Cookie to a Page.
     * Usually this will be used when {@link sulfur.SPage#isOpen()} is still false.
     *
     * @param cookie Cookie to set on the Page
     */
    public void addCookie(Cookie cookie) {
        mDriver.manage().addCookie(cookie);
    }

    /**
     * Open the Page.
     * It emulates the action of the User.
     * NOTE: An Opened page can't be reopened.
     *
     * @return The same SPage object: useful for call chains
     */
    public SPage open() {
        if (!mOpened) {
            LOG.debug("SPage opening: " + mInitialUrl);

            mDriver.get(mInitialUrl);
        }
        mOpened = true;

        return this;
    }

    /**
     * Disposes of a Page.
     * After this, the Page object and the internal Driver become unusable (i.e. WebDriver is quitted).
     *
     * IMPORTANT: If multiple SPage objects use the same WebDriver (i.e. created using
     * <code>SPage(WebDriver, SPageConfig)</code> constructor), quitting it will cause unpredictable failures.
     */
    public void dispose() {
        if (null != mDriver) {
            mDriver.quit();
            mDriver = null;
        }
    }

    /**
     * @return Returns "false" before the SPage#open() method has been invoked.
     */
    public boolean isOpen() {
        return mOpened;
    }

    /**
     * Returns "true" if the SPage has loaded, "false" otherwise.
     * To check for loaded status, it queries all the MANDATORY SPageComponent inside: if all have loaded, the page has loaded.
     *
     * @return "true" if/when all SPageComponent inside the SPage have loaded.
     */
    public boolean isLoaded() {
        for (Map.Entry<String, SPageComponent> component : mPageComponents.entrySet()) {
            // IFF Component is Mandatory AND has not loaded yet, return false
            if (mPageConfig.getMandatoryComponentClassnames().contains(component.getValue().getClass().getCanonicalName())
                    && !component.getValue().isLoaded()) {
                LOG.warn(String.format("SPageComponent '%s' in SPage '%s' has not loaded (yet?)",
                        component.getValue().getName(),
                        getName()));

                return false;
            }
        }

        LOG.debug(String.format("SPage '%s' has loaded", getName()));

        return true;
    }

    /**
     * Refer to {@link SPage#waitForLoad(long, java.util.concurrent.TimeUnit, long, java.util.concurrent.TimeUnit)}
     * @param timeout Time before giving up
     */
    public void waitForLoad(long timeout) {
        waitForLoad(timeout,
                PAGELOAD_WAIT_DEFAULT_TIMEOUT_UNIT,
                PAGELOAD_WAIT_DEFAULT_POLLING_TIME,
                PAGELOAD_WAIT_DEFAULT_POLLING_UNIT);
    }

    /**
     * Refer to {@link SPage#waitForLoad(long, java.util.concurrent.TimeUnit, long, java.util.concurrent.TimeUnit)}
     * @param timeout Time before giving up
     * @param unit Time Unit
     */
    public void waitForLoad(long timeout, TimeUnit unit) {
        waitForLoad(timeout, unit,
                PAGELOAD_WAIT_DEFAULT_POLLING_TIME,
                PAGELOAD_WAIT_DEFAULT_POLLING_UNIT);
    }

    /**
     * Wait for SPage to Load.
     * It will wait for all the internal SPageComponent to finish loading
     *
     * @param timeout Time before giving up the waiting
     * @param timeoutUnit Time Unit used by timeout parameter
     * @param polling Time interval between checking if SPage has loaded
     * @param pollingUnit Time Unit used by polling parameter
     */
    public void waitForLoad(long timeout, TimeUnit timeoutUnit, long polling, TimeUnit pollingUnit) {
        Wait<SPage> waiter = new FluentWait<SPage>(this)
                .pollingEvery(polling, pollingUnit)
                .withTimeout(timeout, timeoutUnit);

        waiter.until(new Function<SPage, Boolean>() {
            public Boolean apply(SPage page) {
                LOG.debug(String.format("Waiting for SPage '%s' to load", getName()));
                return page.isLoaded();
            }
        });
    }

    /**
     * @return Name of the Page, based on the SPageConfig
     */
    public String getName() {
        return mPageConfig.getName();
    }

    /**
     * @return Current Page title.
     */
    public String getTitle() {
        return getDriver().getTitle();
    }

    /**
     * URL used at time the SPage object was constructed.
     * @return URL used at time the SPage object was constructed.
     */
    public String getInitialUrl() {
        return mInitialUrl;
    }

    /**
     * URL the SPage's internal Driver is at.
     * @return URL the SPage's internal Driver is at.
     */
    public String getCurrentUrl() {
        return isOpen() ? getDriver().getCurrentUrl() : getInitialUrl();
    }

    /**
     * @return Current Page source code (i.e. the HTML)
     */
    public String getSource() {
        return getDriver().getPageSource();
    }

    /**
     * Get SPageComponent from the SPage
     *
     * @param componentName Name of the Component we are interested in
     * @return The SPageComponent we are after
     */
    public SPageComponent getComponent(String componentName) {
        SPageComponent component = mPageComponents.get(componentName);
        if (null == component) {
            throw new SUnavailableComponentException(componentName);
        }
        return mPageComponents.get(componentName);
    }

    /**
     * @return A Map<String, SPageComponent> of all the Components that compose the Page (note: based on the Page Configuration).
     */
    public Map<String, SPageComponent> getComponents() {
        return mPageComponents;
    }

    /**
     * Create a map of SPageComponents
     *
     * @param componentClassnames Classnames to use when creating a SPageComponent instance
     * @param containingPage The SPage object that will contain those Components once created
     */
    private static Map<String, SPageComponent> createPageComponentInstances(Set<String> componentClassnames, SPage containingPage) {
        Map<String, SPageComponent> pageComponents = new HashMap<String, SPageComponent>(componentClassnames.size());

        for (String componentClassname : componentClassnames) {
            SPageComponent newPageComponent = createPageComponentInstance(componentClassname, containingPage);
            pageComponents.put(newPageComponent.getName(), newPageComponent);
        }

        return pageComponents;
    }

    /**
     * Create instance of given SPageComponent.
     *
     * NOTE: This delegates the Selenium very own {@link org.openqa.selenium.support.PageFactory}
     * to actually populate the WebElement declared in a SPageComponent.
     *
     * @param componentClassname Classname of the SPageComponent to create
     * @param containingPage SPage that will contain the Component
     * @return Instance of the SPageComponent
     * @throws sulfur.factories.exceptions.SFailedToCreatePageComponentException
     */
     private static SPageComponent createPageComponentInstance(String componentClassname, SPage containingPage) {
        try {
            // Grab class and constructor
            Class<?> componentClass = Class.forName(componentClassname);
            Constructor<?> componentConstructor = componentClass.getConstructor(SPage.class);

            // Create instance of the SPageComponent
            SPageComponent componentObj = (SPageComponent) componentConstructor.newInstance(containingPage);

            // Initialise the decorated WebElement in the Component, delegating the job to the Selenium {@link PageFactory}
            PageFactory.initElements(containingPage.getDriver(), componentObj);

            return componentObj;
        } catch (Exception e) {
            throw new SFailedToCreatePageComponentException(e);
        }
    }

    /**
     * Returns the Driver used by this SPage.
     * The assumption is that you know what a WebDriver is an the effect that you will have manipulating
     * it directly.
     *
     * It's advisable you use SPageComponent(s) instead.
     * @return The WebDriver in use to this SPage Object
     */
    public WebDriver getDriver() {
        return mDriver;
    }

    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) mDriver).executeScript(script, args);
    }

    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) mDriver).executeAsyncScript(script, args);
    }

    @Override
    public void finalize() {
        dispose();
    }
}
