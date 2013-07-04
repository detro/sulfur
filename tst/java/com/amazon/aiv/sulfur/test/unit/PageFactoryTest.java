package com.amazon.aiv.sulfur.test.unit;

import com.amazon.aiv.sulfur.factories.Consts;
import com.amazon.aiv.sulfur.factories.PageFactory;
import com.amazon.aiv.sulfur.factories.exceptions.ConfigNotProvidedException;
import com.amazon.aiv.sulfur.factories.exceptions.InvalidConfigException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * @author Ivan De Marino - demarino@amazon.com
 */
public class PageFactoryTest {

    @Test
    public void shouldBeAbleToGetPageFactory() {
        PageFactory.clearInstance();
        System.setProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.setProperty(Consts.SYSPROP_CONFIG_FILE_PATH, "tst/ex01.sulfur.config.json");
        assertNotNull(PageFactory.getInstance());
    }

    @Test(expectedExceptions = ConfigNotProvidedException.class)
    public void shouldFailToGetPageFactoryIfRequiredSystemPropsAreMissing() {
        PageFactory.clearInstance();
        System.clearProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH);
        System.clearProperty(Consts.SYSPROP_CONFIG_FILE_PATH);
        assertNotNull(PageFactory.getInstance());
    }

    @Test(expectedExceptions = ConfigNotProvidedException.class)
    public void shouldFailToGetPageFactoryIfSystemPropToConfigFileIsMissing() {
        PageFactory.clearInstance();
        System.setProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.clearProperty(Consts.SYSPROP_CONFIG_FILE_PATH);
        PageFactory.getInstance();
    }

    @Test(expectedExceptions = InvalidConfigException.class)
    public void shouldFailToGetPageFactoryIfConfigFileDoesNotExist() {
        PageFactory.clearInstance();
        System.setProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.setProperty(Consts.SYSPROP_CONFIG_FILE_PATH, "imaginary/config/file");
        PageFactory.getInstance();
    }

    @Test(expectedExceptions = InvalidConfigException.class)
    public void shouldFailToGetPageFactoryIfConfigFileIsMalformedJson() {
        PageFactory.clearInstance();
        System.setProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        System.setProperty(Consts.SYSPROP_CONFIG_FILE_PATH, "/etc/hosts");
        PageFactory.getInstance();
    }
}
