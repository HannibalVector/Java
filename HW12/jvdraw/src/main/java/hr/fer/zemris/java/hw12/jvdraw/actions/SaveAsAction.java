package hr.fer.zemris.java.hw12.jvdraw.actions;

import hr.fer.zemris.java.hw12.jvdraw.Drawing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Saves drawing to new vector graphics file.
 * @author ilijan
 */
public class SaveAsAction extends AbstractAction {
    /** Current drawing. */
    private Drawing drawing;

    /**
     * Constructor initializes action using reference to current drawing.
     * @param drawing   current drawing.
     */
    public SaveAsAction(Drawing drawing) {
        this.drawing = drawing;

        putValue(NAME, "Save As");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Util.save(drawing, Util.Mode.SAVE_AS);
    }
}
