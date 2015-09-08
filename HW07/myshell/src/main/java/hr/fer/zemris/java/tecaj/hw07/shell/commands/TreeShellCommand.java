package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

/**
 * Command expects a single argument: directory name and recursively prints a tree
 * of all contained files and subdirectories.
 *
 * @author ilijan
 */
public class TreeShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

        String[] args = Utilities.splitArgumentsAndCheckNumber(arguments, 1);
        File directory = Utilities.getDirectoryNotFile(args[0]);
        Files.walkFileTree(directory.toPath(), new IndentedOutput(env));

        return ShellStatus.CONTINUE;
    }

    /**
     * Implements file visitor which recursively prints filenames of contained
     * files and subdirectories in given directory, using indentation to indicate
     * level in directory tree.
     */
    private static class IndentedOutput implements FileVisitor<Path> {
        /** Environment for printing filenames */
        private Environment env;
        /** Current depth of indentation. */
        private int depth;
        /** Indicator printed before each filename. */
        private static final String FILE_INDICATOR = "├ ";
        /** File indicator length. */
        private static final int FILE_INDICATOR_LENGTH = FILE_INDICATOR.length();

        /**
         * Constructor initializes visitor with given environment.
         * @param env environment used for I/O.
         */
        public IndentedOutput(Environment env) {
            this.env = env;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            printFilename(dir);
            depth += FILE_INDICATOR_LENGTH;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            printFilename(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            depth -= FILE_INDICATOR_LENGTH;
            return FileVisitResult.CONTINUE;
        }

        /**
         * Prints indented filename on environment output.
         * @param file  file for which filename is printed.
         * @throws IOException  if output is not possible in environment.
         */
        private void printFilename(Path file) throws IOException {
            String format = "%" + (depth + FILE_INDICATOR_LENGTH) + "s%s";
            env.writeln(String.format(format, FILE_INDICATOR, file.getFileName()));
        }
    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command expects a single argument: directory name and recursively prints a tree",
                "of all contained files and subdirectories."
        );
    }
}
