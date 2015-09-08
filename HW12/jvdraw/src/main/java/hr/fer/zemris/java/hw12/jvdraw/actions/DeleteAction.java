package hr.fer.zemris.java.hw12.jvdraw.actions;

import hr.fer.zemris.java.hw12.jvdraw.Drawing;
import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.GeometricObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Deletes currently selected list items from drawing.
 * @author ilijan
 */
public class DeleteAction extends AbstractAction {
    /** List containing geometric objects. */
    private JList list;
    /** Current drawing containing geometric objects. */
    private Drawing drawing;

    /**
     * Constructor initializes action using reference to current drawing
     * and list of all objects contained in current drawing.
     *
     * @param list      list of objects in current drawing.
     * @param drawing   current drawing.
     */
    public DeleteAction(JList list, Drawing drawing) {
        putValue(NAME, "Delete");
        putValue(MNEMONIC_KEY, KeyEvent.VK_DELETE);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("del"));

        this.list = list;
        this.drawing = drawing;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<GeometricObject> selected = list.getSelectedValuesList();
        selected.forEach(object -> drawing.remove(object));
    }
}
