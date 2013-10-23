package sulfur.test.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import sulfur.SPage;
import sulfur.SPageComponent;

/**
 * @author Ivan De Marino
 */
public class PlayerSoloModeButtons extends SPageComponent {
    private static final String ROOT_ELEMENT_ID = "utility_buttons";

    public WebElement toggleShowHide;
    public WebElement reload;
    public WebElement close;
    public WebElement play;
    public WebElement playFromBeginning;
    public WebElement playStreamableSibling;
    public WebElement getPlayStreamableSiblingFromBeginning;
    public WebElement playTrailer;
    public WebElement pause;
    public WebElement stop;
    public WebElement seekSeconds;
    public WebElement seek;
    public WebElement errorCode;
    public WebElement error;

    public PlayerSoloModeButtons(SPage containingPage) {
        super(containingPage);
    }

    @Override
    public String getName() {
        return PlayerSoloModeButtons.class.getSimpleName();
    }

    @Override
    public By getRootElementLocator() {
        return By.cssSelector("#" + ROOT_ELEMENT_ID);
    }

    @Override
    public boolean isLoaded() {
        return isElementLoaded(play)
                && isElementLoaded(pause)
                && isElementLoaded(stop);
    }

    @Override
    public boolean isVisible() {
        return isElementVisible(play)
                && isElementVisible(pause)
                && isElementVisible(stop);
    }
}
