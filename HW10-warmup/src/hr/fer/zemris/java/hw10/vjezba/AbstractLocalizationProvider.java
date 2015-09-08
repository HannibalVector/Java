package hr.fer.zemris.java.hw10.vjezba;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilijan
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
    private List<ILocalizationListener> listeners;


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

    public void fire() {
        listeners.forEach(ILocalizationListener::localizationChanged);
    }
}
