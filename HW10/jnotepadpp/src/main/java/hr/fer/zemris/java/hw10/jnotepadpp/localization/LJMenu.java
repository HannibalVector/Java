package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import javax.swing.*;

/**
 * Localizable version of {@link JMenu}.
 * @author ilijan
 */
public class LJMenu extends JMenu {
    /** Key for obtaining translations. */
    private String key;
    /** Localization provider. */
    private ILocalizationProvider provider;

    /**
     * Constructor initializes menu using key and localization provider for obtaining translations.
     * @param key   key for obtaining name.
     * @param provider  localization provider.
     */
    public LJMenu(String key, ILocalizationProvider provider) {
        this.key = key;
        this.provider = provider;
        provider.addLocalizationListener(LJMenu.this::updateLocalizedName);
        updateLocalizedName();
    }

    /**
     * Updates localized name upon localization change.
     */
    private void updateLocalizedName() {
        setText(provider.getString(key));
    }
}
