package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import javax.swing.*;

/**
 * Localizable version of {@link AbstractAction}.
 * @author ilijan
 */
public abstract class LocalizableAction extends AbstractAction {
    /** Key for obtaining translations. */
    private String key;
    /** Localization provider. */
    private ILocalizationProvider provider;

    /**
     * Constructor initializes action using key and localization provider for obtaining translations.
     * @param key   key for obtaining name.
     * @param provider  localization provider.
     */
    public LocalizableAction(String key, ILocalizationProvider provider) {
        this.key = key;
        this.provider = provider;
        provider.addLocalizationListener(LocalizableAction.this::updateLocalizedName);
        updateLocalizedName();
    }

    /**
     * Updates localized name upon localization change.
     */
    private void updateLocalizedName() {
        putValue(NAME, provider.getString(key));
        putValue(SHORT_DESCRIPTION, provider.getString(key + "_description"));
    }
}
