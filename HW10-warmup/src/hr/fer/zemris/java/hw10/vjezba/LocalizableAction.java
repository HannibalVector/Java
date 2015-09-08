package hr.fer.zemris.java.hw10.vjezba;

import javax.swing.*;

/**
 * @author ilijan
 */
public abstract class LocalizableAction extends AbstractAction {
    private String key;
    private ILocalizationProvider provider;

    public LocalizableAction(String key, ILocalizationProvider provider) {
        this.key = key;
        this.provider = provider;
        provider.addLocalizationListener(LocalizableAction.this::updateLocalizedName);
        updateLocalizedName();
    }

    private void updateLocalizedName() {
        putValue(NAME, provider.getString(key));
    }
}
