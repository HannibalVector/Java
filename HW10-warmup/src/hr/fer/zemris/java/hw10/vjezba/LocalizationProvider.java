package hr.fer.zemris.java.hw10.vjezba;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author ilijan
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    private static LocalizationProvider instance = new LocalizationProvider();
    private ResourceBundle bundle;

    private LocalizationProvider() {
        setLanguage("en");
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    public static LocalizationProvider getInstance() {
        return instance;
    }

    public void setLanguage(String language) {
        Locale locale = Locale.forLanguageTag(language);
        bundle =
                ResourceBundle.getBundle("hr.fer.zemris.java.hw10.vjezba.prijevodi", locale);
        fire();
    }
}
