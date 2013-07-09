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

import sulfur.factories.SPageFactory;
import sulfur.utils.SDataProviderUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import java.util.*;

/**
 * @author Ivan De Marino
 */
public class SBaseTest {

    private List<SPage> mPagesToCloseAfterTest = new ArrayList<SPage>();

    /**
     * Utility method to ensure, even on test failure, the SPage object is correctly closed (i.e. "driver.quit()" is invoked).
     *
     * @see SBaseTest#closePagesAfterMethod()
     * @param page SPage to close after the test.
     */
    protected SPage doCloseAfterMethod(SPage page) {
        mPagesToCloseAfterTest.add(page);
        return page;
    }

    @AfterMethod
    protected void closePagesAfterMethod() {
        for (SPage p : mPagesToCloseAfterTest) {
            p.close();
        }
    }

    /**
     * Utility method that creates a SPage that is already listed as a "closing after method".
     * Signature reflects the @see SPageFactory#createPage signature.
     *
     * @param driverName
     * @param pageName
     * @param pathParams
     * @param queryParams
     * @return SPage that is already added to the list of SPage that will "close after method".
     */
    protected SPage createSelfClosingPage(String driverName,
                                         String pageName,
                                         Map<String, String> pathParams,
                                         Map<String, String> queryParams) {
        return doCloseAfterMethod(SPageFactory.getInstance().createPage(
                driverName, pageName,
                pathParams, queryParams));
    }

    /**
     * @DataProvider of all the Pages found by the SPageConfigFactory.
     * Name of the @DataProvider is "pageProvider"
     *
     * @return @DataProvider bi-dimensional array with list of all Pages for which a SPageConfig was found
     */
    @DataProvider(name = "pageProvider")
    public Object[][] provideConfiguredPages() {
        SPageFactory pageFactory = SPageFactory.getInstance();
        List<Object[]> pages = new ArrayList<Object[]>();

        for (String configuredPageName : pageFactory.getAvailablePageConfigs()) {
            pages.add(new Object[]{ configuredPageName });
        }

        return pages.toArray(new Object[pages.size()][]);
    }

    /**
     * @DataProvider of Drivers (based on Sulfur Configuration).
     * Name of the @DataProvider is "driverProvider".
     *
     * @return @DataProvider bi-dimensional array with list of Drivers to use for the test
     */
    @DataProvider(name = "driverProvider")
    public Object[][] provideConfiguredDrivers() {
        SPageFactory pageFactory = SPageFactory.getInstance();
        List<Object[]> drivers = new ArrayList<Object[]>();

        for (String configuredDriver : pageFactory.getConfig().getDrivers()) {
            drivers.add(new Object[]{ configuredDriver });
        }

        return drivers.toArray(new Object[drivers.size()][]);
    }

    /**
     * Comodity method that delegates SDataProviderUtils to create Cartesian Products of @DataProvider
     *
     * @see sulfur.utils.SDataProviderUtils#cartesianProvider(Object[][]...)
     * @param providersData vararg of @DataProvider results
     * @return A @DataProvider iterator, ready to use
     */
    public Iterator<Object[]> makeCartesianProvider(Object[][]...providersData) {
        return SDataProviderUtils.cartesianProvider(providersData);
    }

    /**
     * Comodity method that delegates SDataProviderUtils to create Cartesian Products of @DataProvider
     *
     * @see sulfur.utils.SDataProviderUtils#cartesianProvider(java.util.List)
     * @param providersData List of @DataProvider results
     * @return A @DataProvider iterator, ready to use
     */
    public Iterator<Object[]> makeCartesianProvider(List<Object[][]> providersData) {
        return SDataProviderUtils.cartesianProvider(providersData);
    }
}
