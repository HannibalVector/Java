package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provides localized string for given key and locale object for current language. Implemented as singleton.
 * @author ilijan
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
    /** Single instance of {@link LocalizationProvider}. */
    private static LocalizationProvider instance = new LocalizationProvider();
    /** Resource bundle. */
    private ResourceBundle bundle;
    /** Locale for current laguage. */
    private Locale locale;

    /**
     * Constructor initializes default language to English.
     *
     * */
    private LocalizationProvider() {
        setLanguage("en");
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    /**
     * Returns single instance of {@link LocalizationProvider}.
     * @return  instance of localization provider.
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

    /**
     * Sets provider language to supplied language.
     * @param language  new language.
     */
    public void setLanguage(String language) {
        locale = Locale.forLanguageTag(language);
        bundle =
                ResourceBundle.getBundle(
                        "hr.fer.zemris.java.hw10.jnotepadpp.localization.translation",
                        locale);
        fire();
    }
}
