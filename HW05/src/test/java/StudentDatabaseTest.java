import hr.fer.zemris.java.tecaj.hw5.db.StudentDatabase;
import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * @author ilijan
 */
public class StudentDatabaseTest {

    private static final String DATA_FILE = "database.txt";
    private static StudentDatabase studentDatabase;

    @Test
    @Before
    public void testCreatingDatabase() throws IOException{
        List<String> stringRecords = Files.readAllLines(Paths.get(DATA_FILE), StandardCharsets.UTF_8);
        studentDatabase = new StudentDatabase(stringRecords);
    }

    @Test
    public void testFilterWildcardFirstName() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter("firstName = \"A*\""));
        Assert.assertTrue(filteredStudents.get(0).getFirstName().equals("Andrea"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFilterUnsupportedFieldName() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter("ferstName = \"A*\""));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFilterUnsupportedOperator() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter("firstName =<< \"A*\""));
    }

    @Test
    public void testFilterSpecifiedJmbag() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter("jmbag = \"0000000005\"  "));
    }

    @Test
    public void testFilterWildcardLastName() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter(" lastName = \"H*\""));
        Assert.assertTrue(filteredStudents.get(0).getFirstName().equals("Sonja"));
    }

    @Test
    public void testFilterGreaterThanLastName() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter(" lastName > \"Z\""));
        Assert.assertTrue(filteredStudents.get(0).getFirstName().equals("Kristijan"));
    }

    @Test
    public void testFilterLessThanJmbag() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter(" jmbag < \"0000000012\""));
        Assert.assertTrue(filteredStudents.get(10).getFirstName().equals("Jura"));
    }

    @Test
    public void testFilterGreaterThanOrEqualsFirstName() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter(" firstName >= \"Mateja\""));
        Assert.assertTrue(filteredStudents.get(0).getFirstName().equals("Petra"));
    }

    @Test
    public void testFilterLessThanOrEqualsLastName() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter(" lastName <= \"Cvrlje\""));
        Assert.assertTrue(filteredStudents.get(5).getLastName().equals("Cvrlje"));
    }

    @Test
    public void testFilterNotEqualsLastName() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter(" lastName != \"Glumac\""));
        Assert.assertTrue(filteredStudents.get(0).getFirstName().equals("Marin"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testDifferentJMBAG() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter(" jmbag = \"456\" and jmbag = \"567\""));
        Assert.assertTrue(filteredStudents.get(0).getFirstName().equals("Marin"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMoreWildcardChars() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter(" jmbag = \"4*5*6\" "));
    }

    @Test
    public void testWildcardInTheMiddle() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter(" firstName = \"A*a\""));
        Assert.assertTrue(filteredStudents.get(0).getFirstName().equals("Andrea"));
    }

    @Test
    public void testWildcardAtBeginning() {
        List<StudentRecord> filteredStudents = studentDatabase.filter(new StudentDatabase.QueryFilter(" firstName = \"*drea\""));
        Assert.assertTrue(filteredStudents.get(0).getFirstName().equals("Andrea"));
    }

    @Test
    public void testStudentRecordEquals() {
        StudentRecord first = StudentRecord.recordFromString("123\tMatic\tMate\t4");
        StudentRecord second = StudentRecord.recordFromString("123\tPero\tPeric\t3");
        Assert.assertTrue(first.equals(second));
    }

    @Test
    public void testStudentRecordNotEquals() {
        StudentRecord first = StudentRecord.recordFromString("123\tMatic\tMate\t4");
        StudentRecord second = StudentRecord.recordFromString("453\tPero\tPeric\t3");
        Assert.assertFalse(first.equals(second));
    }

    @Test
    public void testStudentRecordNotEqualsWrongType() {
        StudentRecord first = StudentRecord.recordFromString("123\tMatic\tMate\t4");
        Assert.assertFalse(first.equals("baba"));
    }

    @Test
    public void testStudentRecordGetFinalGrade() {
        StudentRecord first = StudentRecord.recordFromString("123\tMatic\tMate\t4");
        Assert.assertTrue(first.getFinalGrade() == 4);
    }


}
