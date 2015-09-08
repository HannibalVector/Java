package hr.fer.zemris.java.hw10.jnotepadpp;

import hr.fer.zemris.java.hw10.jnotepadpp.localization.FormLocalizationProvider;
import hr.fer.zemris.java.hw10.jnotepadpp.localization.LocalizableAction;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class implementing all actions needed in application {@link JNotepadPP}.
 * @author ilijan
 */
public class JNotepadPPActions {

    /** Main frame of the application. */
    JNotepadPP appWindow;
    /** Panel holding tabs with documents. */
    JTabbedPane tabs;
    /** Localization provider. */
    FormLocalizationProvider lp;

    /**
     * Constructor initializes all variables in new {@link JNotepadPPActions} object using provided parameters.
     * @param appWindow     main application frame.
     * @param tabs          panel holding tabs.
     * @param localizationProvider  localization provider.
     */
    public JNotepadPPActions(JNotepadPP appWindow, JTabbedPane tabs, FormLocalizationProvider localizationProvider) {
        this.appWindow = appWindow;
        this.tabs = tabs;
        lp = localizationProvider;
        createActions();
    }

    /** Action that creates new blank document and puts it in a new tab. */
    protected Action newDocument;
    /** Action opens existing document. */
    protected Action openDocument;
    /** Action safely exits application by checking documents for modification before closing. */
    protected Action exitAction;
    /** Action closes current tab. */
    protected Action closeCurrentTab;
    /** Action saves current tab. */
    protected Action saveCurrentTab;
    /** Action saves current tab in new file. */
    protected Action saveCurrentTabAs;
    /** Action generates statistics about document. */
    protected Action statisticalInfo;
    /** Action copies selected text. */
    protected Action copy;
    /** Action cuts selected text. */
    protected Action cut;
    /** Action pastes selected text. */
    protected Action paste;
    /** Action converts selected text to upper case. */
    protected Action toUpperCase;
    /** Action converts selected text to lower case. */
    protected Action toLowerCase;
    /** Action toggles case for selected text. */
    protected Action toggleCase;
    /** Action sorts selected lines in ascending order. */
    protected Action sortAscending;
    /** Action sorts selected lines in descending order. */
    protected Action sortDescending;
    /** Action removes duplicate lines in selection. */
    protected Action unique;

    /**
     * Creates all actions.
     */
    private void createActions() {

        newDocument = new LocalizableAction("new", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                DocumentTab newDocumentTab = new DocumentTab();
                newDocumentTab.addDocumentModifiedListener(new DocumentModifiedListener(newDocumentTab));
                tabs.addTab(newDocumentTab.getName(), DocumentTab.UNMODIFIED_ICON, newDocumentTab, newDocumentTab.getName());
                tabs.setSelectedComponent(newDocumentTab);
            }
        };
        newDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        newDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

        openDocument = new LocalizableAction("open", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Open file");
                if (fc.showOpenDialog(appWindow) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                Path filePath = fc.getSelectedFile().toPath();

                if (!Files.isReadable(filePath)) {
                    JOptionPane.showMessageDialog(
                            appWindow,
                            "Chosen file (" + filePath + ") is not readable.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {

                    String fileString = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
                    DocumentTab newDocumentTab = new DocumentTab(filePath, fileString);
                    newDocumentTab.addDocumentModifiedListener(new DocumentModifiedListener(newDocumentTab));
                    tabs.addTab(newDocumentTab.getName(), DocumentTab.UNMODIFIED_ICON, newDocumentTab, filePath.toString());
                    tabs.setSelectedComponent(newDocumentTab);

                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(
                            appWindow,
                            "Error while reading file (" + filePath + "): " + e1.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        };
        openDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        openDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

        exitAction = new LocalizableAction("exit", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitSafely();
            }
        };
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ESCAPE);

        saveCurrentTab = new LocalizableAction("save", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentTab(SaveMode.SAVE);
            }
        };
        saveCurrentTab.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveCurrentTab.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

        saveCurrentTabAs = new LocalizableAction("saveAs", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentTab(SaveMode.SAVE_AS);
            }
        };
        saveCurrentTabAs.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
        saveCurrentTabAs.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

