package hr.fer.zemris.java.hw10.jnotepadpp;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Represents tabbed document used in {@link JNotepadPP} editor.
 * @author ilijan
 */
public class DocumentTab extends JScrollPane {
    /** Icon used if document was modified and not saved. */
    public static final Icon MODIFIED_ICON = new ImageIcon("diskette_red.gif");
    /** Icon used if document was not modified after last save. */
    public static final Icon UNMODIFIED_ICON = new ImageIcon("diskette_green.gif");

    /** Number of new files. Used for generating generic document titles. */
    private static int numberOfNewFiles;

    /** Path to file containing document. */
    private Path filePath;
    /** Marks if document was modified. */
    private boolean modified;
    /** Editor containing document. */
    private JTextArea editor;

    /**
     * Constructor initializes new {@link DocumentTab} to new empty document.
     */
    public DocumentTab() {
        editor = new JTextArea();
        initDocumentTab();
    }

    /**
     * Constructor initializes new {@link DocumentTab} from file.
     * @param filePath  file containing document.
     * @param fileString    fileString read from file.
     */
    public DocumentTab(Path filePath, String fileString) {
        this.filePath = filePath;
        editor = new JTextArea(fileString);
        initDocumentTab();
    }

    /**
     * Initializes new {@link DocumentTab}.
     */
    private void initDocumentTab() {
        this.setViewportView(editor);
        updateName();
    }

    /**
     * Adds new listener that observes if document is modified.
     * @param listener listener to be added.
     */
    public void addDocumentModifiedListener(JNotepadPPActions.DocumentModifiedListener listener){
        editor.getDocument().addDocumentListener(listener);
    }

    /**
     * File path getter.
     * @return  file path.
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * File path setter.
     * @param path file path to be set.
     */
    public void setFilePath(Path path) {
        this.filePath = path;
        updateName();
    }

    /**
     * Getter for document editor.
     * @return  document editor ({@link JTextArea} object).
     */
    public JTextArea getEditor() {
        return editor;
    }

    /**
     * Getter for modification state.
     * @return {@code true} if document was modified, {@code false} otherwise.
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Setter for the modification state.
     * @param isModified    value to be set.
     */
    public void setModified(boolean isModified) {
        modified = isModified;
    }

    /**
     * Updates the name of document.
     */
    public void updateName() {
        String name;
        if (filePath == null) {
            numberOfNewFiles++;
            name = "New document " + numberOfNewFiles;
        } else {
            name = filePath.getFileName().toString();
        }

        super.setName(name);
    }
}
