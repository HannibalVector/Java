package hr.fer.zemris.java.hw12.jvdraw.actions;

import hr.fer.zemris.java.hw12.jvdraw.Drawing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Clears current drawing, after asking user to save changes.
 * @author ilijan
 */
public class ClearAction extends AbstractAction {
    /** Current drawing. */
    private Drawing drawing;

    /**
     * Constructor initializes action using reference to current drawing.
     * @param drawing   current drawing.
     */
    public ClearAction(Drawing drawing) {
        this.drawing = drawing;

        putValue(NAME, "Clear");
        putValue(MNEMONIC_KEY, KeyEvent.VK_C);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Util.CheckState state = Util.checkIfModified(drawing);
        if (state == Util.CheckState.PROCEED) {
            drawing.clear();
            drawing.setModified(false);
        }
    }

}
