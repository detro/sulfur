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

package sulfur.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sulfur.SBaseTest;
import sulfur.SPage;
import sulfur.factories.SConfig;
import sulfur.factories.SPageConfigFactory;
import sulfur.factories.SPageFactory;
import sulfur.factories.exceptions.SMissingPathParamException;
import sulfur.factories.exceptions.SMissingQueryParamException;
import sulfur.factories.exceptions.SUnavailableDriverException;
import sulfur.factories.exceptions.SUnavailablePageException;

import java.util.HashMap;

/**
 * Tests to check that Invalid Parameters, provided at SPage-creation time, cause the expected failure.
 *
 * @author Ivan De Marino
 */
public class InvalidParametersForPageCreationTest extends SBaseTest {

    @BeforeClass
    public void setSystemProps() {
        SPageConfigFactory.clearInstance();
        SPageFactory.clearInstance();
        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.sulfur.pageconfigs");
        System.setProperty(SConfig.SYSPROP_CONFIG_FILE_PATH, "tst/ex01.sulfur.config.json");
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
