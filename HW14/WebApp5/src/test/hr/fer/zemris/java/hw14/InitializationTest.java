package hr.fer.zemris.java.hw14;

import hr.fer.zemris.java.hw14.model.Poll;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests method used to load polls from directory.
 * @author ilijan
 */
public class InitializationTest {
    List<Poll> polls;

    @Before
    public void loadPolls() {
        String pollsPath = "web/aplikacija5/WEB-INF/polls";
        polls = Initialization.loadPolls(pollsPath);
        polls.sort((p1, p2) -> p1.getTitle().compareTo(p2.getTitle()));
    }

    @Test
    public void testPollNames() {
        assertEquals("Glasanje za najdražu boju", polls.get(0).getTitle());
        assertEquals("Glasanje za omiljeni bend", polls.get(1).getTitle());
        assertEquals("Najdraža marka automobila", polls.get(2).getTitle());
    }

    @Test
    public void testPollMessages() {
        assertEquals("Koja vam je najdraža boja? Kliknite na link kako biste glasali!", polls.get(0).getMessage());
        assertEquals("Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!", polls.get(1).getMessage());
        assertEquals("Od sljedećih marki automobila koja vam je najdraža? Kliknite na link kako biste glasali!", polls.get(2).getMessage());
    }
}
