package hr.fer.zemris.java.tecaj.hw5.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * A simple student database emulator. Entries are expected to be stored in file "database.txt".
 * It is a simple textual form in which each row contains the data for single student.
 * Attributes are: jmbag, lastName, firstName, finalGrade.
 *
 * @author ilijan
 */
public class StudentDB {

    /** File containing database entries. */
    private static final String DATA_FILE = "database.txt";
    /** Prompt symbol. */
    private static final String PROMPT_SYMBOL = "> ";
    /** Student database. */
    private static StudentDatabase studentDatabase;

    /**
     * Main method for the program {@code StudentDB}.
     * @param args  Command line arguments. Not used anywhere.
     */
    public static void main(String[] args) {
        List<String> stringRecords;
        try {
            stringRecords = Files.readAllLines(Paths.get(DATA_FILE), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error while opening database file.");
            e.printStackTrace();
            stringRecords = null;
            System.exit(-1);
        }

        studentDatabase = new StudentDatabase(stringRecords);
        welcomeMessage();
        prompt();
    }

    /**
     * Filters through database using given query filter.
     * @param queryFilter   query filter to be used for filtering.
     * @return              list obtained by filtering student database with provided filter.
     */
    private static List<StudentRecord> filter(StudentDatabase.QueryFilter queryFilter) {
        return studentDatabase.filter(queryFilter);
    }

    /**
     * Prints given list of database entries on standard output, in table with variable width to accommodate
     * largest given last name largest given and first name.
     * @param list  list to be printed.
     */
    private static void printListOfEntries(List<StudentRecord> list) {
        // getting parameters for drawing table
        int maxLastNameSize = 0;
        int maxFirstNameSize = 0;
        for (StudentRecord record : list) {
            if (record.getLastName().length() > maxLastNameSize) {
                maxLastNameSize = record.getLastName().length();
            }
            if (record.getFirstName().length() > maxFirstNameSize) {
                maxFirstNameSize = record.getFirstName().length();
            }
        }

        String format = "│ %s │ %-" + maxLastNameSize + "s │ %-" + maxFirstNameSize + "s │ %d │%n";
        String lastNameLine = "";
        for (int i = 0; i < maxLastNameSize; i++) {
            lastNameLine += "─";
        }

        String firstNameLine = "";
        for (int i = 0; i < maxFirstNameSize; i++) {
            firstNameLine += "─";
        }

        System.out.println("┌────────────┬─" + lastNameLine + "─┬─" + firstNameLine + "─┬───┐");
        for (StudentRecord record : list) {
            System.out.format(format, record.getJmbag(), record.getLastName(), record.getFirstName(), record.getFinalGrade());
        }
        System.out.println("└────────────┴─" + lastNameLine + "─┴─" + firstNameLine + "─┴───┘");
        System.out.println("Records selected: " + list.size() + "\n");
    }

    /** Prints welcome message on standard output when program is executed. */
    private static void welcomeMessage() {
        System.out.println("Welcome to the Simple student database.\nType your query or type exit to exit the program.");
    }

    /** Prompts user for new command until exit command is entered. */
    private static void prompt() {
        System.out.print(PROMPT_SYMBOL);
        Scanner standardInput = new Scanner(System.in);
        switch(standardInput.next()) {
            case "exit":
                System.out.println("Bye, bye!");
                System.exit(0);
                break;
            case "query":
                try {
                    StudentDatabase.QueryFilter queryFilter = new StudentDatabase.QueryFilter(standardInput.nextLine());
                    printListOfEntries(filter(queryFilter));
                } catch (Exception e) {
                    System.out.println("Illegal query. " + e.getMessage());
                    System.out.println("Please repeat entry.");
                }
                prompt();
                break;
            default:
                System.out.println("Unsupported instruction. Repeat entry or type exit.");
                prompt();
                break;
        }
    }
}
