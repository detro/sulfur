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
import sulfur.configs.SEnvConfig;
import sulfur.configs.SPageConfig;
import sulfur.factories.SEnvConfigFactory;
import sulfur.factories.SPageConfigFactory;
import sulfur.factories.exceptions.SEnvConfigsLocationNotProvidedException;
import sulfur.factories.exceptions.SInvalidEnvConfigsLocationException;
import sulfur.factories.exceptions.SInvalidPageConfigsLocationException;
import sulfur.factories.exceptions.SPageConfigsLocationNotProvidedException;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Ivan De Marino
 */
public class SEnvConfigFactoryTest {

    @BeforeMethod
    public void clearEnvConfigFactoryInstance() {
        SEnvConfigFactory.clearInstance();
    }

    @Test
    public void shouldGrabInstanceOfEnvConfigFactory() {
        System.setProperty(SEnvConfigFactory.SYSPROP_ENV_CONFIGS_DIR_PATH, "tst/test_env_configs");
        assertNotNull(SEnvConfigFactory.getInstance());
    }

    @Test(expectedExceptions = SEnvConfigsLocationNotProvidedException.class)
    public void shouldFailToGrabInstanceOfEnvConfigFactoryIfPathToPageConfigDirIsInvalid() {
        System.clearProperty(SEnvConfigFactory.SYSPROP_ENV_CONFIGS_DIR_PATH);
        SEnvConfigFactory.getInstance();
    }

    @Test(expectedExceptions = SInvalidEnvConfigsLocationException.class)
    public void shouldFailToGrabInstanceOfEnvConfigFactoryIfPageConfigDirIsEmpty() {
        System.setProperty(SEnvConfigFactory.SYSPROP_ENV_CONFIGS_DIR_PATH, "tst");
        SEnvConfigFactory.getInstance();
    }

    @Test
    public void shouldFindEnvConfigs() {
        System.setProperty(SEnvConfigFactory.SYSPROP_ENV_CONFIGS_DIR_PATH, "tst/test_env_configs");
        SEnvConfigFactory envConfigFactory = SEnvConfigFactory.getInstance();

        Map<String, SEnvConfig> envConfigs = envConfigFactory.getEnvConfigs();
        for (Map.Entry<String, SEnvConfig> p : envConfigs.entrySet()) {
            assertEquals(p.getKey(), p.getValue().getName());
            System.out.println(String.format("SEnvConfig => name: %s - filename: %s",
                    p.getValue().getName(),
                    p.getValue().getFilename()));
        }
    }

    @DataProvider(name = "provideCurrentTestEnvConfigs")
    public Object[][] provideCurrentTestEnvConfigs() {
        return new Object[][] {
                { "prod" }
        };
    }

    @Test(dataProvider = "provideCurrentTestEnvConfigs")
    public void shouldFindTheExpectedEnvConfig(String envConfigName) {
        System.setProperty(SEnvConfigFactory.SYSPROP_ENV_CONFIGS_DIR_PATH, "tst/test_env_configs");
        SEnvConfigFactory envConfigFactory = SEnvConfigFactory.getInstance();

        assertNotNull(envConfigFactory.getEnvConfig(envConfigName));
        assertTrue(envConfigFactory.getEnvConfig(envConfigName).getName().equals(envConfigName));
    }
}
