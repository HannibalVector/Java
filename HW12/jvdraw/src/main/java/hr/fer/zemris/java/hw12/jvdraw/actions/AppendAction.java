package hr.fer.zemris.java.hw12.jvdraw.actions;

import hr.fer.zemris.java.hw12.jvdraw.Drawing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Action opens {@link JFileChooser} dialogue and appends all geometrical objects in file
 * to the current drawing.
 *
 * @author ilijan
 */
public class AppendAction extends AbstractAction {
    /** Current drawing to which objects are appended. */
    private Drawing drawing;

    /**
     * Constructor initializes action using reference to current drawing.
     * @param drawing   current drawing.
     */
    public AppendAction(Drawing drawing) {
        this.drawing = drawing;

        putValue(NAME, "Append");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Util.open(drawing, Util.Mode.APPEND);
    }
}
