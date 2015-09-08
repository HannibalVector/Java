package hr.fer.zemris.java.hw14.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Models poll with multiple options.
 */
public class Poll {
	/** Poll ID. */
	private long id;
	/** Poll title. */
	private String title;
	/** Poll message. */
	private String message;
	/** List of poll options. */
	private List<PollOption> pollOptions = new ArrayList<>();

	/**
	 * Getter for the poll ID.
	 * @return	poll ID.
	 */
	public long getID() {
		return id;
	}

	/**
	 * Setter for the poll ID.
	 * @param id	poll ID to be set.
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * Getter for the poll title.
	 * @return	poll title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for the poll title.
	 * @param title		poll title to be set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for the poll message.
	 * @return	poll message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter for the poll message.
	 * @param message	poll message to be set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets list of poll options.
	 * @return		list of poll options.
	 */
	public List<PollOption> getPollOptions() {
		return pollOptions;
	}

	@Override
	public String toString() {
		return "Poll id="+id;
	}
}