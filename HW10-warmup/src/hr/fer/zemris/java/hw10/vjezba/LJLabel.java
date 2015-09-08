package hr.fer.zemris.java.hw10.vjezba;

import javax.swing.*;

/**
 * @author ilijan
 */
public class LJLabel extends JLabel {
    private String key;
    private ILocalizationProvider provider;

    public LJLabel(String key, ILocalizationProvider provider) {
        this.key = key;
        this.provider = provider;
        provider.addLocalizationListener(LJLabel.this::updateLocalizedName);
        updateLocalizedName();
    }

    private void updateLocalizedName() {
        setText(provider.getString(key));
    }
}
