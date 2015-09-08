package hr.fer.zemris.java.hw12.jvdraw;

import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.GeometricObject;
import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.Property;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * List of geometric objects contained in vector drawing.
 * @author ilijan
 */
public class JGeometricList extends JList<GeometricObject> {

    /**
     * List is initialized using vector drawing whose object it lists.
     * @param drawing   drawing containing all the geometric objects.
     */
    public JGeometricList(Drawing drawing) {
        super(new DrawingObjectListModel(drawing));
        addMouseListener(mouseAdapter);
    }

    /**
     * Mouse adapter implementing action performed when list item is double-clicked.
     */
    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                GeometricObject selected = getSelectedValue();
                JOptionPane.showConfirmDialog(
                        null,
                        getPropertiesPanel(selected),
                        "Properties for " + selected.getName() + ":",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
            }
        }
    };

    /**
     * Constructs properties panel (of type {@link JPanel}) to be shown in {@link JOptionPane}.
     * Panel enables modification of all {@link GeometricObject} properties which are defined in
     * {@link GeometricObject#properties}.
     * @param object    geometric object whose properties are listed.
     * @return          {@link JPanel} listing geometric object properties.
     */
    private static JPanel getPropertiesPanel(GeometricObject object) {
        List<Property> properties = object.getProperties();
        JPanel panel = new JPanel(new GridLayout(properties.size(), 2));

        for (Property property : object.getProperties()) {
            panel.add(new JLabel(property.getName()));
            panel.add(getProperyPanel(property));
        }

        return panel;
    }

    /**
     * Constructs {@link JPanel} containing fields or other components used for modifying
     * single geometric object property, represented by type {@link Property}.
     * @param property  {@link Property} to be represented.
     * @return          {@link JPanel} used for modifying given property.
     */
    private static JPanel getProperyPanel(Property property) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        Object value = property.get();

        if (value instanceof String) {
            JTextField textField = new JTextField((String)value);

            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    property.set(textField.getText());
                }
            });

            panel.add(textField);

        } else if (value instanceof Point) {
            Point point = (Point)value;
            JTextField xField = new JTextField(String.valueOf(point.x));
            JTextField yField = new JTextField(String.valueOf(point.y));

            FocusListener changePoint = new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    try {
                    property.set(
                            new Point(Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText())));
                    } catch (NumberFormatException ex) {
                        showInvalidInputMessage(property.getName());
                    }
                }
            };

            xField.addFocusListener(changePoint);
            yField.addFocusListener(changePoint);

            panel.add(new JLabel("x: "));
            panel.add(xField);
            panel.add(new JLabel(" y: "));
            panel.add(yField);
        } else if (value instanceof Integer) {
            JTextField field = new JTextField(String.valueOf(value));

            field.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    try {
                        property.set(Integer.parseInt(field.getText()));
                    } catch (NumberFormatException ex) {
                        showInvalidInputMessage(property.getName());
                    }
                }
            });

            panel.add(field);
        } else if (value instanceof Color) {
            Color oldColor = (Color)value;
            JColorArea colorArea = new JColorArea(property.getName(), oldColor,
                    (source, oldColor1, newColor) -> property.set(newColor));

            panel.add(colorArea);
        }

        return panel;
    }

    /**
     * Error shown if illegal value is attempted to be set for property with given name.
     * @param input name of the property with invalid value.
     */
    private static void showInvalidInputMessage(String input) {
        JOptionPane.showMessageDialog(
                null,
                "Invalid input for property: " + input,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
