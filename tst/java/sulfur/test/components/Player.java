package sulfur.test.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import sulfur.SPage;
import sulfur.SPageComponent;

/**
 * @author Ivan De Marino
 */
public class Player extends SPageComponent {
    // Explicitly defined WebElement
    private static final String CONTAINER_CONTAINER_ID = "player_container_container";
    @FindBy(how = How.ID, using = CONTAINER_CONTAINER_ID)
    @CacheLookup
    private WebElement playerContainerContainer;

    // Implicitly defined WebElements: those will be searched on the page by "id" and "name", using the variable name
    private WebElement player_container;
    private WebElement player_alerts;

    public Player(SPage containingPage) {
        super(containingPage);
    }

    @Override
    public String getName() {
        return "Player";
    }

    @Override
    public boolean isLoaded() {
        // If "player_container_container" is on the page (even if not visible yet), the Player can be considered loaded
        Object result = getContainingPage().executeScript("return document.getElementById(arguments[0]) !== null;", CONTAINER_CONTAINER_ID);
        return result instanceof Boolean && ((Boolean)result).booleanValue();
    }

    @Override
    public boolean isVisible() {
        return playerContainerContainer.isDisplayed();
    }

    public boolean isAlertsContainerVisible() {
        return player_alerts.isDisplayed();
    }

    public boolean isPlaybackContainerVisible() {
        return player_container.isDisplayed();
    }

    @Override
    public String getSource() {
        return (String)getContainingPage().executeScript("return document.getElementById(arguments[0]).outerHTML;", CONTAINER_CONTAINER_ID);
    }
}