        closeCurrentTab = new LocalizableAction("close", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                DocumentTab selectedDocument = getSelectedDocument();
                if (selectedDocument == null) {
                    return;
                }
                if (selectedDocument.isModified()) {
                    int answer = JOptionPane.showConfirmDialog(
                            appWindow,
                            "File " + selectedDocument.getName() + " is modified.\n" +
                                    "Do you want to save the file before closing?",
                            "Warning",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (answer == JOptionPane.CANCEL_OPTION) {
                        return;
                    } else if (answer == JOptionPane.YES_OPTION) {
                        saveCurrentTab(SaveMode.SAVE);
                    }
                }
                tabs.removeTabAt(tabs.getSelectedIndex());
            }
        };
        closeCurrentTab.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
        closeCurrentTab.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);

        statisticalInfo = new LocalizableAction("statInfo", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                DocumentTab selectedDocument = getSelectedDocument();
                if (selectedDocument == null) {
                    return;
                }
                JTextArea currentEditor = selectedDocument.getEditor();
                int numberOfCharacters = currentEditor.getDocument().getLength();;
                int numberOfLines = currentEditor.getLineCount();

                final String SPACES = " \t\n";
                int nonSpaceCharacters = 0;

                char[] textArray = currentEditor.getText().toCharArray();
                for (int i = 0; i < textArray.length; i++) {
                    if (!SPACES.contains(String.valueOf(textArray[i]))) {
                        nonSpaceCharacters++;
                    }
                }

                JOptionPane.showMessageDialog(
                        appWindow,
                        String.format("Your document has %d characters, %d non-blank characters and %d lines.",
                                numberOfCharacters, nonSpaceCharacters, numberOfLines),
                        "Statistical Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        };
        statisticalInfo.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
        statisticalInfo.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);

        copy = new LocalizableAction("copy", lp) {
            Action defaultCopyAction = new DefaultEditorKit.CopyAction();
            @Override
            public void actionPerformed(ActionEvent e) {
                defaultCopyAction.actionPerformed(e);
            }
        };
        copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        copy.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

        cut = new LocalizableAction("cut", lp) {
            Action defaultCopyAction = new DefaultEditorKit.CutAction();
            @Override
            public void actionPerformed(ActionEvent e) {
                defaultCopyAction.actionPerformed(e);
            }
        };
        cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        cut.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

        paste = new LocalizableAction("paste", lp) {
            Action defaultCopyAction = new DefaultEditorKit.PasteAction();
            @Override
            public void actionPerformed(ActionEvent e) {
                defaultCopyAction.actionPerformed(e);
            }
        };
        paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        paste.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);

        toggleCase = new LocalizableAction("toggleCase", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                transformSelection(JNotepadPPActions::toggleCase);
            }
        };

        toUpperCase = new LocalizableAction("toUpperCase", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                transformSelection(String::toUpperCase);
            }
        };

        toLowerCase = new LocalizableAction("toLowerCase", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                transformSelection(String::toLowerCase);
            }
        };

        sortAscending = new LocalizableAction("sortAscending", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                transformLinesSelection(lines -> sortStringArray(lines,
                        (s1, s2) -> Collator.getInstance(lp.getLocale()).compare(s1, s2)));
            }
        };

        sortDescending = new LocalizableAction("sortDescending", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                transformLinesSelection(lines -> sortStringArray(lines,
                        (s1, s2) -> -1 * Collator.getInstance(lp.getLocale()).compare(s1, s2)));
            }
        };

        unique = new LocalizableAction("unique", lp) {
            @Override
            public void actionPerformed(ActionEvent e) {
                transformLinesSelection(lines -> {

                    List<String> list = Arrays.asList(lines);
                    List<String> list2 = list.stream().distinct().collect(Collectors.toList());

                    return list2.toArray(new String[list2.size()]);
                });
            }
        };
    }

    /**
     * Transforms selected string using provided transformation.
     * @param transformation    transformation used to transform selected text.
     */
    private void transformSelection(Function<String, String> transformation) {
        DocumentTab selectedDocument = getSelectedDocument();
        if (selectedDocument == null) {
            return;
        }

        JTextArea editor = selectedDocument.getEditor();
        int selectionStart = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
        int selectionEnd = Math.max(editor.getCaret().getDot(), editor.getCaret().getMark()) - selectionStart;

        Document doc = editor.getDocument();
        try {
            String selection = doc.getText(selectionStart, selectionEnd);
            doc.remove(selectionStart, selectionEnd);
            doc.insertString(selectionStart, transformation.apply(selection), null);
        } catch (BadLocationException ignorable) {
        }
    }

    /**
     * Transforms selected lines using provided transformation.
     * @param transformation transformation used to transform lines.
     */
    private void transformLinesSelection(Function<String[], String[]> transformation) {
        DocumentTab selectedDocument = getSelectedDocument();
        if (selectedDocument == null) {
            return;
        }

        JTextArea editor = selectedDocument.getEditor();
        int selectionStart = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
        int selectionEnd = Math.max(editor.getCaret().getDot(), editor.getCaret().getMark()) - selectionStart;

        Document doc = editor.getDocument();
        try {
            int startingLine = editor.getLineOfOffset(selectionStart);
            int endingLine = editor.getLineOfOffset(selectionEnd);

            selectionStart = editor.getLineStartOffset(startingLine);
            selectionEnd = editor.getLineEndOffset(endingLine);

            String selection = doc.getText(selectionStart, selectionEnd);
            doc.remove(selectionStart, selectionEnd);

            String[] lines = selection.split("\n");
            String[] transformedLines = transformation.apply(lines);

            StringBuilder sb = new StringBuilder();

            for (String line : transformedLines) {
                sb.append(line).append("\n");
            }

            doc.insertString(selectionStart, sb.toString(), null);
        } catch (BadLocationException ignorable) {
        }
    }

    /**
     * Sorts array of strings.
     * @param lines array to be sorted.
     * @param comparator    comparator to be used for sorting.
     * @return
     */
    private String[] sortStringArray(String[] lines, Comparator<String> comparator) {
        Arrays.sort(lines, comparator);
        return lines;
    }

    /**
     * Toggles case of selected text.
     * @param selection {@code String} containing text to be transformed.
     * @return  text with toggled case.
     */
    private static String toggleCase(String selection) {
        char[] znakovi = selection.toCharArray();
        for (int i = 0; i < znakovi.length; i++) {
            char c = znakovi[i];
            if (Character.isLowerCase(c)) {
                znakovi[i] = Character.toUpperCase(c);
            } else if (Character.isUpperCase(c)) {
                znakovi[i] = Character.toLowerCase(c);
            }
        }
        return new String(znakovi);
    }

    /**
     * Listener which observes document for modification and changes its modification state
     * and displayed icon accordingly.
     */
    public class DocumentModifiedListener implements DocumentListener {

        /**
         * Observed document.
         */
        private DocumentTab document;

        /**
         * Constructior initializes new listener using provided document.
         * @param document  document to be observed.
         */
        public DocumentModifiedListener(DocumentTab document) {
            this.document = document;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            setModifiedAndUpdateIcon();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setModifiedAndUpdateIcon();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setModifiedAndUpdateIcon();
        }

        /**
         * Sets modification state of document and changes displayed icon accordingly.
         */
        private void setModifiedAndUpdateIcon() {
            document.setModified(true);
            updateTabIcon(document);
        }
    }

    /**
     * Updates tab icon for supplied document.
     * @param document  document for which the tab icon will be updated.
     */
    protected void updateTabIcon(DocumentTab document) {
        if (document.isModified()) {
            tabs.setIconAt(tabs.indexOfComponent(document), DocumentTab.MODIFIED_ICON);
        } else {
            tabs.setIconAt(tabs.indexOfComponent(document), DocumentTab.UNMODIFIED_ICON);
        }
    }

    /**
     * Exits application safely by checking all opened documents for modification beforehand.
     */
    protected void exitSafely() {
        for (int i = 0, numberOfTabs = tabs.getTabCount(); i < numberOfTabs; i++) {
            DocumentTab selectedDocument = (DocumentTab)tabs.getComponentAt(i);
            if (selectedDocument.isModified()) {
                tabs.setSelectedIndex(i);
                int answer = JOptionPane.showConfirmDialog(
                        appWindow,
                        "File " + selectedDocument.getName() + " is modified.\n" +
                                "Do you want to save the file before closing?",
                        "Warning",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (answer == JOptionPane.CANCEL_OPTION) {
                    return;
                } else if (answer == JOptionPane.YES_OPTION) {
                    saveCurrentTab(SaveMode.SAVE);
                }
            }
        }

        appWindow.dispose();
    }

    /**
     * Gets selected document.
     * @return  document in currently active tab.
     */
    protected DocumentTab getSelectedDocument() {
        if (tabs.getTabCount() == 0) {
            return null;
        }
        return (DocumentTab)tabs.getComponentAt(tabs.getSelectedIndex());
    }

    /**
     * Saves document in current tab.
     * @param mode  {@link hr.fer.zemris.java.hw10.jnotepadpp.JNotepadPPActions.SaveMode} determining if file should be
     *                     saved in previously used file or in new file.
     */
    protected void saveCurrentTab(SaveMode mode) {
        DocumentTab selectedDocument = getSelectedDocument();
        if (selectedDocument == null) {
            return;
        }
        if (selectedDocument.getFilePath() == null || mode == SaveMode.SAVE_AS) {
            JFileChooser fc = new JFileChooser();

            if (mode == SaveMode.SAVE_AS) {
                fc.setDialogTitle("Save " + selectedDocument.getName() + " as");
            } else {
                fc.setDialogTitle("Save " + selectedDocument.getName());
            }

            if (fc.showSaveDialog(appWindow) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            Path file = fc.getSelectedFile().toPath();

            if (Files.exists(file)) {
                int rez = JOptionPane.showConfirmDialog(
                        appWindow,
                        "Chosen file (" + file + ") already exists. Are you sure you want to overwrite it?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (rez != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            selectedDocument.setFilePath(file);
            int index = tabs.indexOfComponent(selectedDocument);
            tabs.setTitleAt(index, selectedDocument.getName());
            tabs.setToolTipTextAt(index, file.toString());
        }

        try {
            Files.write(selectedDocument.getFilePath(), selectedDocument.getEditor().getText().getBytes(StandardCharsets.UTF_8));
            selectedDocument.setModified(false);
            updateTabIcon(selectedDocument);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(
                    appWindow,
                    "Error while writing file (" + selectedDocument.getFilePath()  + "): " + e1.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Determines saving mode.
     */
    enum SaveMode {
        SAVE, SAVE_AS
    }
}
