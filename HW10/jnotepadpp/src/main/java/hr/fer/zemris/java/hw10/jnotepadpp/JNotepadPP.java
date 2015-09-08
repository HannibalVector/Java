package hr.fer.zemris.java.hw10.jnotepadpp;

import hr.fer.zemris.java.hw10.jnotepadpp.localization.FormLocalizationProvider;
import hr.fer.zemris.java.hw10.jnotepadpp.localization.LJMenu;
import hr.fer.zemris.java.hw10.jnotepadpp.localization.LocalizableAction;
import hr.fer.zemris.java.hw10.jnotepadpp.localization.LocalizationProvider;

import javax.swing.*;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Simple text editor with support for working with multiple tabs.
 * @author ilijan
 */
public class JNotepadPP extends JFrame{

    /** Pane that contains all tabs with opened documents. */
    private JTabbedPane tabs;
    /** Instance of class that implements all actions. */
    private JNotepadPPActions actions;
    /** Editor of currently active tab. */
    private JTextArea activeEditor;
    /** Caret listener used for controlling commands that can only be executed when selection is made. */
    private CaretListener caretListener;

    /** Status bar at the bottom of the screen. */
    private StatusBar statusBar;

    /** List of components that are linked to commands that can only be executed when the selection is made. */
    private java.util.List<JComponent> editingComponents;

    /** Form localization provider. */
    private FormLocalizationProvider localizationProvider;

    /** Window title. */
    private static final String TITLE = "JNotepad++";

    /**
     * Constructor initializes new {@code JNotepadPP} by initializing its basic components.
     */
    public JNotepadPP() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        tabs = new JTabbedPane();

        localizationProvider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
        actions = new JNotepadPPActions(this, tabs, localizationProvider);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                actions.exitSafely();
            }
        });

        editingComponents = new ArrayList<>();

        setTitle(TITLE);
        setSize(800, 600);

        initGUI();
    }

    /**
     * Initializes graphic user interface.
     */
    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabs, BorderLayout.CENTER);

        statusBar = new StatusBar(localizationProvider);
        getContentPane().add(statusBar, BorderLayout.PAGE_END);

        caretListener = e -> {
            toggleEditingMenus();
        };

        tabs.addChangeListener(e -> {
            if (tabs.getTabCount() == 0) {
                JNotepadPP.this.setTitle(TITLE);
                changeActiveEditor(null);

                return;
            }
            DocumentTab selectedDocument = actions.getSelectedDocument();
            Path file = selectedDocument.getFilePath();
            if (file != null) {
                JNotepadPP.this.setTitle(file.toString() + " - " + TITLE);
            } else {
                JNotepadPP.this.setTitle(selectedDocument.getName() + " - " + TITLE);
            }
            changeActiveEditor(selectedDocument.getEditor());
        });

        createMenus();
        createToolbars();
        setEditingMenusEnabled(false);
    }

    /**
     * Enables or disables menus linked to commands that can only be executed when the selection is made.
     */
    private void toggleEditingMenus() {
        if (activeEditor != null && activeEditor.getCaret().getDot() != activeEditor.getCaret().getMark()) {
            setEditingMenusEnabled(true);
        } else {
            setEditingMenusEnabled(false);
        }
    }

    /**
     * Enables or disables menus linked to commands that can only be executed when the selection is made.
     * @param enabled desired value.
     */
    private void setEditingMenusEnabled(boolean enabled) {
        for (JComponent component : editingComponents) {
            component.setEnabled(enabled);
        }
    }

    /**
     * Changes active editor to supplied editor.
     * @param editor    editor to be set as active.
     */
    private void changeActiveEditor(JTextArea editor) {
        if (activeEditor != null) {
            activeEditor.removeCaretListener(caretListener);
        }
        activeEditor = editor;
        if (editor != null) {
            activeEditor.addCaretListener(caretListener);
        }

        statusBar.setCurrentEditor(editor);
        toggleEditingMenus();
    }

    /**
     * Creates all toolbars.
     */
    private void createToolbars() {
        JToolBar toolbar = new JToolBar();

        toolbar.add(new JButton(actions.newDocument));
        toolbar.add(new JButton(actions.openDocument));
        toolbar.add(new JButton(actions.saveCurrentTab));
        toolbar.add(new JButton(actions.saveCurrentTabAs));

        toolbar.addSeparator();
        toolbar.add(new JButton(actions.cut));
        toolbar.add(new JButton(actions.copy));
        toolbar.add(new JButton(actions.paste));

        toolbar.addSeparator();
        toolbar.add(new JButton(actions.statisticalInfo));

        toolbar.addSeparator();
        toolbar.add(new JButton(actions.closeCurrentTab));
        toolbar.add(new JButton(actions.exitAction));

        getContentPane().add(toolbar, BorderLayout.PAGE_START);
    }

    /**
     * Creates menus.
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new LJMenu("file", localizationProvider);
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(actions.newDocument));
        fileMenu.add(new JMenuItem(actions.openDocument));
        fileMenu.add(new JMenuItem(actions.saveCurrentTab));
        fileMenu.add(new JMenuItem(actions.saveCurrentTabAs));

        fileMenu.addSeparator();

        fileMenu.add(new JMenuItem(actions.closeCurrentTab));
        fileMenu.add(new JMenuItem(actions.exitAction));

        JMenu editMenu = new LJMenu("edit", localizationProvider);
        menuBar.add(editMenu);
        editMenu.add(new JMenuItem(actions.cut));
        editMenu.add(new JMenuItem(actions.copy));
        editMenu.add(new JMenuItem(actions.paste));
        editMenu.addSeparator();
        editMenu.add(actions.statisticalInfo);

        JMenu languageMenu = new LJMenu("languages", localizationProvider);
        menuBar.add(languageMenu);

        languageMenu.add(new JMenuItem(new LocalizableAction("english", localizationProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("en");
            }
        }));

        languageMenu.add(new JMenuItem(new LocalizableAction("german", localizationProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("de");
            }
        }));

        languageMenu.add(new JMenuItem(new LocalizableAction("croatian", localizationProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("hr");
            }
        }));

        JMenu toolsMenu = new LJMenu("tools", localizationProvider);
        menuBar.add(toolsMenu);
        JMenu changeCase = new LJMenu("changeCase", localizationProvider);
        toolsMenu.add(changeCase);
        changeCase.add(new JMenuItem(actions.toLowerCase));
        changeCase.add(new JMenuItem(actions.toUpperCase));
        changeCase.add(new JMenuItem(actions.toggleCase));
        editingComponents.add(changeCase);

        JMenu sort = new LJMenu("sort", localizationProvider);
        toolsMenu.add(sort);
        sort.add(new JMenuItem(actions.sortAscending));
        sort.add(new JMenuItem(actions.sortDescending));
        editingComponents.add(sort);

        toolsMenu.add(new JMenuItem(actions.unique));

        setJMenuBar(menuBar);
    }

    /**
     * The main method for application {@link JNotepadPP}.
     * @param args  command-line arguments. Not used anywhere.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
    }
}
