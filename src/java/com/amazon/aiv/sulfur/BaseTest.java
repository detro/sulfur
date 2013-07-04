package com.amazon.aiv.sulfur;

import com.amazon.aiv.sulfur.factories.PageFactory;
import com.amazon.aiv.sulfur.utils.DataProviderUtils;
import com.google.common.collect.ImmutableList;
import org.testng.annotations.DataProvider;

import java.util.*;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 */
public class BaseTest {

    // One factory for all the Tests, even across multiple threads
    private static final PageFactory mPageFactory = PageFactory.getInstance();

    /**
     * @DataProvider of all the Pages found by the PageConfigFactory.
     * Name of the @DataProvider is "pageProvider"
     *
     * @return @DataProvider bi-dimensional array with list of all Pages for which a PageConfig was found
     */
    @DataProvider(name = "pageProvider")
    public Object[][] provideConfiguredPages() {
        List<Object[]> pages = new ArrayList<Object[]>();

        for (String configuredPageName : mPageFactory.getAvailablePageConfigs()) {
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
        List<Object[]> drivers = new ArrayList<Object[]>();

        for (String configuredDriver : mPageFactory.getConfig().getDrivers()) {
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

    /**
     * The PageFactory.
     * NOTE: There is ONE PageFactory for all the tests (synchronized)
     *
     * @return The PageFactory
     */
    protected static PageFactory getPageFactory() {
        return mPageFactory;
    }
}
