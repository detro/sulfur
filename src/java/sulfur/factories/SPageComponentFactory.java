package sulfur.factories;

import sulfur.SPage;
import sulfur.SPageComponent;
import sulfur.factories.exceptions.SFailedToCreatePageComponentException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ivan De Marino
 */
public class SPageComponentFactory {
    /**
     * Initializes the map of SPageComponent
     * @param componentClassnames Classnames to use when creating a SPageComponent instance
     */
    public static Map<String, SPageComponent> createPageComponentInstances(String[] componentClassnames, SPage containingPage) {
        Map<String, SPageComponent> pageComponents = new HashMap<String, SPageComponent>(componentClassnames.length);

        for (String componentClassname : componentClassnames) {
            SPageComponent newPageComponent = createPageComponentInstance(componentClassname, containingPage);
            pageComponents.put(newPageComponent.getName(), newPageComponent);
        }

        return pageComponents;
    }

    /**
     * Create instance of given SPageComponent
     *
     * @param componentClassname Classname of the SPageComponent to create
     * @param containingPage SPage that will contain the Component
     * @return Instance of the SPageComponent
     * @throws sulfur.factories.exceptions.SFailedToCreatePageComponentException
     */
    public static SPageComponent createPageComponentInstance(String componentClassname, SPage containingPage) {
        try {
            Class<?> componentClass = Class.forName(componentClassname);
            Constructor<?> componentConstructor = componentClass.getConstructor(SPage.class);
            return (SPageComponent) componentConstructor.newInstance(containingPage);
        } catch (Exception e) {
            throw new SFailedToCreatePageComponentException(e);
        }
    }
}
