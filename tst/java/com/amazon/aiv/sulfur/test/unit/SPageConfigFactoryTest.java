package com.amazon.aiv.sulfur.test.unit;

import com.amazon.aiv.sulfur.SBaseTest;
import com.amazon.aiv.sulfur.factories.SConsts;
import com.amazon.aiv.sulfur.factories.SPageConfigFactory;
import com.amazon.aiv.sulfur.factories.exceptions.SInvalidPageConfigsLocationException;
import com.amazon.aiv.sulfur.factories.exceptions.SPageConfigsLocationNotProvidedException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * @author Ivan De Marino - demarino@amazon.com
 */
public class SPageConfigFactoryTest extends SBaseTest {

    @Test
    public void shouldGrabInstanceOfPageConfigFactory() {
        SPageConfigFactory.clearInstance();
        System.setProperty(SConsts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        assertNotNull(SPageConfigFactory.getInstance());
    }

    @Test(expectedExceptions = SPageConfigsLocationNotProvidedException.class)
    public void shouldFailToGrabInstanceOfPageConfigFactoryIfPathToPageConfigDirIsInvalid() {
        SPageConfigFactory.clearInstance();
        System.clearProperty(SConsts.SYSPROP_PAGE_CONFIGS_DIR_PATH);
        SPageConfigFactory.getInstance();
    }

    @Test(expectedExceptions = SInvalidPageConfigsLocationException.class)
    public void shouldFailToGrabInstanceOfPageConfigFactoryIfPageConfigDirIsEmpty() {
        SPageConfigFactory.clearInstance();
        System.setProperty(SConsts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst");
        SPageConfigFactory.getInstance();
    }

}
