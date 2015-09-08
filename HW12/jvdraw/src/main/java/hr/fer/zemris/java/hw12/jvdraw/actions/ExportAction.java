package hr.fer.zemris.java.hw12.jvdraw.actions;

import hr.fer.zemris.java.hw12.jvdraw.Drawing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Exports drawing to rasterized image.
 * @author ilijan
 */
public class ExportAction extends AbstractAction {
    /** Current drawing. */
    private Drawing drawing;

    /**
     * Constructor initializes action using reference to current drawing.
     * @param drawing   current drawing.
     */
    public ExportAction(Drawing drawing) {
        this.drawing = drawing;

        putValue(NAME, "Export");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Util.save(drawing, Util.Mode.EXPORT);
    }
}
