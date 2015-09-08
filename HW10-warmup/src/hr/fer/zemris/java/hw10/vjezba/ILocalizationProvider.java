package hr.fer.zemris.java.hw10.vjezba;

/**
 * @author ilijan
 */
public interface ILocalizationProvider {

    void addLocalizationListener(ILocalizationListener listener);
    void removeLocalizationListener(ILocalizationListener listener);
    String getString(String key);
}
