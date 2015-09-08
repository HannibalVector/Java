package hr.fer.zemris.java.hw12.jvdraw.actions;

import hr.fer.zemris.java.hw12.jvdraw.Drawing;
import hr.fer.zemris.java.hw12.jvdraw.DrawingException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class offering common methods shared by multiple actions.
 * @author ilijan
 */
public class Util {

    /** Modes for reading and writing files. */
    enum Mode {
        /** Clears drawing and loads objects. */
        OPEN("Open file", extensionFilter),

        /** Appends objects to current drawing. */
        APPEND("Append file", extensionFilter),

        /** Saves current drawing in existing file. */
        SAVE("Save drawing", extensionFilter),

        /** Saves current drawing in a new file. */
        SAVE_AS("Save drawing as", extensionFilter),

        /** Exports current drawing to raster image. */
        EXPORT("Export drawing as", exportFilter);

        /** {@link JFileChooser} window title for the given mode. */
        private String title;
        /** Supported file extensions filter for the given mode. */
        private FileNameExtensionFilter filter;

        /**
         * Constructor initializes new mode using window title to be displayed
         * and filter determining supported file extensions.
         * @param title     window title.
         * @param filter    supported file extensions.
         */
        Mode(String title, FileNameExtensionFilter filter) {
            this.title = title;
            this.filter = filter;
        }
    }

    /** Marks state of check for drawing modification. */
    enum CheckState {
        /** Operation that triggered check should abort. */
        ABORT,
        /** Operation that triggered check can proceed. */
        PROCEED
    }

    /**
     * Opens file and loads drawing into current drawing using provided mode.
     * @param drawing   current drawing.
     * @param mode      opening mode.
     */
    public static void open(Drawing drawing, Mode mode) {
        JFileChooser fc = new JFileChooser();

        fc.setDialogTitle(mode.title);
        fc.addChoosableFileFilter(mode.filter);

        if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path filePath = fc.getSelectedFile().toPath();

        if (!Files.isReadable(filePath)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Chosen file (" + filePath + ") is not readable.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String fileString = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            if (mode == Mode.OPEN) {
                drawing.clear();
                drawing.setFilename(filePath);
            }
            drawing.addFromString(fileString);
            if (mode == Mode.OPEN) {
                drawing.setModified(false);
            }

        } catch (IOException e1) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error while reading file (" + filePath + "): " + e1.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Saves current drawing using provided mode.
     * @param drawing   current drawing.
     * @param mode      saving mode.
     */
    public static void save(Drawing drawing, Mode mode) {
        Path file = null;
        if (!drawing.getFilename().isPresent() || mode != Mode.SAVE) {
            JFileChooser fc = new JFileChooser();

            fc.setDialogTitle(mode.title);
            fc.addChoosableFileFilter(mode.filter);

            if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            file = fc.getSelectedFile().toPath();
            if (mode == Mode.SAVE || mode == Mode.SAVE_AS
                    && !file.endsWith(EXTENSION)) {
                file = Paths.get(file + EXTENSION);
            }

            if (Files.exists(file)) {
                int rez = JOptionPane.showConfirmDialog(
                        null,
                        "Chosen file (" + file + ") already exists. Are you sure you want to overwrite it?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (rez != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            drawing.setFilename(file);
        }

        try {
            if (mode == Mode.SAVE || mode == Mode.SAVE_AS) {
                Files.write(drawing.getFilename().get(), drawing.generateString().getBytes(StandardCharsets.UTF_8));
                drawing.setModified(false);
            } else if (mode == Mode.EXPORT) {
                try {
                    BufferedImage image = drawing.rasterize();
                    String fileName = file.getFileName().toString();
                    String extension = fileName.substring(fileName.indexOf(".") + 1);
                    ImageIO.write(image, extension, file.toFile());
                } catch (DrawingException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Cannot export empty image.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error while writing file (" + drawing.getFilename()  + "): " + e1.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Checks current drawing for modification and returns check state.
     * @param drawing   current drawing.
     * @return          check state.
     */
    public static CheckState checkIfModified(Drawing drawing) {
        if (drawing.isModified()) {
            int answer = JOptionPane.showConfirmDialog(
                    null,
                    "Drawing is modified.\n" +
                            "Do you want to save the drawing before closing?",
                    "Warning",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (answer == JOptionPane.CANCEL_OPTION) {
                return CheckState.ABORT;
            } else if (answer == JOptionPane.YES_OPTION) {
                save(drawing, Mode.SAVE);
            }
        }
        return CheckState.PROCEED;
    }

    /** Vector graphics file extension. */
    private static final String EXTENSION = ".jvd";

    /** File extension filter for vector graphics file. */
    private static FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("JVDraw vector drawing.", "jvd");

    /** File extension filter for raster image files used for exporting. */
    private static FileNameExtensionFilter exportFilter = new FileNameExtensionFilter("Export to .png, .gif or .jpg.", "png", "gif", "jpg");
}
