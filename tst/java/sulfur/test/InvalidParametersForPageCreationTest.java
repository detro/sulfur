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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sulfur.SBaseTest;
import sulfur.SPage;
import sulfur.configs.SEnvConfig;
import sulfur.configs.SPageConfig;
import sulfur.factories.SEnvConfigFactory;
import sulfur.factories.SPageConfigFactory;
import sulfur.factories.exceptions.SMissingPathParamException;
import sulfur.factories.exceptions.SMissingQueryParamException;
import sulfur.factories.exceptions.SUnavailableDriverException;

import java.util.HashMap;

/**
* Tests to check that Invalid Parameters, provided at SPage-creation time, cause the expected failure.
*
* @author Ivan De Marino
*/
public class InvalidParametersForPageCreationTest extends SBaseTest {

    @BeforeClass
    public void setSystemProps() {
        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/test_page_configs");
        System.setProperty(SEnvConfigFactory.SYSPROP_ENV_CONFIGS_DIR_PATH, "tst/test_env_configs");
    }

    @Test(dataProvider = "provideConfiguredEnvsAndDriverNames", expectedExceptions = SMissingQueryParamException.class)
    public void shouldFailIfMandatoryQueryParameterIsMissing(SEnvConfig envConfig, String driverName) {
        SPage playerSoloMode = createSelfDisposingPage(envConfig.getName(),
                driverName,
                "playersolomode",
                new HashMap<String, String>(),  //< no path parameters
                new HashMap<String, String>()); //< no query parameters
        playerSoloMode.open();
    }

    @Test(dataProvider = "provideConfiguredEnvsAndDriverNames", expectedExceptions = SMissingPathParamException.class)
    public void shouldFailIfMandatoryPathParameterIsMissing(SEnvConfig envConfig, String driverName) {
        SPage detailsPage = createSelfDisposingPage(envConfig.getName(),
                driverName,
                "detailspage",
                new HashMap<String, String>(),  //< no path parameters
                new HashMap<String, String>()); //< no query parameters
        detailsPage.open();
    }

    @DataProvider(name = "provideCartesianEnvByPageConfigs")
    public java.util.Iterator<Object[]> provideCartesianEnvByPageConfigs() {
        return makeCartesianProvider(provideConfiguredEnvs(), provideConfiguredPages());
    }

    @Test(dataProvider = "provideCartesianEnvByPageConfigs", expectedExceptions = SUnavailableDriverException.class)
    public void shouldFailIfDriverNameIsInvalid(SEnvConfig envConfig, SPageConfig pageConfig) {
        SPage playerSoloMode = new SPage(
                envConfig,
                "madeUpDriver",
                pageConfig,
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.open();
    }

    @Test(dataProvider = "provideConfiguredEnvsAndDriverNames", expectedExceptions = RuntimeException.class)
    public void shouldFailIfPageNameIsInvalid(SEnvConfig envConfig, String driverName) {
        SPage playerSoloMode = createSelfDisposingPage(
                envConfig,
                driverName,
                SPageConfigFactory.getInstance().getPageConfig("nonExistentPage"),
                new HashMap<String, String>(),
                new HashMap<String, String>());
        playerSoloMode.open();
    }
}
