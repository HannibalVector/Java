package hr.fer.zemris.java.hw12.jvdraw.actions;

import hr.fer.zemris.java.hw12.jvdraw.Drawing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Checks if drawing was modified, asks user to save changes
 * and terminates the application.
 * @author ilijan
 */
public class ExitAction extends AbstractAction {
    /** Current window to be disposed. */
    private JFrame frame;
    /** Current drawing. */
    private Drawing drawing;

    /**
     * Constructor initializes action using references to current drawing
     * and current window.
     * @param frame     current window.
     * @param drawing   current drawing.
     */
    public ExitAction(JFrame frame, Drawing drawing) {
        putValue(NAME, "Exit");
        putValue(MNEMONIC_KEY, KeyEvent.VK_W);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));

        this.frame = frame;
        this.drawing = drawing;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Util.CheckState state = Util.checkIfModified(drawing);
        if (state == Util.CheckState.PROCEED) {
            frame.dispose();
        }
    }
}
