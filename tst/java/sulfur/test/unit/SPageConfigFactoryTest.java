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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sulfur.configs.SPageConfig;
import sulfur.factories.SPageConfigFactory;
import sulfur.factories.exceptions.SInvalidPageConfigsLocationException;
import sulfur.factories.exceptions.SPageConfigsLocationNotProvidedException;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Ivan De Marino
 */
public class SPageConfigFactoryTest {

    @BeforeMethod
    public void clearPageConfigFactoryInstance() {
        SPageConfigFactory.clearInstance();
    }

    @Test
    public void shouldGrabInstanceOfPageConfigFactory() {
        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/test_page_configs");
        assertNotNull(SPageConfigFactory.getInstance());
    }

    @Test(expectedExceptions = SPageConfigsLocationNotProvidedException.class)
    public void shouldFailToGrabInstanceOfPageConfigFactoryIfPathToPageConfigDirIsInvalid() {
        System.clearProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH);
        SPageConfigFactory.getInstance();
    }

    @Test(expectedExceptions = SInvalidPageConfigsLocationException.class)
    public void shouldFailToGrabInstanceOfPageConfigFactoryIfPageConfigDirIsEmpty() {
        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst");
        SPageConfigFactory.getInstance();
    }

    @Test
    public void shouldFindPageConfigs() {
        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/test_page_configs");
        SPageConfigFactory pageConfigFactory = SPageConfigFactory.getInstance();

        Map<String, SPageConfig> pageConfigs = pageConfigFactory.getPageConfigs();
        for (Map.Entry<String, SPageConfig> p : pageConfigs.entrySet()) {
            assertEquals(p.getKey(), p.getValue().getName());
            System.out.println(String.format("SPageConfig => name: %s - filename: %s",
                    p.getValue().getName(),
                    p.getValue().getFilename()));
        }
    }

    @DataProvider(name = "provideCurrentTestPageConfigs")
    public Object[][] provideCurrentTestPageConfigs() {
        return new Object[][] {
                { "detailspage" },
                { "fakepage" },
                { "playersolomode" }
        };
    }

    @Test(dataProvider = "provideCurrentTestPageConfigs")
    public void shouldFindTheExpectedPageConfig(String pageConfigName) {
        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/test_page_configs");
        SPageConfigFactory pageConfigFactory = SPageConfigFactory.getInstance();

        assertNotNull(pageConfigFactory.getPageConfig(pageConfigName));
        assertTrue(pageConfigFactory.getPageConfig(pageConfigName).getName().equals(pageConfigName));
    }
}
