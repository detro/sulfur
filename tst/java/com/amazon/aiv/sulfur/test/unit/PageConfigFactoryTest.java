package com.amazon.aiv.sulfur.test.unit;

import com.amazon.aiv.sulfur.BaseTest;
import com.amazon.aiv.sulfur.factories.Consts;
import com.amazon.aiv.sulfur.factories.PageConfigFactory;
import com.amazon.aiv.sulfur.factories.exceptions.InvalidPageConfigsLocationException;
import com.amazon.aiv.sulfur.factories.exceptions.PageConfigsLocationNotProvidedException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * @author Ivan De Marino - demarino@amazon.com
 */
public class PageConfigFactoryTest extends BaseTest {

    @Test
    public void shouldGrabInstanceOfPageConfigFactory() {
        PageConfigFactory.clearInstance();
        System.setProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.page.configs");
        assertNotNull(PageConfigFactory.getInstance());
    }

    @Test(expectedExceptions = PageConfigsLocationNotProvidedException.class)
    public void shouldFailToGrabInstanceOfPageConfigFactoryIfPathToPageConfigDirIsInvalid() {
        PageConfigFactory.clearInstance();
        System.clearProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH);
        PageConfigFactory.getInstance();
    }

    @Test(expectedExceptions = InvalidPageConfigsLocationException.class)
    public void shouldFailToGrabInstanceOfPageConfigFactoryIfPageConfigDirIsEmpty() {
        PageConfigFactory.clearInstance();
        System.setProperty(Consts.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst");
        PageConfigFactory.getInstance();
    }

}
