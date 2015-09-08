package hr.fer.zemris.java.hw12.jvdraw.actions;

import hr.fer.zemris.java.hw12.jvdraw.Drawing;
import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.GeometricObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Raises z-index of currently selected list items by one.
 * @author ilijan
 */
public class RaiseAction extends AbstractAction {
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
    public RaiseAction(JList list, Drawing drawing) {
        putValue(NAME, "Raise");
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        putValue(ACCELERATOR_KEY, KeyEvent.VK_UP);

        this.list = list;
        this.drawing = drawing;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selected = list.getSelectedIndices();
        List<GeometricObject> selectedObjects = list.getSelectedValuesList();

        list.clearSelection();
        Arrays.sort(selected);

        for (int i = selected.length - 1; i >= 0; i--) {
            drawing.raiseObject(selected[i]);
        }

        List<Integer> newIndicesList = selectedObjects.stream().map(drawing::indexOf).collect(Collectors.toList());
        int[] newSelection = new int[newIndicesList.size()];
        int i = 0;
        for (int index : newIndicesList) {
            newSelection[i] = index;
            i++;
        }

        list.setSelectedIndices(newSelection);
    }
}
