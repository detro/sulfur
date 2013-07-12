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
        return "PlayerSoloModeButtons";
    }

    @Override
    public By getRootElementLocator() {
        return By.cssSelector("#" + ROOT_ELEMENT_ID);
    }

    @Override
    public boolean isLoaded() {
        // TODO check for ALL the elements...
        return play.isDisplayed();
    }

    @Override
    public boolean isVisible() {
        // TODO check for ALL the elements...
        return play.isDisplayed();
    }
}
