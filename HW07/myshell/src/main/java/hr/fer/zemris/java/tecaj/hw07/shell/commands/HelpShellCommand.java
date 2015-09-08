package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Command can be started without arguments, or with single argument.
 * If started with no arguments, command lists names of all supported commands.
 * If started with single argument, command prints name and the description of selected command.
 *
 * @author ilijan
 */
public class HelpShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
        String[] args = Utilities.splitArgumentsAndCheckNumber(arguments, 0, 1);
        switch (args.length) {
            case 0:
                listSupportedCommands(env);
                break;
            case 1:
                getHelpForCommand(env, args[0]);
                break;
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Lists supported commands in given environment.
     * @param env   environment whose commands are listed.
     * @throws IOException  if I/O is not possible in environment.
     */
    private void listSupportedCommands(Environment env) throws IOException {
        env.writeln("Supported commands:");
        for(ShellCommand command : env.commands()) {
            env.writeln("\t" + command.getCommandName());
        }
    }

    /**
     * Gets help for supplied command.
     * @param env   environment used for getting command and communicating with user.
     * @param name  command name.
     * @throws IOException  if I/O is not possible in environment.
     */
    private void getHelpForCommand(Environment env, String name) throws IOException {
        ShellCommand command = env.getCommand(name);

        String commandName = command.getCommandName();
        List<String> commandDescription = command.getCommandDescription();

        env.writeln("Command name:\n\t" + commandName);
        env.writeln("Command description:");

        for (String line : commandDescription) {
            env.writeln("\t" + line);
        }
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command can be started without arguments, or with single argument.",
                "If started with no arguments, command lists names of all supported commands.",
                "If started with single argument, command prints name and the description of selected command."
        );
    }
}
