package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.util.Locale;

/**
 * Bridges single instance of localization provider with interested observers.
 * @author ilijan
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
    private boolean connected;
    private ILocalizationProvider provider;
    private ILocalizationListener listener = LocalizationProviderBridge.this::fire;


    public LocalizationProviderBridge(ILocalizationProvider provider) {
        this.provider = provider;
    }

    public void connect() {
        if (!connected) {
            provider.addLocalizationListener(listener);
        }

        connected = true;
    }

    public void disconnect() {
        if(connected) {
            provider.removeLocalizationListener(listener);
        }

        connected = false;
    }

    @Override
    public String getString(String key) {
        return provider.getString(key);
    }

    @Override
    public Locale getLocale() {
        return provider.getLocale();
    }
}
