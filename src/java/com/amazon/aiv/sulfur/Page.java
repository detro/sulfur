package com.amazon.aiv.sulfur;

import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * TODO
 */
public class Page {

    private WebDriver mDriver;

    public Page(WebDriver driver) {
        mDriver = driver;
    }

    public load() {
        // TODO
    }

    public String getTitle() {
        return null;
    }

    public String getUrl() {
        return null;
    }

    public String getSource() {
        return null;
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
