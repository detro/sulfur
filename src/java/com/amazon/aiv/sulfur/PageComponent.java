package com.amazon.aiv.sulfur;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * TODO
 */
public abstract class PageComponent {

    abstract public boolean isLoaded();

    abstract public boolean isVisible();

    abstract public String getSource();
}
