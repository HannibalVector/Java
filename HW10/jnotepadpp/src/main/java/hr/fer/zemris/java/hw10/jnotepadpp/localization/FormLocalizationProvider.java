package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Localization provider that registers itself automatically on observed {@link JFrame} form.
 * Enables automatic registration and deregistration when window is opened or closed.
 * @author ilijan
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

    /**
     * Initializes new {@link FormLocalizationProvider} using bridged provider and
     * supplied frame.
     * @param provider  provider to be bridged.
     * @param frame     frame to register at.
     */
    public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
        super(provider);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                connect();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }
        });
    }
}
