package com.amazon.aiv.sulfur;

import com.amazon.aiv.sulfur.factories.SPageFactory;
import com.amazon.aiv.sulfur.utils.SDataProviderUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import java.util.*;

/**
 * @author Ivan De Marino <demarino@amazon.com>
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
     * @see com.amazon.aiv.sulfur.utils.SDataProviderUtils#cartesianProvider(Object[][]...)
     * @param providersData vararg of @DataProvider results
     * @return A @DataProvider iterator, ready to use
     */
    public Iterator<Object[]> makeCartesianProvider(Object[][]...providersData) {
        return SDataProviderUtils.cartesianProvider(providersData);
    }

    /**
     * Comodity method that delegates SDataProviderUtils to create Cartesian Products of @DataProvider
     *
     * @see com.amazon.aiv.sulfur.utils.SDataProviderUtils#cartesianProvider(java.util.List)
     * @param providersData List of @DataProvider results
     * @return A @DataProvider iterator, ready to use
     */
    public Iterator<Object[]> makeCartesianProvider(List<Object[][]> providersData) {
        return SDataProviderUtils.cartesianProvider(providersData);
    }
}
