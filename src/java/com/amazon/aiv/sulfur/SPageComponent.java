package com.amazon.aiv.sulfur;

import org.openqa.selenium.WebDriver;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * TODO
 */
public abstract class SPageComponent {

    private final WebDriver mDriver;

    public SPageComponent(WebDriver driver) {
        mDriver = driver;
    }

    abstract public boolean isLoaded();

    abstract public boolean isVisible();

    abstract public String getSource();
}
