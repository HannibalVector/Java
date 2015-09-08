package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommandException;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * Command expects two arguments: source file name and destination file name (i.e. paths and names).
 * Source (the first argument) can only be file, not directory.
 * If the destination (the second argument) is directory, source will be copied in that directory
 * using the original file name.
 *
 * @author ilijan
 */
public class CopyShellCommand implements ShellCommand {
    /** Size of buffer used for copying. */
    private static final int BUFFER_SIZE = 4096;

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

        String[] args = Utilities.splitArgumentsAndCheckNumber(arguments, 2);
        String sourceFilename = args[0];
        String destinationFilename = args[1];

        File source = new File(sourceFilename);
        File destination = new File(destinationFilename);
        checkSourceAndDestination(source, destination);

        if (destination.isDirectory()) {
            destination = new File(destinationFilename + "/" + sourceFilename);
        }

        if (destination.exists() && !isOverwritingAllowed(env)) {
            return ShellStatus.CONTINUE;
        }

        copy(source, destination);

        return ShellStatus.CONTINUE;
    }

    /**
     * Checks source and destination file. If source does not exist, or source and destination
     * are the same file, or if source is a directory throws {@link IllegalArgumentException}.
     * @param source    source file to be checked.
     * @param destination   destination file to be checked.
     */
    private void checkSourceAndDestination(File source, File destination)
    {
        if (!source.exists()) {
            throw new IllegalArgumentException("Source (" + source.getName() + ") does not exist.");
        }
        if (source.isDirectory()) {
            throw new IllegalArgumentException(source.getName() + " is a directory. Source cannot be directory.");
        }
        try {
            if (Files.isSameFile(source.toPath(), destination.toPath())) {
                throw new IllegalArgumentException("Source (" + source.getName() + ") and destination (" + destination.getName() + ") are the same.");
            }
        } catch (IOException ignorable) {}
    }

    /**
     * In case destination file exists, asks user if overwriting is allowed.
     * @param env   environment for communication with user.
     * @return  {@code true} if overwriting is allowed, {@code false} otherwise.
     * @throws IOException  if I/O is not possible on environment.
     */
    private boolean isOverwritingAllowed(Environment env) throws IOException {
        env.write("Destination file already exists. Overwrite? (y/n) ");
        while (true) {
            String answer = env.readLine();
            if(answer.toLowerCase().equals("y")) return true;
            else if (answer.toLowerCase().equals("n")) return false;
            else env.writeln("Invalid input. Repeat entry.");
        }
    }

    /**
     * Copies sourceFile to destinationFIle.
     * @param sourceFile    file to be copied.
     * @param destinationFile   destination for copying source file.
     */
    private void copy(File sourceFile, File destinationFile) {
        try (InputStream inputStream = new BufferedInputStream(
                new FileInputStream(sourceFile), BUFFER_SIZE);

             OutputStream outputStream = new BufferedOutputStream(
                     new FileOutputStream(destinationFile), BUFFER_SIZE)
        ) {

            while (true) {
                byte[] inputBuffer = new byte[BUFFER_SIZE];
                int r = inputStream.read(inputBuffer, 0, BUFFER_SIZE);
                if (r < 1) {
                    break;
                }
                outputStream.write(inputBuffer, 0, r);
            }

        } catch (Exception ex) {
            throw new ShellCommandException(ex.getMessage());
        }
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command expects two arguments: source file name and destination file name (i.e. paths and names).",
                "Source (the first argument) can only be file, not directory.",
                "If the destination (the second argument) is directory, source will be copied in that directory",
                "using the original file name."
        );
    }
}
