package sulfur.test.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import sulfur.SPage;
import sulfur.SPageComponent;

public class BodyDummyComponent extends SPageComponent {

    @FindBy(how = How.TAG_NAME, using = "body")
    public WebElement body;

    public BodyDummyComponent(SPage container) {
        super(container);
    }

    @Override
    public String getName() {
        return BodyDummyComponent.class.getSimpleName();
    }

    @Override
    public By getRootElementLocator() {
        return By.tagName("body");
    }

    @Override
    public boolean isLoaded() {
        return isElementLoaded(body);
    }

    @Override
    public boolean isVisible() {
        return isElementVisible(body);
    }
}
