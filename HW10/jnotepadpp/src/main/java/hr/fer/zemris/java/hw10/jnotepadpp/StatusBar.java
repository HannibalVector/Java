package hr.fer.zemris.java.hw10.jnotepadpp;

import hr.fer.zemris.java.hw10.jnotepadpp.localization.FormLocalizationProvider;
import hr.fer.zemris.java.hw10.jnotepadpp.localization.LJLabel;

import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Status bar for the application {@link JNotepadPP}.
 * @author ilijan
 */
public class StatusBar extends JPanel {
    /** Editor of currently active tab. */
    private JTextArea currentEditor;
    /** Label containing document length. */
    private JLabel docLengthLabel;
    /** Label containing caret line position. */
    private JLabel caretLineLabel;
    /** Label containing caret column position. */
    private JLabel caretColumnLabel;
    /** Label containing caret selection length. */
    private JLabel caretSelectionLabel;

    /** Label containing clock. */
    private JLabel clockLabel;
    /** Localization provider. */
    private FormLocalizationProvider lp;

    /**
     * Constructor initializes new {@link StatusBar} using provided localization provider.
     * @param lp    localization provider to be used.
     */
    public StatusBar(FormLocalizationProvider lp) {
        this.lp = lp;

        setLayout(new GridLayout(1, 3));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        JPanel docLengthPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        docLengthPanel.add(new LJLabel("length", lp));
        docLengthLabel = new JLabel();
        docLengthPanel.add(docLengthLabel);


        caretLineLabel = new JLabel();
        caretColumnLabel = new JLabel();
        caretSelectionLabel = new JLabel();

        JPanel caretPositionPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        caretPositionPanel.add(new LJLabel("line", lp));
        caretPositionPanel.add(caretLineLabel);
        caretPositionPanel.add(new LJLabel("column", lp));
        caretPositionPanel.add(caretColumnLabel);
        caretPositionPanel.add(new LJLabel("selection", lp));
        caretPositionPanel.add(caretSelectionLabel);

        clockLabel = new JLabel();


        caretPositionPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));
        clockLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));
        clockLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        add(docLengthPanel);
        add(caretPositionPanel);
        add(clockLabel);

        update();
        startClock();
    }

    /**
     * Updates status bar.
     */
    public void update() {
        String lengthString = "--";
        String lineNumberString = "--";
        String columnNumberString = "--";
        String selectionLengthString = "--";

        if (currentEditor != null) {
            int caretPosition = currentEditor.getCaretPosition();
            int selectionLength = Math.abs(currentEditor.getCaret().getDot() - currentEditor.getCaret().getMark());

            try {
                int lineNumber = currentEditor.getLineOfOffset(caretPosition);
                int columnNumber = caretPosition - currentEditor.getLineStartOffset(lineNumber);
                lineNumber++;

                lengthString = String.valueOf(currentEditor.getDocument().getLength());
                lineNumberString = String.valueOf(lineNumber);
                columnNumberString = String.valueOf(columnNumber);
                selectionLengthString = String.valueOf(selectionLength);

            } catch (BadLocationException ignorable) { }

        }

        docLengthLabel.setText(lengthString);
        caretLineLabel.setText(lineNumberString);
        caretColumnLabel.setText(columnNumberString);
        caretSelectionLabel.setText(selectionLengthString);
    }

    /**
     * Caret listener which observes caret changes.
     */
    private CaretListener caretListener = e -> StatusBar.this.update();

    /**
     * Sets current editor to provided {@link JTextArea} object.
     * @param editor    provided editor.
     */
    public void setCurrentEditor(JTextArea editor) {
        if (currentEditor != null) {
            currentEditor.removeCaretListener(caretListener);
        }
        currentEditor = editor;
        if (editor != null) {
            editor.addCaretListener(caretListener);
        }
        update();
    }

    /**
     * Starts clock.
     */
    private void startClock() {
        Thread t = new Thread(() ->
        {
            while (true) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                clockLabel.setText(formatter.format(new Date()));
                clockLabel.repaint();

                try { Thread.sleep(500); } catch (Exception ignorable) { }
            }
        }
        );

        t.setDaemon(true);
        t.start();
    }
}
