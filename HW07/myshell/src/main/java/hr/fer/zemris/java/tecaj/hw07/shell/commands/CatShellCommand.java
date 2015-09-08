package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommandException;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * Command opens given file and writes its content to console.
 * Command takes one or two arguments. The first argument is path to some file and is mandatory.
 * The second argument is charset name that should be used to interpret chars from bytes.
 * If not provided, a default platform charset is used.
 *
 * @author ilijan
 */
public class CatShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

        String[] args = Utilities.splitArgumentsAndCheckNumber(arguments, 1, 2);
        switch (args.length) {
            case 1:
                printFileContents(env, args[0], Charset.defaultCharset());
                break;
            case 2:
                try {
                    printFileContents(env, args[0], Charset.forName(args[1]));
                } catch (UnsupportedCharsetException ex){
                    throw new IllegalArgumentException("Charset '" + args[1] + "' is not supported.");
                }
                break;
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Prints file contents on environment output.
     * @param env   environment for printing output.
     * @param filename  file to be printed.
     * @param charset   charset to be used.
     * @throws IOException  if printing on environment output is not possible.
     */
    private void printFileContents(Environment env, String filename, Charset charset) throws IOException {

        File file = Utilities.getFileNotDirectory(filename);
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                env.writeln(line);
            }
        } catch (IOException ex) {
            throw new ShellCommandException(ex.getMessage());
        }
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command opens given file and writes its content to console.",
                "Command takes one or two arguments. The first argument is path to some file and is mandatory.",
                "The second argument is charset name that should be used to interpret chars from bytes.",
                "If not provided, a default platform charset is used."
        );
    }
}
