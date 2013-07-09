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

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import sulfur.factories.exceptions.SFailedToCreatePageComponentException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Ivan De Marino
 *
 * TODO
 */
public class SPage {

    private boolean mOpened;

    private final WebDriver mDriver;
    private final String mInitialUrl;
    private Map<String, SPageComponent> mPageComponents;

    /**
     * Copy-constructor.
     *
     * @param page SPage to copy from. It will also copy the "opened" status.
     */
    public SPage(SPage page) {
        // Straight copy
        mDriver = page.getDriver();
        mPageComponents = page.mPageComponents;

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
     * @param componentClassnames Classnames of the SPageComponent that this SPage have to contain
     */
    public SPage(WebDriver driver, String[] componentClassnames) {
        mDriver = driver;
        mOpened = true;
        mInitialUrl = mDriver.getCurrentUrl();
        initPageComponentInstances(componentClassnames);
    }

    /**
     * Construct a Page Object, not yet opened.
     * @param driver WebDriver instance, initialised and ready to use
     * @param initialUrl URL that will be used on SPage#open()
     * @param componentClassnames Classnames of the SPageComponent that this SPage have to contain
     */
    public SPage(WebDriver driver, String initialUrl, String[] componentClassnames) {
        mDriver = driver;
        mOpened = false;
        mInitialUrl = initialUrl;
        initPageComponentInstances(componentClassnames);
    }

    /**
     * Initializes the map of SPageComponent
     * @param componentClassnames Classnames to use when creating a SPageComponent instance
     */
    private void initPageComponentInstances(String[] componentClassnames) {
        mPageComponents = new HashMap<String, SPageComponent>(componentClassnames.length);
        for (String componentClassname : componentClassnames) {
            SPageComponent newPageComponent = createPageComponentInstance(componentClassname, this);
            mPageComponents.put(newPageComponent.getName(), newPageComponent);
        }
    }

    /**
     * Create instance of given SPageComponent
     *
     * @param componentClassname Classname of the SPageComponent to create
     * @param containingPage SPage that will contain the Component
     * @return Instance of the SPageComponent
     * @throws sulfur.factories.exceptions.SFailedToCreatePageComponentException
     */
    private SPageComponent createPageComponentInstance(String componentClassname, SPage containingPage) {
        try {
            Class<?> componentClass = Class.forName(componentClassname);
            Constructor<?> componentConstructor = componentClass.getConstructor(SPage.class);
            return (SPageComponent) componentConstructor.newInstance(containingPage);
        } catch (Exception e) {
            throw new SFailedToCreatePageComponentException(e);
        }
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
            mDriver.get(mInitialUrl);
        }
        mOpened = true;
    }

    /**
     * Disposes of a Page.
     * After this, the Page object and the internal Driver become unusable (i.e. WebDriver is quitted).
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

    public String getSource() {
        return getDriver().getPageSource();
    }

    public SPageComponent getComponent(String componentName) {
        return mPageComponents.get(componentName);
    }

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
}
