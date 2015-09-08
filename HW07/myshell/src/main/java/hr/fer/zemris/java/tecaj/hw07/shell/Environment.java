package hr.fer.zemris.java.tecaj.hw07.shell;

import java.io.IOException;

/**
 * Shell environment used for communication between {@link ShellCommand} commands and user.
 * @author ilijan
 */
public interface Environment {
    /**
     * Reads line from environment input. When user input is expected PROMPTSYMBOL is printed. Input finishes
     * when user inputs newline character, except if MORELINESSYMBOL is entered at the end of the line. Then input
     * extends to new line, which is indicated by MUTILINESSYMBOL. Given symbols can be
     * returned by calling methods {@link #getPromptSymbol()}, {@link #getMorelinesSymbol()}
     * and {@link #getMultilineSymbol()}.
     *
     * @return line read from environment
     input.
     * @throws IOException  if reading from environment
     input is not possible.
     */
    String readLine() throws IOException;

    /**
     * Writes {@code String} to environment output.
     * @param text  text to be written.
     * @return  text to be written.
     * @throws IOException  if writing to environment
     output is not possible.
     */
    String write(String text) throws IOException;

    /**
     * Writes {@code} String to environment output and adds newline character at the end.
     * @param text  text to be written in a new line.
     * @return  text to be written.
     * @throws IOException  if writing to environment
     output is not possible.
     */
    String writeln(String text) throws IOException;

    /**
     * Returns {@code Iterable} object of all available commands in the environment.
     * @return {@code Iterable} object of all available commands in the environment.
     */
    Iterable<ShellCommand> commands();

    /**
     * Gives {@link ShellCommand} command for given command name.
     * @param name  name of the requested command.
     * @return  command of type {@link ShellCommand} of given name.
     */
    ShellCommand getCommand(String name);

    /**
     * Gets MULTILINESYMBOL character, written at the beginning of line in which user input is expected,
     * if previus line finished with MORELINESSYMBOL. Symbol notifies user that current line is
     * part of multi-line command.
     * @return  MULTILINESYMBOL character.
     */
    Character getMultilineSymbol();

    /**
     * Sets MULTILINESYMBOL character, written at the beginning of line in which user input is expected,
     * if previus line finished with MORELINESSYMBOL.
     * @param symbol new character for MULTILINESYMBOL.
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Gets PROMPTSYMBOL character, written at the beginning of line in which user input is expected.
     * @return  PROMPTSYMBOL character.
     */
    Character getPromptSymbol();

    /**
     * Sets PROMPTSYMBOL character, written at the beginning of line in which user input is expected.
     * @param symbol new PROMPTSYMBOL character.
     */
    void setPromptSymbol(Character symbol);

    /**
     * Gets MORELINESSYMBOL character, which when written at the end of the input line enables user
     * to extend input to more lines.
     * @return  MORELINESSYMBOL character.
     */
    Character getMorelinesSymbol();

    /**
     * Sets MORELINESSYMBOL character, which when written at the end of the input line enables user
     * to extend input to more lines.
     * @param symbol new MORELINESSYMOL character.
     */
    void setMorelinesSymbol(Character symbol);

    /**
     * Initializes environment by reading PROMPTSYMBOL, MORELINESSYMBOL and MULTILINESYMOL from given
     * settings file.
     * @param settingsFilename filename for settings file.
     */
    void initialize(String settingsFilename);

    /**
     *  Closes environment, but saves current PROMPTSYMBOL, MORELINESSYMBOL and MULTILINESYMBOL to settings
     *  file provided to method {@link #initialize(String)}
     */
    void close();
}
