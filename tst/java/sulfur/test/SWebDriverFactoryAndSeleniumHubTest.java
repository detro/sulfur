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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.net.UrlChecker;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.server.SeleniumServer;
import org.openqa.selenium.server.cli.RemoteControlLauncher;
import org.testng.annotations.*;
import sulfur.SBaseTest;
import sulfur.configs.SEnvConfig;
import sulfur.factories.SEnvConfigFactory;
import sulfur.factories.SPageConfigFactory;
import sulfur.factories.SWebDriverFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
* @author Ivan De Marino
*/
public class SWebDriverFactoryAndSeleniumHubTest extends SBaseTest {

    private SeleniumServerRunner mSeleniumServer = null;
    private WebDriver mDriver;

    @BeforeClass
    public void setSystemProps() {
        SPageConfigFactory.clearInstance();
        SEnvConfigFactory.clearInstance();

        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/test_page_configs");
        System.setProperty(SEnvConfigFactory.SYSPROP_ENV_CONFIGS_DIR_PATH, "tst/test_env_localseleniumgrid_configs");
    }

    @BeforeClass
    public void startSeleniumServer() {
        SEnvConfig localSeleniumGridEnvConfig = (SEnvConfig)SEnvConfigFactory.getInstance().getEnvConfigs().values().toArray()[0];

        if (null == mSeleniumServer) {
            mSeleniumServer = new SeleniumServerRunner();
            mSeleniumServer.start();
            try {
                new UrlChecker().waitUntilAvailable(20, TimeUnit.SECONDS, new URL(localSeleniumGridEnvConfig.getSeleniumHub().toString() + "/status"));
            } catch (UrlChecker.TimeoutException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterClass
    public void stopSeleniumServer() {
        if (null != mSeleniumServer) {
            mSeleniumServer.shutdown();
        }
    }

    @BeforeMethod
    public void prepDriver() {
        mDriver = null;
    }

    @AfterMethod
    public void quitDriver() {
        mDriver.quit();
    }

    @Test(dataProvider = "provideConfiguredEnvsAndDriverNames")
    public void shouldCreateDriver(SEnvConfig envConfig, String driverName) {
        mDriver = new SWebDriverFactory(envConfig).createDriver(driverName);
        assertNotNull(mDriver);
        assertTrue(mDriver instanceof RemoteWebDriver);
    }

    /**
     * A Thread where we run the SeleniumServer used in this test
     */
    public class SeleniumServerRunner extends Thread {

        private boolean mRunning = false;
        private SeleniumServer mSeleniumServer = null;

        @Override
        public void run() {
            try {
                // Start the Selenium Server
                System.setProperty("org.openqa.jetty.http.HttpRequest.maxFormContentSize", "0");
                mSeleniumServer = new SeleniumServer(
                        false,                                                          //< no slow resources
                        RemoteControlLauncher.parseLauncherOptions(new String[] {}));   //< options
                mSeleniumServer.boot();

                // Enter in a "while loop" waiting for the Server to be not running anymore
                mRunning = true;
                while (mRunning) { /* nothing to do - the Selenium Server is running */}

                // Stop the actual Selenium server
                mSeleniumServer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void shutdown() {
            mRunning = false;
        }
    }
}
