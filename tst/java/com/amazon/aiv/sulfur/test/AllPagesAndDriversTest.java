package com.amazon.aiv.sulfur.test;

import com.amazon.aiv.sulfur.BaseTest;
import com.amazon.aiv.sulfur.Page;
import com.amazon.aiv.sulfur.factories.Consts;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.assertTrue;

/**
 * @author Ivan De Marino - demarino@amazon.com
 */
public class AllPagesAndDriversTest extends BaseTest {

    public AllPagesAndDriversTest() {
        System.setProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.setProperty(Consts.SYSPROP_CONFIG_FILE_PATH, "tst/ex01.sulfur.config.json");
    }

    @DataProvider(name = "allPagesByAllDrivers")
    public Iterator<Object[]> provideAllPagesByAllDrivers() {
        return makeCartesianProvider(provideConfiguredPages(), provideConfiguredDrivers());
    }

    @Test(dataProvider = "allPagesByAllDrivers")
    public void shouldLoadAllConfiguredPagesInAllConfiguredDrivers(String pageName, String driverName) {
        Map<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("asin", "B00CV77WBU");

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("asin", "B00CV77WBU");
        queryParams.put("key2", "val2");

        // create the page (self closing)
        Page p = createSelfClosingPage(driverName, pageName, pathParams, queryParams);
        // open the page
        p.open();
        // validate the title
        assertTrue(p.getCurrentUrl().toLowerCase().contains("amazon.com"));
    }
}
