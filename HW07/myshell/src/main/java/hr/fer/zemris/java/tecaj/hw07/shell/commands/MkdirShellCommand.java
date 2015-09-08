package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommandException;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Command takes a single argument: directory name, and creates the appropriate directory structure.
 *
 * @author ilijan
 */
public class MkdirShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args = Utilities.splitArgumentsAndCheckNumber(arguments, 1);

        File newDirectory = new File(args[0]);
        boolean isCreationSuccessful = newDirectory.mkdirs();
        if (!isCreationSuccessful) {
            throw new ShellCommandException("Cannot create directory structure.");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command takes a single argument: directory name, and creates the appropriate directory",
                "structure."
        );
    }
}
