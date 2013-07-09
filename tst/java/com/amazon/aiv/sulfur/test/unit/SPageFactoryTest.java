package com.amazon.aiv.sulfur.test.unit;

import com.amazon.aiv.sulfur.factories.SConsts;
import com.amazon.aiv.sulfur.factories.SPageFactory;
import com.amazon.aiv.sulfur.factories.exceptions.SConfigNotProvidedException;
import com.amazon.aiv.sulfur.factories.exceptions.SInvalidConfigException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * @author Ivan De Marino - demarino@amazon.com
 */
public class SPageFactoryTest {

    @Test
    public void shouldBeAbleToGetPageFactory() {
        SPageFactory.clearInstance();
        System.setProperty(SConsts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.setProperty(SConsts.SYSPROP_CONFIG_FILE_PATH, "tst/ex01.sulfur.config.json");
        assertNotNull(SPageFactory.getInstance());
    }

    @Test(expectedExceptions = SConfigNotProvidedException.class)
    public void shouldFailToGetPageFactoryIfRequiredSystemPropsAreMissing() {
        SPageFactory.clearInstance();
        System.clearProperty(SConsts.SYSPROP_PAGE_CONFIGS_DIR_PATH);
        System.clearProperty(SConsts.SYSPROP_CONFIG_FILE_PATH);
        assertNotNull(SPageFactory.getInstance());
    }

    @Test(expectedExceptions = SConfigNotProvidedException.class)
    public void shouldFailToGetPageFactoryIfSystemPropToConfigFileIsMissing() {
        SPageFactory.clearInstance();
        System.setProperty(SConsts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.clearProperty(SConsts.SYSPROP_CONFIG_FILE_PATH);
        SPageFactory.getInstance();
    }

    @Test(expectedExceptions = SInvalidConfigException.class)
    public void shouldFailToGetPageFactoryIfConfigFileDoesNotExist() {
        SPageFactory.clearInstance();
        System.setProperty(SConsts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.setProperty(SConsts.SYSPROP_CONFIG_FILE_PATH, "imaginary/config/file");
        SPageFactory.getInstance();
    }

    @Test(expectedExceptions = SInvalidConfigException.class)
    public void shouldFailToGetPageFactoryIfConfigFileIsMalformedJson() {
        SPageFactory.clearInstance();
        System.setProperty(SConsts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.setProperty(SConsts.SYSPROP_CONFIG_FILE_PATH, "/etc/hosts");
        SPageFactory.getInstance();
    }
}
