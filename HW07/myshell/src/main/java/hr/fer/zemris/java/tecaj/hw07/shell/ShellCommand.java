package hr.fer.zemris.java.tecaj.hw07.shell;

import java.io.IOException;
import java.util.List;

/**
 * Represents abstract shell command.
 * @author ilijan
 */
public interface ShellCommand {
    /**
     * Contains all the work done by executing command.
     * @param env   environment for communication with user.
     * @param arguments arguments provided by user.
     * @return  shell status of type {@link ShellStatus} indicating
     * weather shell should continue with work, or terminate.
     * @throws IOException if reading or writing to environment
     * input/output is not possible.
     */
    ShellStatus executeCommand(Environment env, String arguments) throws IOException;

    /**
     * Gets command name.
     * @return command name.
     */
    String getCommandName();

    /**
     * Gets command description contained in list of strings which
     * represent lines of description.
     * @return  list of lines of description of command.
     */
    List<String> getCommandDescription();
}
