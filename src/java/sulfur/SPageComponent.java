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

import org.openqa.selenium.By;

/**
 * @author Ivan De Marino
 *
 * An SPageComponent (or just "Component") is a logical representation of something present/composing a Page (SPage).
 * In other context you might see this referred as "Widget" or "a DIV".
 * But because of the abstract interface, it can even be something non-graphical like a JavaScript library/object.
 */
public abstract class SPageComponent {

    private final SPage mContainingPage;

    public SPageComponent(SPage containingPage) {
        mContainingPage = containingPage;
    }

    /**
     * @return The SPage object in which this SPageComponent is located.
     */
    protected SPage getContainingPage() {
        return mContainingPage;
    }

    /**
     * @return Component Name. Useful for debugging.
     */
    abstract public String getName();

    /**
     * @return A {@link By} locator to the Root element for this Component, "null" if none exist.
     */
    abstract public By getRootElementLocator();

    /**
     * Returns "true" if the Component on the page has loaded.
     * How a component can be considered loaded it's up to the specific component implementation.
     *
     * @return "true" if the Component on the page has loaded.
     */
    abstract public boolean isLoaded();

    /**
     * Returns "true" if the Component is visible on the page.
     * How a component can be considered visible it's up to the specific component implementation.
     * @return "true" if the Component is visible on the page.
     */
    abstract public boolean isVisible();

    /**
     * @return HTML Source code of the component (i.e. the DOM subtree as String), if any.
     */
    public String getSource() {
        By locator = getRootElementLocator();
        if (null != locator) {
            return (String) getContainingPage().executeScript(
                    "return arguments[0].outerHTML;",                        //< all the HTML that makes this component
                    getContainingPage().getDriver().findElement(locator));  //< Find the Root WebElement by it's Locator
        }
        return "";
    }
}
