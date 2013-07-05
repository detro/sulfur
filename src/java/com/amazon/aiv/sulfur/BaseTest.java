package com.amazon.aiv.sulfur;

import com.amazon.aiv.sulfur.factories.PageFactory;
import com.amazon.aiv.sulfur.utils.DataProviderUtils;
import com.google.common.collect.ImmutableList;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;

import java.util.*;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class BaseTest {

    private List<Page> mPagesToCloseAfterTest = new ArrayList<Page>();

    /**
     * Utility method to ensure, even on test failure, the Page object is correctly closed (i.e. "driver.quit()" is invoked).
     *
     * @see com.amazon.aiv.sulfur.BaseTest#closePagesAfterMethod()
     * @param page Page to close after the test.
     */
    protected Page doCloseAfterMethod(Page page) {
        mPagesToCloseAfterTest.add(page);
        return page;
    }

    @AfterMethod
    protected void closePagesAfterMethod() {
        for (Page p : mPagesToCloseAfterTest) {
            p.close();
        }
    }

    /**
     * Utility method that creates a Page that is already listed as a "closing after method".
     * Signature reflects the @see PageFactory#createPage signature.
     *
     * @param driverName
     * @param pageName
     * @param pathParams
     * @param queryParams
     * @return Page that is already added to the list of Page that will "close after method".
     */
    protected Page createSelfClosingPage(String driverName,
                                         String pageName,
                                         Map<String, String> pathParams,
                                         Map<String, String> queryParams) {
        return doCloseAfterMethod(PageFactory.getInstance().createPage(
                driverName, pageName,
                pathParams, queryParams));
    }

    /**
     * @DataProvider of all the Pages found by the PageConfigFactory.
     * Name of the @DataProvider is "pageProvider"
     *
     * @return @DataProvider bi-dimensional array with list of all Pages for which a PageConfig was found
     */
    @DataProvider(name = "pageProvider")
    public Object[][] provideConfiguredPages() {
        PageFactory pageFactory = PageFactory.getInstance();
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
        PageFactory pageFactory = PageFactory.getInstance();
        List<Object[]> drivers = new ArrayList<Object[]>();

        for (String configuredDriver : pageFactory.getConfig().getDrivers()) {
            drivers.add(new Object[]{ configuredDriver });
        }

        return drivers.toArray(new Object[drivers.size()][]);
    }

    /**
     * Comodity method that delegates DataProviderUtils to create Cartesian Products of @DataProvider
     *
     * @see DataProviderUtils#cartesianProvider(Object[][]...)
     * @param providersData vararg of @DataProvider results
     * @return A @DataProvider iterator, ready to use
     */
    public Iterator<Object[]> makeCartesianProvider(Object[][]...providersData) {
        return DataProviderUtils.cartesianProvider(providersData);
    }

    /**
     * Comodity method that delegates DataProviderUtils to create Cartesian Products of @DataProvider
     *
     * @see DataProviderUtils#cartesianProvider(java.util.List)
     * @param providersData List of @DataProvider results
     * @return A @DataProvider iterator, ready to use
     */
    public Iterator<Object[]> makeCartesianProvider(List<Object[][]> providersData) {
        return DataProviderUtils.cartesianProvider(providersData);
    }
}
