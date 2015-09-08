package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellCommandException;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Command expects a single argument: file name, and produces hex-output with corresponding
 * ASCII translation. For all non-ASCII characters (all bytes whose value is less than 32 or
 * greater than 127) '.' is printed instead
 * hex-output is produced only if source file is not directory.
 *
 * @author ilijan
 */
public class HexdumpShellCommand implements ShellCommand {
    /** Size of buffer dumped in single line. */
    private static final int BUFFER_SIZE = 16;

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

        String[] args = Utilities.splitArgumentsAndCheckNumber(arguments, 1);

        hexdump(env, args[0]);

        return ShellStatus.CONTINUE;
    }

    /**
     * Dumps hex-output of given file to environment output.
     * @param env   environment used for displaying hex-output.
     * @param filename  name of file to be displayed.
     * @throws IOException  if I/O is not possible in environment.
     */
    private void hexdump(Environment env, String filename) throws IOException {

        File file = Utilities.getFileNotDirectory(filename);

        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE)) {

            int address = 0;
            while (true) {
                byte[] inputBuffer = new byte[BUFFER_SIZE];
                int r = inputStream.read(inputBuffer, 0, BUFFER_SIZE);
                if (r < 1) {
                    break;
                }
                env.writeln(bytesToHex(address, inputBuffer));
                address += BUFFER_SIZE;
            }

        } catch (Exception ex) {
            throw new ShellCommandException(ex.getMessage());
        }

    }

    /**
     * Converts array of bytes to string representing single
     * line in hexdump output. Output contains address, bytes converted
     * in hexadecimal format and ASCII representation of supported characters.
     *
     * @param address   address of given array of bytes in file.
     * @param bytes     array of bytes to be dumped.
     * @return          string representation of single line of hexdump output.
     */
    private String bytesToHex(int address, byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%08X: ", address));

        int firstHalf = bytes.length/2;

        for (int i = 0; i < firstHalf; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        sb.append("| ");
        for (int i = firstHalf; i < bytes.length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        sb.append("| ");
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%c", bytes[i] > 32 && bytes[i] < 127 ? (char)bytes[i] : '.'));
        }

        return sb.toString();
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command expects a single argument: file name, and produces hex-output with corresponding",
                "ASCII translation. For all non-ASCII characters (all bytes whose value is less than 32 or",
                "greater than 127) '.' is printed instead.",
                "Hex-output is produced only if source file is not directory."
        );
    }
}
