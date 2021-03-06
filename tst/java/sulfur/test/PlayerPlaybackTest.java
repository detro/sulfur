package sulfur.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sulfur.SBaseTest;
import sulfur.SPage;
import sulfur.configs.SEnvConfig;
import sulfur.factories.SEnvConfigFactory;
import sulfur.factories.SPageConfigFactory;
import sulfur.test.components.BodyDummyComponent;
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
        SPageConfigFactory.clearInstance();
        SEnvConfigFactory.clearInstance();

        System.setProperty(SPageConfigFactory.SYSPROP_PAGE_CONFIGS_DIR_PATH, "tst/test_page_configs");
        System.setProperty(SEnvConfigFactory.SYSPROP_ENV_CONFIGS_DIR_PATH, "tst/test_env_configs");
    }

    @Test(dataProvider = "provideConfiguredEnvsAndDriverNames")
    public void shouldLoadPlayerOnPlayerSoloModePage(SEnvConfig envConfig, String driverName) {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("asin", "B00CV77WBU");
        queryParams.put("key2", "val2");

        // create the page
        SPage p = createOpenAndSelfDisposingPage(envConfig.getName(), driverName, "playersolomode", null, queryParams);

        // Wait 5 seconds for the Page to load
        p.waitForLoad(10, TimeUnit.SECONDS);

        // grab Player component and do some sanity checks
        Player player = (Player) p.getComponent("Player");
        assertTrue(player.isVisible());
        assertFalse(player.isAlertsContainerVisible());
        assertFalse(player.isPlaybackContainerVisible());   //< NOTICE: Playback container not-visible initially
        player.waitForLoad(10);
        assertTrue(player.isLoaded());

        // grab PlayerSoloModeButtons component
        PlayerSoloModeButtons playerButtons = (PlayerSoloModeButtons) p.getComponent("PlayerSoloModeButtons");
        assertTrue(playerButtons.isVisible());
        playerButtons.waitForLoad(10);
        assertTrue(playerButtons.isLoaded());

        // Check that, clicking on the Show/Hide Toggle, the Player's Playback Container becomes visible
        playerButtons.toggleShowHide.click();
        assertTrue(player.isPlaybackContainerVisible());

        BodyDummyComponent bodyDummyComponent = (BodyDummyComponent) p.getComponent("BodyDummyComponent");
        bodyDummyComponent.waitForLoad(10);
        assertTrue(bodyDummyComponent.isLoaded());
        assertTrue(bodyDummyComponent.isVisible());
    }
}
