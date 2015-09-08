package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommandException;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Command takes a single argument – directory – and writes a directory listing (not recursive).
 * The output consists of 4 columns:
 * <ul>
 * <li>1st column indicates if current object is directory (d),
 * readable (r),  writable (w) and executable (x);</li>
 * <li>2nd column contains object size in bytes;</li>
 * <li>3rd column contains file creation date/time;</li>
 * <li>4th column contains file name.</li>
 * </ul>
 *
 * @author ilijan
 */
public class LsShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

        String[] args = Utilities.splitArgumentsAndCheckNumber(arguments, 1);
        listFiles(env, args[0]);

        return ShellStatus.CONTINUE;
    }

    /**
     * Lists files from given directory on given environment output.
     * @param env environment for communication with user.
     * @param directoryFilename directory for listing files.
     * @throws IOException  if I/O is not possible in environment.
     */
    private void listFiles(Environment env, String directoryFilename) throws IOException {
        File directory = Utilities.getDirectoryNotFile(directoryFilename);
        File[] children = directory.listFiles();

        for (File file : children) {
            env.writeln(getAttributes(file));
        }
    }

    /**
     * Gets file attributes (as described in documentation of class {@link LsShellCommand})
     * and returns them as string.
     * @param file file for which attributes are returned.
     * @return file attributes.
     */
    private String getAttributes(File file) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            BasicFileAttributeView faView = Files.getFileAttributeView(
                    file.toPath(), BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
            );
            BasicFileAttributes attributes = faView.readAttributes();
            FileTime fileTime = attributes.creationTime();

            long fileSize = attributes.size();
            String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

            String isDirectory = file.isDirectory() ? "d" : "-";
            String isReadable = file.canRead() ? "r" : "-";
            String isWriteable = file.canWrite() ? "w" : "-";
            String isExecuteable = file.canExecute() ? "e" : "-";

            return String.format("%s%s%s%s %10d %s %s",
                    isDirectory, isReadable, isWriteable, isExecuteable,
                    fileSize, formattedDateTime, file.getName()
            );

        } catch (Exception ex) {
            throw new ShellCommandException("Error while listing files. " + ex.getMessage());
        }
    }


    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command takes a single argument – directory – and writes a directory listing (not recursive).",
                "The output consists of 4 columns:",
                "\t1st column indicates if current object is directory (d),",
                "\t\treadable (r),  writable (w) and executable (x);",
                "\t2nd column contains object size in bytes;",
                "\t3rd column contains file creation date/time;",
                "\t4th column contains file name."
        );
    }
}
