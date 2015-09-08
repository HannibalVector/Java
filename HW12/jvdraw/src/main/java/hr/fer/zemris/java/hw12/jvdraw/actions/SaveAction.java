package hr.fer.zemris.java.hw12.jvdraw.actions;

import hr.fer.zemris.java.hw12.jvdraw.Drawing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Saves drawing to vector graphics file. If drawing was not loaded from file
 * user is asked to choose new file in which drawing will be saved.
 * @author ilijan
 */
public class SaveAction extends AbstractAction {
    /** Current drawing. */
    private Drawing drawing;

    /**
     * Constructor initializes action using reference to current drawing.
     * @param drawing   current drawing.
     */
    public SaveAction(Drawing drawing) {
        this.drawing = drawing;

        putValue(NAME, "Save");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Util.save(drawing, Util.Mode.SAVE);
    }
}
