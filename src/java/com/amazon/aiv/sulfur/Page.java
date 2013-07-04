package com.amazon.aiv.sulfur;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * TODO
 */
public class Page {

    private final WebDriver mDriver;
    private final String mInitialUrl;

    public Page(WebDriver driver, String initialUrl) {
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

    public String getUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getSource() {
        return getDriver().getPageSource();
    }

    public PageComponent getComponent(String componentName) {
        return null;
    }

    public List<PageComponent> getComponents() {
        return null;
    }

    /**
     * Returns the Driver used by this Page.
     * The assumption is that you know what a WebDriver is an the effect that you will have manipulating
     * it directly.
     *
     * It's advisable you use PageComponent(s) instead.
     * @return The WebDriver in use to this Page Object
     */
    public WebDriver getDriver() {
        return mDriver;
    }
}
