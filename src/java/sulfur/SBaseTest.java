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

import sulfur.configs.SEnvConfig;
import sulfur.configs.SPageConfig;
import sulfur.factories.SEnvConfigFactory;
import sulfur.factories.SPageConfigFactory;
import sulfur.utils.SDataProviderUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import java.util.*;

/**
 * @author Ivan De Marino
 */
abstract public class SBaseTest {

    private List<SPage> mPagesToDisposeAfterTest = new ArrayList<SPage>();

    /**
     * Utility method to ensure, even on test failure, the SPage object is correctly closed (i.e. "driver.quit()" is invoked).
     *
     * @see SBaseTest#disposePagesAfterTestMethod()
     * @param page SPage to dispose after the test.
     */
    protected SPage disposeAfterTestMethod(SPage page) {
        mPagesToDisposeAfterTest.add(page);
        return page;
    }

    @AfterMethod
    protected void disposePagesAfterTestMethod() {
        for (SPage p : mPagesToDisposeAfterTest) {
            p.dispose();
        }
    }

    /**
     * Factory method to create an SPage that is already .open() AND registered via {@link SBaseTest#disposeAfterTestMethod(SPage)}.
     * Use this method when you know you don't need to set anything extra on the page before "opening" it.
     *
     * @param envConfig Sulfur Environment Configuration
     * @param driverName Name of the WebDriver you want to use (see {@link sulfur.factories.SWebDriverFactory})
     * @param pageConfig Page Configuration
     * @param pathParams Map of Path Parameters to use
     * @param queryParams Map of Query Parameters to use
     * @return Newly created SPage, already open and registered for self disposal
     */
    protected SPage createOpenAndSelfDisposingPage(SEnvConfig envConfig,
                                                   String driverName,
                                                   SPageConfig pageConfig,
                                                   Map<String, String> pathParams,
                                                   Map<String, String> queryParams) {
        return createSelfDisposingPage(envConfig, driverName, pageConfig, pathParams, queryParams).open();
    }

    /**
     * See {@link SBaseTest#createOpenAndSelfDisposingPage(sulfur.configs.SEnvConfig, String, sulfur.configs.SPageConfig, java.util.Map, java.util.Map)}.
     *
     * @param envName
     * @param driverName
     * @param pageName
     * @param pathParams
     * @param queryParams
     * @return Newly created SPage, already open and registered for self disposal
     */
    protected SPage createOpenAndSelfDisposingPage(String envName,
                                                   String driverName,
                                                   String pageName,
                                                   Map<String, String> pathParams,
                                                   Map<String, String> queryParams) {
        return createSelfDisposingPage(envName, driverName, pageName, pathParams, queryParams).open();
    }

    /**
     * Factory method to create an SPage that is already registered via {@link SBaseTest#disposeAfterTestMethod(SPage)}.
     *
     * @param envConfig
     * @param driverName
     * @param pageConfig
     * @param pathParams
     * @param queryParams
     * @return
     */
    protected SPage createSelfDisposingPage(SEnvConfig envConfig,
                                            String driverName,
                                            SPageConfig pageConfig,
                                            Map<String, String> pathParams,
                                            Map<String, String> queryParams) {
        return disposeAfterTestMethod(new SPage(envConfig, driverName, pageConfig, pathParams, queryParams));
    }

    /**
     * See {@link SBaseTest#createSelfDisposingPage(sulfur.configs.SEnvConfig, String, sulfur.configs.SPageConfig, java.util.Map, java.util.Map)}.
     *
     * @param envName
     * @param driverName
     * @param pageName
     * @param pathParams
     * @param queryParams
     * @return
     */
    protected SPage createSelfDisposingPage(String envName,
                                            String driverName,
                                            String pageName,
                                            Map<String, String> pathParams,
                                            Map<String, String> queryParams) {
        SEnvConfig envConfig = SEnvConfigFactory.getInstance().getEnvConfig(envName);
        SPageConfig pageConfig = SPageConfigFactory.getInstance().getPageConfig(pageName);

        return disposeAfterTestMethod(new SPage(envConfig, driverName, pageConfig, pathParams, queryParams));
    }

    /**
     * DataProvider of all the Page Configs found by the SPageConfigFactory.
     * Name of the DataProvider is "provideConfiguredPageConfigs"
     *
     * @return DataProvider bi-dimensional array with list of all Page Configs (SPageConfig) that were found
     */
    @DataProvider(name = "provideConfiguredPages")
    protected Object[][] provideConfiguredPages() {
        List<Object[]> pageConfigs = new ArrayList<Object[]>();

        for (SPageConfig pageConfig : SPageConfigFactory.getInstance().getPageConfigs().values()) {
            pageConfigs.add(new Object[]{ pageConfig });
        }

        return pageConfigs.toArray(new Object[pageConfigs.size()][]);
    }

    /**
     * DataProvider of all the Env Configs found by the SEnvConfigFactory.
     * Name of the DataProvider is "provideConfiguredEnvConfigs"
     *
     * @return DataProvider bi-dimensional array with list of all Env Configs (SEnvConfigs) that were found
     */
    @DataProvider(name = "provideConfiguredEnvs")
    public Object[][] provideConfiguredEnvs() {
        List<Object[]> envConfigs = new ArrayList<Object[]>();

        for (SEnvConfig envConfig : SEnvConfigFactory.getInstance().getEnvConfigs().values()) {
            envConfigs.add(new Object[] { envConfig });
        }

        return envConfigs.toArray(new Object[envConfigs.size()][]);
    }

    /**
     * Returns DataProvider-friendly dataset of Driver Names (based on Sulfur Configuration).
     * It's ideal to be used to implement a DataProvider for a specific SEnvConfig.
     *
     * @return DataProvider bi-dimensional array with list of Drivers to use for the test
     */
    protected Object[][] provideConfiguredDriverNamesByEnv(SEnvConfig envConfig) {
        List<Object[]> driverNames = new ArrayList<Object[]>();

        for (String driverName : envConfig.getDrivers()) {
            driverNames.add(new Object[]{ driverName });
        }

        return driverNames.toArray(new Object[driverNames.size()][]);
    }

    @DataProvider(name = "provideConfiguredEnvsAndDriverNames")
    protected Object[][] provideConfiguredEnvsAndDriverNames() {
        List<Object[]> result = new ArrayList<Object[]>();

        for(SEnvConfig envConfig : SEnvConfigFactory.getInstance().getEnvConfigs().values()) {
            for (String driverName : envConfig.getDrivers()) {
                result.add(new Object[] { envConfig, driverName });
            }
        }

        return result.toArray(new Object[result.size()][]);
    }

    /**
     * Comodity method that delegates SDataProviderUtils to create Cartesian Products of @DataProvider
     *
     * @see sulfur.utils.SDataProviderUtils#cartesianProvider(Object[][]...)
     * @param providersData vararg of @DataProvider results
     * @return A @DataProvider iterator, ready to use
     */
    protected Iterator<Object[]> makeCartesianProvider(Object[][]...providersData) {
        return SDataProviderUtils.cartesianProvider(providersData);
    }

    /**
     * Comodity method that delegates SDataProviderUtils to create Cartesian Products of @DataProvider
     *
     * @see sulfur.utils.SDataProviderUtils#cartesianProvider(java.util.List)
     * @param providersData List of @DataProvider results
     * @return A @DataProvider iterator, ready to use
     */
    protected Iterator<Object[]> makeCartesianProvider(List<Object[][]> providersData) {
        return SDataProviderUtils.cartesianProvider(providersData);
    }
}
