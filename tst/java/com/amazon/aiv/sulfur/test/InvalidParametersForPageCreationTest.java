package com.amazon.aiv.sulfur.test;

import com.amazon.aiv.sulfur.BaseTest;
import com.amazon.aiv.sulfur.Page;
import com.amazon.aiv.sulfur.factories.Consts;
import com.amazon.aiv.sulfur.factories.PageFactory;
import com.amazon.aiv.sulfur.factories.exceptions.MissingPathParamException;
import com.amazon.aiv.sulfur.factories.exceptions.MissingQueryParamException;
import com.amazon.aiv.sulfur.factories.exceptions.UnavailableDriverException;
import com.amazon.aiv.sulfur.factories.exceptions.UnavailablePageException;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Tests to check that Invalid Parameters, provided at Page-creation time, cause the expected failure.
 *
 * @author Ivan De Marino - demarino@amazon.com
 */
public class InvalidParametersForPageCreationTest extends BaseTest {

    public InvalidParametersForPageCreationTest() {
        System.setProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.setProperty(Consts.SYSPROP_CONFIG_FILE_PATH, "tst/ex01.sulfur.config.json");
    }

    @Test(dataProvider = "driverProvider", expectedExceptions = MissingQueryParamException.class)
    public void shouldFailIfMandatoryQueryParameterIsMissing(String driverName) {
        Page playerSoloMode = PageFactory.getInstance().createPage(
                driverName,
                "playersolomode",
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.open();
    }

    @Test(dataProvider = "driverProvider", expectedExceptions = MissingPathParamException.class)
    public void shouldFailIfMandatoryPathParameterIsMissing(String driverName) {
        Page playerSoloMode = PageFactory.getInstance().createPage(
                driverName,
                "detailspage",
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.open();
    }

    @Test(dataProvider = "pageProvider", expectedExceptions = UnavailableDriverException.class)
    public void shouldFailIfDriverNameIsInvalid(String pageName) {
        Page playerSoloMode = PageFactory.getInstance().createPage(
                "madeUpDriver",
                pageName,
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.open();
    }

    @Test(dataProvider = "driverProvider", expectedExceptions = UnavailablePageException.class)
    public void shouldFailIfPageNameIsInvalid(String driverName) {
        Page playerSoloMode = PageFactory.getInstance().createPage(
                driverName,
                "nonExistentPage",
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.open();
    }
}
