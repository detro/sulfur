package com.amazon.aiv.sulfur.test;

import com.amazon.aiv.sulfur.SBaseTest;
import com.amazon.aiv.sulfur.SPage;
import com.amazon.aiv.sulfur.factories.SConsts;
import com.amazon.aiv.sulfur.factories.SPageFactory;
import com.amazon.aiv.sulfur.factories.exceptions.SMissingPathParamException;
import com.amazon.aiv.sulfur.factories.exceptions.SMissingQueryParamException;
import com.amazon.aiv.sulfur.factories.exceptions.SUnavailableDriverException;
import com.amazon.aiv.sulfur.factories.exceptions.SUnavailablePageException;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Tests to check that Invalid Parameters, provided at SPage-creation time, cause the expected failure.
 *
 * @author Ivan De Marino - demarino@amazon.com
 */
public class InvalidParametersForPageCreationTest extends SBaseTest {

    public InvalidParametersForPageCreationTest() {
        System.setProperty(SConsts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.setProperty(SConsts.SYSPROP_CONFIG_FILE_PATH, "tst/ex01.sulfur.config.json");
    }

    @Test(dataProvider = "driverProvider", expectedExceptions = SMissingQueryParamException.class)
    public void shouldFailIfMandatoryQueryParameterIsMissing(String driverName) {
        SPage playerSoloMode = SPageFactory.getInstance().createPage(
                driverName,
                "playersolomode",
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.open();
    }

    @Test(dataProvider = "driverProvider", expectedExceptions = SMissingPathParamException.class)
    public void shouldFailIfMandatoryPathParameterIsMissing(String driverName) {
        SPage playerSoloMode = SPageFactory.getInstance().createPage(
                driverName,
                "detailspage",
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.open();
    }

    @Test(dataProvider = "pageProvider", expectedExceptions = SUnavailableDriverException.class)
    public void shouldFailIfDriverNameIsInvalid(String pageName) {
        SPage playerSoloMode = SPageFactory.getInstance().createPage(
                "madeUpDriver",
                pageName,
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.open();
    }

    @Test(dataProvider = "driverProvider", expectedExceptions = SUnavailablePageException.class)
    public void shouldFailIfPageNameIsInvalid(String driverName) {
        SPage playerSoloMode = SPageFactory.getInstance().createPage(
                driverName,
                "nonExistentPage",
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.open();
    }
}
