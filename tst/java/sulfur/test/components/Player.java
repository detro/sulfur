package sulfur.test.components;

import sulfur.SPage;
import sulfur.SPageComponent;

/**
 * @author Ivan De Marino
 */
public class Player extends SPageComponent {

    public Player(SPage parentPage) {
        super(parentPage);
    }

    @Override
    public String getName() {
        // TODO
        return "Player";
    }

    @Override
    public boolean isLoaded() {
        // TODO
        return true;
    }

    @Override
    public boolean isVisible() {
        // TODO
        return true;
    }

    @Override
    public String getSource() {
        // TODO
        return getDriver().getPageSource();
    }
}
