package hr.fer.zemris.java.tecaj.hw07.shell;

import java.io.*;
import java.util.Map;

/**
 * Implements shell environment for reading from standard input and writing to standard output.
 * @author ilijan
 */
public class EnvironmentImpl implements Environment {

    /** Map of all available commands, stored by command name as key. */
    private Map<String, ShellCommand> commands;

    /** Filename for settings file. */
    public String settingsFilename;

    /** Symbol written when user is prompted for input. */
    private Character PROMPTSYMBOL = '>';
    /** When written user is enabled to enter command in more lines. */
    private Character MORELINESSYMBOL = '!';
    /** Notifies user that current line is part of multi-line command. */
    private Character MULTILINESYMBOL = '|';

    /** Environment reader used for all user input. */
    private BufferedReader reader;
    /** Environment writer used for all output to user. */
    private BufferedWriter writer;

    /**
     * Constructor takes map of commands and settings file.
     * @param commands  map of commands.
     * @param settingsFilename  filename of settings file.
     */
    public EnvironmentImpl(Map<String, ShellCommand> commands, String settingsFilename) {
        this.commands = commands;
        this.settingsFilename = settingsFilename;
        initialize(settingsFilename);

        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
    }

    @Override
    public void initialize(String settingsFilename) {
        File settingsFile = new File(settingsFilename);
        if (!settingsFile.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(
                                new FileInputStream(settingsFile)),"UTF-8"))
             ) {

            PROMPTSYMBOL = br.readLine().charAt(0);
            MORELINESSYMBOL = br.readLine().charAt(0);
            MULTILINESYMBOL = br.readLine().charAt(0);

        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    @Override
    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        while(true) {
            String line = reader.readLine();
            sb.append(line);
            if (!line.endsWith(String.valueOf(MORELINESSYMBOL))) {
                break;
            }
            sb.replace(sb.length() - 1, sb.length(), "");
            write(MULTILINESYMBOL + " ");
        }
        return sb.toString();
    }

    @Override
    public String write(String text) throws IOException {
        writer.write(text);
        writer.flush();
        return text;
    }

    @Override
    public String writeln(String text) throws IOException {
        writer.write(text);
        writer.newLine();
        writer.flush();
        return text;
    }

    @Override
    public Iterable<ShellCommand> commands() {
        return commands.values();
    }

    @Override
    public ShellCommand getCommand(String name) {
        if (!commands.containsKey(name)) {
            throw new IllegalArgumentException("Command not found.");
        }
        return commands.get(name);
    }

    @Override
    public Character getMultilineSymbol() {
        return MULTILINESYMBOL;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        MULTILINESYMBOL = symbol;
    }

    @Override
    public Character getPromptSymbol() {
        return PROMPTSYMBOL;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        PROMPTSYMBOL = symbol;
    }

    @Override
    public Character getMorelinesSymbol() {
        return MORELINESSYMBOL;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        MORELINESSYMBOL = symbol;
    }

    @Override
    public void close() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(
                        new BufferedOutputStream(
                                new FileOutputStream(settingsFilename)),"UTF-8"))
        ) {

            bw.write(PROMPTSYMBOL);
            bw.newLine();
            bw.write(MORELINESSYMBOL);
            bw.newLine();
            bw.write(MULTILINESYMBOL);
            bw.newLine();

        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
}
