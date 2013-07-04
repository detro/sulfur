package com.amazon.aiv.sulfur.test;

import com.amazon.aiv.sulfur.BaseTest;
import com.amazon.aiv.sulfur.Page;
import com.amazon.aiv.sulfur.factories.exceptions.MissingPathParamException;
import com.amazon.aiv.sulfur.factories.exceptions.MissingQueryParamException;
import com.amazon.aiv.sulfur.factories.exceptions.UnavailableDriverException;
import com.amazon.aiv.sulfur.factories.exceptions.UnavailablePageException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Tests to check that Invalid Parameters, provided at Page-creation time, cause the expected failure.
 *
 * @author Ivan De Marino - demarino@amazon.com
 */
public class InvalidParametersForPageCreationTest extends BaseTest {
    @Test(dataProvider = "driverProvider", expectedExceptions = MissingQueryParamException.class)
    public void shouldFailIfMandatoryQueryParameterIsMissing(String driverName) {
        Page playerSoloMode = getPageFactory().createPage(
                driverName,
                "playersolomode",
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.load();
    }

    @Test(dataProvider = "driverProvider", expectedExceptions = MissingPathParamException.class)
    public void shouldFailIfMandatoryPathParameterIsMissing(String driverName) {
        Page playerSoloMode = getPageFactory().createPage(
                driverName,
                "detailspage",
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.load();
    }

    @Test(dataProvider = "pageProvider", expectedExceptions = UnavailableDriverException.class)
    public void shouldFailIfDriverNameIsInvalid(String pageName) {
        Page playerSoloMode = getPageFactory().createPage(
                "madeUpDriver",
                pageName,
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.load();
    }

    @Test(dataProvider = "driverProvider", expectedExceptions = UnavailablePageException.class)
    public void shouldFailIfPageNameIsInvalid(String driverName) {
        Page playerSoloMode = getPageFactory().createPage(
                driverName,
                "nonExistentPage",
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.load();
    }
}
