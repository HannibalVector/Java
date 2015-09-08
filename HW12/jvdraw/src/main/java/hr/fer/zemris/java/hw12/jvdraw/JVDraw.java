package hr.fer.zemris.java.hw12.jvdraw;

import hr.fer.zemris.java.hw12.jvdraw.actions.*;
import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.ObjectType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.UIManager.setLookAndFeel;

/**
 * {@code JVDraw} is a simple GUI application for drawing vector graphics. Application supports opening and
 * saving drawings in .jvd format as well as exporting drawings to raster formats .png, .jpg and .gif.
 * @author ilijan
 */
public class JVDraw extends JFrame {

    /** Drawing canvas. */
    private JDrawingCanvas canvas;
    /** Vector drawing. */
    private Drawing drawing;
    /** Default foreground color. */
    public static final Color DEFAULT_FOREGROUND = Color.BLACK;
    /** Default background color. */
    public static final Color DEFAULT_BACKGROUND = Color.WHITE;
    /** Window title. */
    public static final String TITLE = "JVDraw";

    /**
     * Constructor sets title and close operation and initializes GUI.
     * @throws HeadlessException    Thrown when code that is dependent on a keyboard, display,
     *                              or mouse is called in an environment that does not support
     *                              a keyboard, display, or mouse.
     */
    public JVDraw() throws HeadlessException {
        setTitle(TITLE);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        initGUI();
    }

    /**
     * Initializes graphical user interface.
     */
    private void initGUI() {
        setLayout(new BorderLayout());

        drawing =  new Drawing();
        drawing.addDrawingModificationListener(drawing -> {
            String fileName;
            if (drawing.getFilename().isPresent()) {
                fileName = drawing.getFilename().get().toString();
            } else {
                fileName = "New Drawing";
            }
            if (drawing.isModified()) {
                setTitle(TITLE + " - " + fileName + " [modified]");
            } else {
                setTitle(TITLE + " - " + fileName);
            }
        });

        canvas = new JDrawingCanvas(drawing);
        getContentPane().add(canvas, BorderLayout.CENTER);

        JPanel listPanel = new JPanel(new BorderLayout());
        JGeometricList list = new JGeometricList(drawing);
        listPanel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel listOptions = new JPanel();
        listOptions.add(new JButton(new RaiseAction(list, drawing)));
        listOptions.add(new JButton(new LowerAction(list, drawing)));
        listOptions.add(new JButton(new DeleteAction(list, drawing)));

        listPanel.add(listOptions, BorderLayout.PAGE_END);

        getContentPane().add(listPanel, BorderLayout.LINE_END);

        createMenus();
        createToolbar();

        pack();
    }

    /**
     * Creates toolbars.
     */
    private void createToolbar() {
        JToolBar toolbar = new JToolBar();
        JColorArea foregroundColorArea = new JColorArea(
                "foreground",
                DEFAULT_FOREGROUND,
                canvas
        );

        JColorArea backgroundColorArea = new JColorArea(
                "background",
                DEFAULT_BACKGROUND,
                canvas
        );

        toolbar.add(foregroundColorArea);
        toolbar.add(Box.createRigidArea(new Dimension(5, 0)));
        toolbar.add(backgroundColorArea);
        toolbar.add(Box.createRigidArea(new Dimension(5, 0)));
        toolbar.addSeparator();

        getContentPane().add(new JColorInfo(foregroundColorArea, backgroundColorArea),
                BorderLayout.PAGE_END);

        ButtonGroup objectChooser = new ButtonGroup();
        for (ObjectType type : ObjectType.values()) {
            JToggleButton button = new JToggleButton(type.toString());
            button.addActionListener(l -> canvas.setNextObjectType(type));
            objectChooser.add(button);
            toolbar.add(button);
        }

        getContentPane().add(toolbar, BorderLayout.PAGE_START);
    }

    /**
     * Creates menus.
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(new ClearAction(drawing)));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(new OpenAction(drawing)));
        fileMenu.add(new JMenuItem(new AppendAction(drawing)));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(new SaveAction(drawing)));
        fileMenu.add(new JMenuItem(new SaveAsAction(drawing)));
        fileMenu.add(new JMenuItem(new ExportAction(drawing)));
        fileMenu.addSeparator();
        fileMenu.add(new ExitAction(this, drawing));
        menuBar.add(fileMenu);

        JMenu themesMenu = new JMenu("Themes");
        for(UIManager.LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
            AbstractAction setTheme = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        setLookAndFeel(lafi.getClassName());
                        SwingUtilities.updateComponentTreeUI(JVDraw.this);
                    } catch (Exception ignorable) {
                    }
                }
            };
            setTheme.putValue(Action.NAME, lafi.getName());
            themesMenu.add(new JMenuItem(setTheme));
        }
        menuBar.add(themesMenu);

        setJMenuBar(menuBar);
    }

    /**
     * The main method for the application {@link JVDraw}.
     * @param args  command-line arguments. Not used anywhere.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JVDraw().setVisible(true));
    }
}
