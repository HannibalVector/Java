package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Command accepts one or two arguments.
 * The first argument is symbol identifier. It can be PROMPTSYMBOL, MORELINESSYMBOL or MULTILINESYMBOL.
 * <ul>
 * <li>PROMPTSYMBOL is character that is printed when user input is expected.</li>
 * <li>MORELINESSYMBOL is character that, if written at the end of the line, extends
 * command to new line.</li>
 * <li>MULTILINESYMBOL is character that is writen at the beggining of line if current line
 * is part of muti-line command.</li>
 * </ul>
 * If second argument is not supplied, command symbol prints current symbol with given identifier.
 * If second argument is supplied, it must be single character. Symbol for given identifier is then
 * set to given character.
 * Usage example:
 *      symbol PROMPTSYMBOL #
 *      symbol MULTILINESYMBOL
 *
 * @author ilijan
 */
public class SymbolShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

        String[] args = Utilities.splitArgumentsAndCheckNumber(arguments, 1, 2);
        switch (args.length) {
            case 1:
                printSymbol(env, args[0]);
                break;
            case 2:
                changeSymbol(env, args[0], args[1]);
                break;
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Changes given environment symbol.
     * @param env   environment in which environment symbol is changed.
     * @param symbolName    symbol identifier.
     * @param newSymbolString   string containing new symbol character.
     * @throws IOException  if I/O is not possible in environment.
     */
    private void changeSymbol(Environment env, String symbolName, String newSymbolString) throws IOException {
        Character currentSymbol;

        if(newSymbolString.length() != 1) {
            throw new IllegalArgumentException("Symbol must be single character.");
        }

        Character newSymbol = newSymbolString.charAt(0);
        switch (symbolName) {
            case "PROMPTSYMBOL":
                currentSymbol = env.getPromptSymbol();
                env.setPromptSymbol(newSymbol);
                break;
            case "MORELINESSYMBOL":
                currentSymbol = env.getMorelinesSymbol();
                env.setMorelinesSymbol(newSymbol);
                break;
            case "MULTILINESYMBOL":
                currentSymbol = env.getMultilineSymbol();
                env.setMultilineSymbol(newSymbol);
                break;
            default:
                throw new IllegalArgumentException("Symbol not found.");
        }

        env.writeln("Symbol for " + symbolName + " changed from '" + currentSymbol + "' to '" + newSymbol + "'");
    }

    /**
     * Prints current environment symbol for given symbol identifier.
     * @param env   environment whose environment symbol is printed.
     * @param symbolName    symbol identifier.
     * @throws IOException  if I/O is not possible in environment.
     */
    private void printSymbol(Environment env, String symbolName) throws IOException {

        Character currentSymbol;
        switch (symbolName) {
            case "PROMPTSYMBOL":
                currentSymbol = env.getPromptSymbol();
                break;
            case "MORELINESSYMBOL":
                currentSymbol = env.getMorelinesSymbol();
                break;
            case "MULTILINESYMBOL":
                currentSymbol = env.getMultilineSymbol();
                break;
            default:
                throw new IllegalArgumentException("Symbol not found.");
        }

        env.writeln("Symbol for " + symbolName + " is '" + currentSymbol + "'");
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command accepts one or two arguments.",
                "The first argument is symbol identifier. It can be PROMPTSYMBOL, MORELINESSYMBOL or MULTILINESYMBOL.",
                "\tPROMPTSYMBOL is character that is printed when user input is expected.",
                "\tMORELINESSYMBOL is character that, if written at the end of the line, extends ",
                "\t\tcommand to new line.",
                "\tMULTILINESYMBOL is character that is writen at the beggining of line if current line",
                "\t\tis part of muti-line command.",
                "If second argument is not supplied, command symbol prints current symbol with given identifier.",
                "If second argument is supplied, it must be single character. Symbol for given identifier is then",
                "set to given character.",
                "\nUsage example:",
                "\tsymbol PROMPTSYMBOL #",
                "\tsymbol MULTILINESYMBOL"
        );
    }
}
