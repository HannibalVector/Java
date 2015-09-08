package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.Scanner;

/**
 * Class stores single entry for student database.
 * @author ilijan
 */
public class StudentRecord {

    /** JMBAG of the student stored in entry. */
    private String jmbag;

    /** Last name of the student stored in entry. */
    private String lastName;

    /** First name of the student stored in entry. */
    private String firstName;

    /** Final grade of the student stored in entry. */
    private int finalGrade;

    /**
     * Getter for the JMBAG.
     * @return  JMBAG of the student stored in entry.
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * Getter for the last name.
     * @return  last name of the student stored in entry.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for the first name.
     * @return  first name of the student stored in entry.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for the final grade.
     * @return  final grade of the student stored in entry.
     */
    public int getFinalGrade() {
        return finalGrade;
    }

    /**
     * Factory method creates new {@code StudentRecord} from string. Input is expected in form
     * jmbag (tab) lastName(s) (tab) firstName(s) (tab) finalGrade. Final grade should be able
     * to be parsed as {@code int}.
     *
     * @param recordString  string from which new {@code StudentRecord} entry needs to be created.
     * @return              new {@code StudentRecord}.
     */
    public static StudentRecord recordFromString(String recordString) {
        StudentRecord newRecord = new StudentRecord();

        Scanner recordScanner = new Scanner(recordString);
        recordScanner.useDelimiter("\t");
        newRecord.jmbag = recordScanner.next();
        newRecord.lastName = recordScanner.next();
        newRecord.firstName = recordScanner.next();
        newRecord.finalGrade = recordScanner.nextInt();

        return newRecord;
    }

    @Override
    public int hashCode() {
        return this.jmbag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StudentRecord) {
            StudentRecord other = (StudentRecord) obj;
            return this.jmbag.equals(other.jmbag);
        } else {
            return false;
        }
    }
}
