package hr.fer.zemris.java.student0177035204.hw15.model;

import hr.fer.zemris.java.student0177035204.hw15.util.Util;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents blog entry, i.e. blog post.
 */
@NamedQueries({
		@NamedQuery(
				name="BlogEntry.findByCreator",
				query="from BlogEntry as entry where entry.creator.nick=:creator",
				hints={@QueryHint(name="org.hibernate.cacheable", value="true")}
		)
})
@Entity
@Table(name = "blog_entries")
@Cacheable(true)
public class BlogEntry {

	/** Post id. */
	@Id
	@GeneratedValue
	private Long id;

	/** List of post comments. */
	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("postedOn")
	private List<BlogComment> comments = new ArrayList<>();

	/** Timestamp when the post was created. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date createdAt;

	/** Timestamp when the post was last modified. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date lastModifiedAt;

	/** Post title. */
	@Column(nullable = false, length = 100)
	private String title;

	/** Post text. */
	@Column(nullable = false, length = 4096)
	private String text;

	/** Creator of the post. */
	@ManyToOne
	@JoinColumn(nullable = false)
	private BlogUser creator;

	/**
	 * Getter for the post id.
	 * @return	post id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter for the post id.
	 * @param id new post id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for the list of comments.
	 * @return list of comments.
	 */
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * Getter for the creation timestamp.
	 * @return	creation timestamp.
	 */
	public String getCreatedAt() {
		if (createdAt == null) {
			return null;
		}
		return Util.DATE_FORMAT.format(createdAt);
	}

	/**
	 * Setter for the creation timestamp.
	 * @param createdAt new creation timestamp.
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Getter for the last modification timestamp.
	 * @return	last modification timestamp.
	 */
	public String getLastModifiedAt() {
		if (lastModifiedAt == null) {
			return null;
		}
		return Util.DATE_FORMAT.format(lastModifiedAt);
	}

	/**
	 * Setter for the last modification timestamp.
	 * @param lastModifiedAt	last modification timestamp.
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Getter for the post title.
	 * @return	post title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 *	Setter for the post title.
	 * @param title	new post title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for the post text.
	 * @return post text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter for the post text.
	 * @param text	new post text.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 *	Getter for the blog creator.
	 * @return	blog creator.
	 */
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * Setter fot the blog creator.
	 * @param creator blog creator to be set.
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
