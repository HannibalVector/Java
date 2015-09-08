package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class used for parsing command arguments from given string, and checking if number
 * of provided arguments is compatible with called command.
 * @author ilijan
 */
public class Utilities {

    /**
     * Method parses command arguments from given string and checks if number of provided arguments is
     * allowed. Arguments can be separated by empty space, or wrapped in double quotes, in which case
     * spaces are regarded as parts of argument string.
     * If length of provided arguments is not allowed, {@link IllegalArgumentException} is thrown.
     *
     * @param arguments string containing arguments.
     * @param allowedLengths allowed lengths of arguments.
     * @return  array of strings representing arguments for shell command.
     */
    public static String[] splitArgumentsAndCheckNumber(String arguments, int ... allowedLengths) {

        if (arguments.isEmpty()) {
            for (int l : allowedLengths) {
                if (l == 0) {
                    return new String[]{};
                }
            }
            throw new IllegalArgumentException("No arguments supplied.");
        }

        List<String> argsList = new ArrayList<>();

        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(arguments);
        while (m.find())
            argsList.add(m.group(1).replace("\"", ""));

        String[] args = new String[argsList.size()];
        args = argsList.toArray(args);

        for (int l : allowedLengths) {
            if (args.length == l) {
                return args;
            }
        }

        throw new IllegalArgumentException("Invalid number of arguments.");
    }

    /**
     * Checks if file with given filename exists and that it is not a directory.
     * @param filename  file to be checked.
     * @return  file for given filename.
     */
    public static File getFileNotDirectory(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            throw new IllegalArgumentException("File " + filename + " does not exist.");
        }

        if (file.isDirectory()) {
            throw new IllegalArgumentException(filename + " is a directory.");
        }
        return file;
    }

    /**
     * Checks if file with given filename exists and that it is a directory.
     * @param directoryFilename file to be checked.
     * @return  directory with given filename.
     */
    public static File getDirectoryNotFile(String directoryFilename) {
        File directory = new File(directoryFilename);

        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory " + directoryFilename + " does not exist.");
        }

        if(!directory.isDirectory()) {
            throw new IllegalArgumentException(directoryFilename + " is not directory.");
        }
        return directory;
    }
}
