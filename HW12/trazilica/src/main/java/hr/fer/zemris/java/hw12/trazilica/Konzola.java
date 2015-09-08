package hr.fer.zemris.java.hw12.trazilica;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Console application used for querying document database. Single command-line argument is expected - path
 * to directory containing the collection of textual files to be added to the database.
 * @author ilijan
 */
public class Konzola {
    private static Database database;
    private static List<Database.QueryResult> results;
    private static final String HORIZONTAL_LINE =
            "-------------------------------------------------------";

    /**
     * The main method for the application {@link Konzola}.
     * @param args  command-line arguments. Single command-line argument is expected - path
     *              to directory containing the collection of textual files to be added
     *              to the database.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Path do directory containing document database expected.");
            System.exit(0);
        }

        File directory = new File(args[0]);
        if (!directory.isDirectory()) {
            System.out.println("Given path is not a directory.");
            System.exit(0);
        }

        database = new Database(directory);
        System.out.println("\nSize of dictionary is " + database.getVocabulary().size() + " words.");

        while (true) {
            Status status = prompt();
            if (status == Status.EXIT) {
                break;
            }
        }
    }

    /**
     * Method prompts user for input and calls appropriate methods for supported commands.
     * @return  enum {@link Status} indicating weather user should be prompted again, or
     *          the application should terminate.
     */
    private static Status prompt() {
        System.out.print("\nEnter command > ");
        Scanner sc = new Scanner(System.in);
        switch(sc.next()) {
            case "query":
                query(sc.nextLine());
                return Status.CONTINUE;
            case "type":
                try {
                    type(sc.nextInt());
                } catch (InputMismatchException ex) {
                    System.out.println("Command 'type' expects one integer parameter.");
                }
                return Status.CONTINUE;
            case "results":
                showResults();
                return Status.CONTINUE;
            case "exit":
                return Status.EXIT;
            default:
                System.out.println("Command not found.");
                return Status.CONTINUE;
        }
    }

    /**
     * Shows results of query or, if no results are available, informs user to make query first.
     */
    private static void showResults() {
        try {
            if (results.size() == 0) {
                System.out.println("No results found for given query.");
                return;
            }
            for (int i = 0, numOfResults = results.size(); i < numOfResults; i++) {
                Database.QueryResult result = results.get(i);
                System.out.format("[%d] (%.4f) %s%n", i, result.similarity, result.documentPath);
            }
        } catch (NullPointerException e) {
            System.out.println("No results available. You must make query first.");
        }
    }

    /**
     * Dumps contents of the document which is result of query at the given index on standard output.
     * @param i     index of the file to be printed in the list of results.
     */
    private static void type(int i) {
        try {
            if (results.size() == 0) {
                System.out.println("No results found for given query.");
                return;
            }

            Path path = results.get(i).documentPath;
            List<String> fileLines = Files.readAllLines(path);
            System.out.println(HORIZONTAL_LINE);
            System.out.println("Document: " + path);
            System.out.println(HORIZONTAL_LINE);
            fileLines.forEach(line -> System.out.println(line));
            System.out.println(HORIZONTAL_LINE);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Only indices in range [0, " + (results.size()-1) + "] are permitted.");
        } catch (NullPointerException e) {
            System.out.println("No results available. You must make query first.");
        }
    }

    /**
     * Queries database for the given query.
     * @param queryInput    string representing database query.
     */
    private static void query(String queryInput) {
        DocumentQuery query = new DocumentQuery(queryInput, database);
        System.out.println("Query is: " + query.printWords());

        results = database.getMostSimilar(query);

        System.out.println("The best " + Database.RESULTS_THRESHOLD + " results:");
        showResults();
    }

    /**
     * Indicates status of the application after the command is executed.
     */
    private enum Status {
        /** Application continues and user is prompted again. */
        CONTINUE,
        /** Application terminates. */
        EXIT
    }
}
