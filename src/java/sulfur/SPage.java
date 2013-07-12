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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import sulfur.factories.SPageComponentFactory;
import sulfur.factories.SPageConfig;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Ivan De Marino
 *
 * TODO
 */
public class SPage {
    private static final Logger LOG = Logger.getLogger(SPage.class);

    /** Default Polling for waitForLoad */
    private static final long DEFAULT_PAGELOAD_WAIT_POLLING = 10;
    /** Default Time Unit for waitForLoad */
    private static final TimeUnit DEFAULT_PAGELOAD_WAIT_UNIT = TimeUnit.SECONDS;

    private boolean mOpened;

    private final WebDriver mDriver;
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
     * This is used to when a Page interaction causes navigation to another page.
     *
     * NOTE: Calls to SPage#open() will have no effect.
     *
     * @param driver WebDriver intance, initialised and with the page already loaded.
     * @param config SPageConfig to use for this SPage
     */
    public SPage(WebDriver driver, SPageConfig config) {
        mDriver = driver;
        mOpened = true;
        mInitialUrl = mDriver.getCurrentUrl();
        mPageConfig = config;
        mPageComponents = SPageComponentFactory.createPageComponentInstances(mPageConfig.getComponentClassnames(), this);
    }

    /**
     * Construct a Page Object, not yet opened.
     * @param driver WebDriver instance, initialised and ready to use
     * @param initialUrl URL that will be used on SPage#open()
     * @param config SPageConfig to use for this SPage
     */
    public SPage(WebDriver driver, String initialUrl, SPageConfig config) {
        mDriver = driver;
        mOpened = false;
        mInitialUrl = initialUrl;
        mPageConfig = config;
        mPageComponents = SPageComponentFactory.createPageComponentInstances(mPageConfig.getComponentClassnames(), this);
    }

    public void setCookie(Cookie cookie) {
        mDriver.manage().addCookie(cookie);
    }

    /**
     * Open the Page.
     * It emulates the action of the User.
     * NOTE: An Opened page can't be reopened.
     */
    public void open() {
        // TODO - too basic
        if (!mOpened) {
            LOG.debug("SPage opening: " + mInitialUrl);

            mDriver.get(mInitialUrl);
        }
        mOpened = true;
    }

    /**
     * Disposes of a Page.
     * After this, the Page object and the internal Driver become unusable (i.e. WebDriver is quitted).
     * I blame it on Java's lack of destructor.
     */
    public void dispose() {
        mDriver.quit();
    }

    /**
     * @return Returns "false" before the SPage#open() method has been invoked.
     */
    public boolean isOpen() {
        return mOpened;
    }

    /**
     * Returns "true" if the SPage has loaded, "false" otherwise.
     * To check for loaded status, it queries all the SPageComponent inside: if all have loaded, the page has loaded.
     *
     * @return "true" if/when all SPageComponent inside the SPage have loaded.
     */
    public boolean hasLoaded() {
        for (Map.Entry<String, SPageComponent> component : mPageComponents.entrySet()) {
            if (!component.getValue().isLoaded()) {
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
     * Refer to {@link SPage#waitForLoad(long, long, java.util.concurrent.TimeUnit)}.
     * @param timeout Time before giving up
     */
    public void waitForLoad(long timeout) {
        waitForLoad(timeout, DEFAULT_PAGELOAD_WAIT_UNIT);
    }

    /**
     * Refer to {@link SPage#waitForLoad(long, long, java.util.concurrent.TimeUnit)}.
     * @param timeout Time before giving up
     * @param unit Time Unit
     */
    public void waitForLoad(long timeout, TimeUnit unit) {
        // Polling will be a constant multiple of the current Time Unit
        waitForLoad(timeout, unit.convert(DEFAULT_PAGELOAD_WAIT_POLLING, unit), unit);
    }

    /**
     * Wait for SPage to Load.
     * It will wait for all the internal SPageComponent to finish loading
     *
     * @param timeout Time before giving up the waiting
     * @param polling Time interval between checking if SPage has loaded
     * @param unit Time Unit used by timeout and polling parameters
     */
    public void waitForLoad(long timeout, long polling, TimeUnit unit) {
        Wait<SPage> waiter = new FluentWait<SPage>(this)
                .pollingEvery(polling, unit)
                .withTimeout(timeout, unit);

        waiter.until(new Function<SPage, Boolean>() {
            public Boolean apply(SPage page) {
                LOG.debug(String.format("Waiting for SPage '%s' to load", getName()));
                return page.hasLoaded();
            }
        });
    }

    /**
     * @return Name of the Page, based on the SPageConfig
     */
    public String getName() {
        // TODO
        return null;
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
        return getDriver().getCurrentUrl();
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
     * @return The SPageComponent we are after, or "null" if not found
     */
    public SPageComponent getComponent(String componentName) {
        return mPageComponents.get(componentName);
    }

    /**
     * @return A Map<String, SPageComponent> of all the Components that compose the Page (note: based on the Page Configuration).
     */
    public Map<String, SPageComponent> getComponents() {
        return mPageComponents;
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
