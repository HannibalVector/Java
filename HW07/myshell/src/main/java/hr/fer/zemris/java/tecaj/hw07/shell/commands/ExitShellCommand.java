package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Command that closes console.
 *
 * @author ilijan
 */
public class ExitShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        if (!arguments.isEmpty()) {
            throw new IllegalArgumentException("No arguments are expected.");
        }

        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command closes console."
        );
    }
}