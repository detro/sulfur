package com.amazon.aiv.sulfur;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * TODO
 */
public class SPage {

    private final WebDriver mDriver;
    private final String mInitialUrl;

    public SPage(WebDriver driver, String initialUrl) {
        mDriver = driver;
        mInitialUrl = initialUrl;
    }

    public void setCookie(Cookie cookie) {
        mDriver.manage().addCookie(cookie);
    }

    public void open() {
        // TODO - too basic
        mDriver.get(mInitialUrl);
    }

    public void close() {
        mDriver.quit();
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
        return null;
    }

    public List<SPageComponent> getComponents() {
        return null;
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
