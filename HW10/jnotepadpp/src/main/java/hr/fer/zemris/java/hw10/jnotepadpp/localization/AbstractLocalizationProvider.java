package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements registering and deregistering of listeners in {@link ILocalizationProvider} interface.
 * @author ilijan
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
    /** List of listeners. */
    private List<ILocalizationListener> listeners;

    /**
     * Initializes list of listeners.
     */
    public AbstractLocalizationProvider() {
        listeners = new ArrayList<>();
    }

    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners about change of localization.
     */
    public void fire() {
        listeners.forEach(ILocalizationListener::localizationChanged);
    }
}
