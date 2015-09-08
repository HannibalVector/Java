package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Command takes no arguments and lists names of supported charsets.
 * A single charset name is written per line.
 *
 * @author ilijan
 */
public class CharsetsShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

        if (!arguments.isEmpty()) {
            throw new IllegalArgumentException("No arguments are expected.");
        }

        for (String charset : Charset.availableCharsets().keySet()) {
            env.writeln(charset);
        }
        return null;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command takes no arguments and lists names of supported charsets.",
                "A single charset name is written per line."
        );
    }
}
