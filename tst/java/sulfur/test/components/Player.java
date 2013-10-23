package sulfur.test.components;

import org.openqa.selenium.By;
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
    private static final String ROOT_ELEMENT_ID = "player_container_container";
    @FindBy(how = How.ID, using = ROOT_ELEMENT_ID)
    @CacheLookup
    private WebElement player_container_container;

    // Implicitly defined WebElements: those will be searched on the page by "id" and "name", using the variable name
    private WebElement player_container;
    private WebElement player_alerts;

    public Player(SPage containingPage) {
        super(containingPage);
    }

    @Override
    public String getName() {
        return Player.class.getSimpleName();
    }

    @Override
    public By getRootElementLocator() {
        return By.cssSelector("#" + ROOT_ELEMENT_ID);
    }

    @Override
    public boolean isLoaded() {
        return isElementLoaded(player_container_container);
    }

    @Override
    public boolean isVisible() {
        return player_container_container.isDisplayed();
    }

    public boolean isAlertsContainerVisible() {
        return player_alerts.isDisplayed();
    }

    public boolean isPlaybackContainerVisible() {
        return player_container.isDisplayed();
    }
}
