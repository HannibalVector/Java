package hr.fer.zemris.java.tecaj.hw07.shell;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Implements simple command-line shell.
 *
 * @author ilijan
 */
public class MyShell {
    /** Settings file for initialization of environment. */
    private static final String SETTINGS_FILE = "settings.txt";
    /** Shell environment used for user input and output. */
    private static Environment environment;

    /**
     * Main method for {@link MyShell} application. No input arguments are expected.
     * @param args  command-line arguments. Not used anywhere.
     */
    public static void main(String[] args) {
        environment = buildEnvironment();
        try {
            printGreetingMessage();

            while (true) {
                try {
                    environment.write(String.valueOf(environment.getPromptSymbol()) + " ");
                    String line = environment.readLine();
                    String commandName = getCommandName(line);
                    String commandArgs = getCommandArgs(line);
                    ShellCommand command = environment.getCommand(commandName);
                    ShellStatus status = command.executeCommand(environment, commandArgs);
                    if (status == ShellStatus.TERMINATE) {
                        break;
                    }
                } catch (IllegalArgumentException | ShellCommandException ex) {
                    environment.writeln(ex.getMessage());
                }
            }

            printByeByeMessage();
            environment.close();

        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    /**
     * Gets command name from string representing line of text, or throws
     * {@link IllegalArgumentException} if string is empty.
     * @param line  line of text to extract command name from.
     * @return  command name.
     */
    private static String getCommandName(String line) {
        Scanner sc = new Scanner(line);
        if (!sc.hasNext()) {
            throw new IllegalArgumentException("No command name.");
        }
        return sc.next();
    }

    /**
     * Gets command arguments from line of text. If line only contains command
     * returns an empty string.
     * @param line line of text to extract command arguments from.
     * @return  command arguments.
     */
    private static String getCommandArgs(String line) {
        Scanner sc = new Scanner(line);
        sc.next();
        sc.useDelimiter("\n");
        if(sc.hasNext()) {
            return sc.next().trim();
        } else {
            return "";
        }
    }

    /**
     * Initializes shell environment. Fills map of commands with supported
     * commands, and passes settings file to environment constructor.
     * @return  initialized shell environment.
     */
    private static Environment buildEnvironment() {
        HashMap<String, ShellCommand> commands = new HashMap<>();

        commands.put("symbol", new SymbolShellCommand());
        commands.put("charsets", new CharsetsShellCommand());
        commands.put("cat", new CatShellCommand());
        commands.put("ls", new LsShellCommand());
        commands.put("tree", new TreeShellCommand());
        commands.put("copy", new CopyShellCommand());
        commands.put("mkdir", new MkdirShellCommand());
        commands.put("hexdump", new HexdumpShellCommand());
        commands.put("exit", new ExitShellCommand());
        commands.put("help", new HelpShellCommand());

        return new EnvironmentImpl(commands, SETTINGS_FILE);
    }

    /**
     * Prints greeting message on environment output.
     * @throws IOException if writing to environment output is not possible.
     */
    private static void printGreetingMessage() throws IOException{
        environment.writeln("Welcome to MyShell v 1.0");
    }

    /**
     * Prints goodbye message on environment output.
     * @throws IOException if writing to environment output is not possible.
     */
    private static void printByeByeMessage() throws IOException{
        environment.writeln("Bye, bye!");
    }


}
