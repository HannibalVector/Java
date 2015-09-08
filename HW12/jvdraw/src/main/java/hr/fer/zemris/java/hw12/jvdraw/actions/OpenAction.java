package hr.fer.zemris.java.hw12.jvdraw.actions;

import hr.fer.zemris.java.hw12.jvdraw.Drawing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Action opens {@link JFileChooser} dialogue and loads all geometrical objects in file
 * to the current drawing. If current drawing was modified user is asked to
 * save changes before opening new drawing.
 *
 * @author ilijan
 */
public class OpenAction extends AbstractAction {
    /** Current drawing to which objects are loaded. */
    private Drawing drawing;

    /**
     * Constructor initializes action using reference to current drawing.
     * @param drawing   current drawing.
     */
    public OpenAction(Drawing drawing) {
        this.drawing = drawing;

        putValue(NAME, "Open");
        putValue(MNEMONIC_KEY, KeyEvent.VK_O);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Util.CheckState state = Util.checkIfModified(drawing);
        if (state == Util.CheckState.PROCEED) {
            Util.open(drawing, Util.Mode.OPEN);
        }
    }
}
