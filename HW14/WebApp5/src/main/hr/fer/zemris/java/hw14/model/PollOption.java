package hr.fer.zemris.java.hw14.model;

/**
 * Represents single poll option.
 */
public class PollOption {
    /** Poll option id. */
    private long id;
    /** Option name. */
    private String name;
    /** Hyperlink associated with option. */
    private String link;
    /** Current number of votes for given option in a poll. */
    private int numberOfVotes;

    /**
     * Constructor initializes new {@link PollOption} using option name and link associated
     * with the option.
     * @param name          name of the option.
     * @param link          link associated with the option.
     */
    public PollOption(long id, String name, String link, int numberOfVotes) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.numberOfVotes = numberOfVotes;
    }

    /**
     * Getter for the {@link PollOption} id.
     * @return  {@link PollOption} id.
     */
    public long getID() {
        return id;
    }

    /**
     * Getter for the option name.
     * @return  option name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the link.
     * @return  link associated with the option..
     */
    public String getLink() {
        return link;
    }

    /**
     * Upvotes option by one in a poll.
     */
    public void vote() {
        numberOfVotes++;
    }

    /**
     * Gets votes count for the given option in a poll.
     * @return  votes count.
     */
    public int getVotes() {
        return numberOfVotes;
    }
}
