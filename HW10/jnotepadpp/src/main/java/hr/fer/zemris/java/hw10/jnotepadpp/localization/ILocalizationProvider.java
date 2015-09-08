package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.util.Locale;

/**
 * Gives translations and locale for provided keys.
 * @author ilijan
 */
public interface ILocalizationProvider {

    /**
     * Registers new listener.
     * @param listener listener to be registered.
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * Removes given listener.
     * @param listener listener to be removed.
     */
    void removeLocalizationListener(ILocalizationListener listener);

    /**
     * Returns translation for given key.
     * @param key   key for wanted translation.
     * @return      string containing wanted translation.
     */
    String getString(String key);

    /**
     * Locale for current language.
     * @return  {@link Locale} object for current language.
     */
    Locale getLocale();
}
