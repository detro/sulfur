package sulfur.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sulfur.SBaseTest;
import sulfur.SPage;
import sulfur.factories.SConfig;
import sulfur.factories.SPageConfigFactory;
import sulfur.factories.SPageFactory;
import sulfur.test.components.Player;
import sulfur.test.components.PlayerSoloModeButtons;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Ivan De Marino
 */
public class PlayerPlaybackTest extends SBaseTest {

    @BeforeClass
    public void setSystemProps() {
        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/ex01.sulfur.pageconfigs");
        System.setProperty(SConfig.SYSPROP_CONFIG_FILE_PATH, "tst/ex01.sulfur.config.json");
    }

//    @Test(dataProvider = "driverProvider", expectedExceptions = SUnavailableComponentException.class)
    @Test(dataProvider = "driverProvider")
    public void shouldLoadPlayerOnPlayerSoloModePage(String driverName) {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("asin", "B00CV77WBU");
        queryParams.put("key2", "val2");

        // create the page
        SPage p = SPageFactory.getInstance().createPage(driverName, "playersolomode", null, queryParams);
        // make sure it's not "left hanging" after the test has finished
        disposeAfterTestMethod(p);
        // open the page
        p.open();

        // Wait 5 seconds for the Page to load
        p.waitForLoad(10, TimeUnit.SECONDS);

        // grab Player component and do some sanity checks
        Player player = (Player) p.getComponent("Player");
        assertTrue(player.isVisible());
        assertFalse(player.isAlertsContainerVisible());
        assertFalse(player.isPlaybackContainerVisible());   //< NOTICE: Playback container not-visible initially

        // grab PlayerSoloModeButtons component
        PlayerSoloModeButtons playerButtons = (PlayerSoloModeButtons) p.getComponent("PlayerSoloModeButtons");
        assertTrue(playerButtons.isVisible());

        // Check that, clicking on the Show/Hide Toggle, the Player's Playback Container becomes visible
        playerButtons.toggleShowHide.click();
        assertTrue(player.isPlaybackContainerVisible());
    }
}
