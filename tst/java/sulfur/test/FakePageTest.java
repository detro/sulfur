package sulfur.test;

import org.testng.annotations.Test;
import sulfur.SBaseTest;
import sulfur.SPage;
import sulfur.factories.SPageConfigFactory;
import sulfur.factories.SPageFactory;
import sulfur.factories.exceptions.SFailedToCreatePageComponentException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ivan De Marino
 */
public class FakePageTest extends SBaseTest {

    public FakePageTest() {
        SPageConfigFactory.clearInstance();
        SPageFactory.clearInstance();
        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.sulfur.pageconfigs");
        System.setProperty(SPageFactory.SYSPROP_CONFIG_FILE_PATH, "tst/ex01.sulfur.config.json");
    }

    @Test(dataProvider = "driverProvider", expectedExceptions = SFailedToCreatePageComponentException.class)
    public void shouldFailToLoadFakePageWithFakeComponents(String driverName) {
        Map<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("asin", "B00CV77WBU");

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("asin", "B00CV77WBU");
        queryParams.put("key2", "val2");

        // create the page: this should throw an exception
        SPage p = SPageFactory.getInstance().createPage(driverName, "fakepage", pathParams, queryParams);
    }
}
