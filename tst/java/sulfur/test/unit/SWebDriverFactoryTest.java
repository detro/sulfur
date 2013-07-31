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

package sulfur.test.unit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import sulfur.factories.SConfig;
import sulfur.factories.SPageConfigFactory;
import sulfur.factories.SPageFactory;
import sulfur.factories.SWebDriverFactory;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Ivan De Marino
 */
public class SWebDriverFactoryTest {
    private WebDriver mDriver;

    @BeforeClass
    public void initMandatorySystemProperties() {
        SPageConfigFactory.clearInstance();
        SPageFactory.clearInstance();
        SWebDriverFactory.clearInstance();
        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.sulfur.pageconfigs");
        System.setProperty(SConfig.SYSPROP_CONFIG_FILE_PATH, "tst/ex01.sulfur.config.json");
    }

    @BeforeMethod
    public void prepDriver() {
        mDriver = null;
    }

    @AfterMethod
    public void quitDriver() {
        mDriver.quit();
    }

    @DataProvider(name = "driverNameAndClassProvider")
    public Object[][] provideDriverNameAndClass() {
        return new Object[][] {
                { "firefox", FirefoxDriver.class },
                { "chrome", ChromeDriver.class }
        };
    }

    @Test(dataProvider = "driverNameAndClassProvider")
    public void shouldCreateDriver(String driverName, Class driverClass) {
        mDriver = SWebDriverFactory.getInstance().createDriver(driverName);
        assertNotNull(mDriver);
        assertTrue(driverClass.isInstance(mDriver));
    }

}
