package hr.fer.zemris.java.student0177035204.hw15.model;

import hr.fer.zemris.java.student0177035204.hw15.util.Util;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents comment on a blog entry.
 */
@Entity
@Table(name = "blog_comments")
@Cacheable(true)
public class BlogComment {

	/** Comment id. */
	@Id
	@GeneratedValue
	private Long id;

	/** Blog entry to which comment belongs. */
	@ManyToOne
	@JoinColumn(nullable = false)
	private BlogEntry blogEntry;

	/** Comment message. */
	@Column(nullable = false, length = 4096)
	private String message;

	/** Timestamp of the moment when comment was posted. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date postedOn;

	/** Author of the comment. */
	@ManyToOne
	private BlogUser author;

	/**
	 * Getter for the comment author.
	 * @return author of the comment.
	 */
	public BlogUser getAuthor() {
		return author;
	}

	/**
	 * Setter for the comment author.
	 * @param author	new comment author.
	 */
	public void setAuthor(BlogUser author) {
		this.author = author;
	}

	/**
	 * Getter for comment id.
	 * @return	comment id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter for the comment id.
	 * @param id	new comment id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for the blog entry to which comment belongs.
	 * @return	blog entry to which comment belongs.
	 */
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Setter for the blog entry.
	 * @param blogEntry	new blog entry.
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Getter for the message.
	 * @return	comment message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter for the message.
	 * @param message	new comment message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter for the timestamp. Uses formatting provided in {@link Util} class.
	 * @return	timestamp when the comment was posted.
	 */
	public String getPostedOn() {
		if (postedOn == null) {
			return null;
		}
		return Util.DATE_FORMAT.format(postedOn);
	}

	/**
	 * Setter for the timestamp.
	 * @param postedOn	new timestamp.
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
