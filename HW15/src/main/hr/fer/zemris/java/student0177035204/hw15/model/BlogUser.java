package hr.fer.zemris.java.student0177035204.hw15.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents user of the blog and creator of blog posts and comments.
 * @author ilijan
 */
@NamedQueries({
        @NamedQuery(
                name="BlogUser.findByNick",
                query="from BlogUser as user where user.nick=:nick",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}
        ),
        @NamedQuery(
        name="BlogUser.getAll",
        query="from BlogUser",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}
        )
})
@Entity
@Table(name = "blog_users")
@Cacheable(true)
public class BlogUser {

    /** User id. */
    @Id
    @GeneratedValue
    private Long id;

    /** First name. */
    @Column(nullable = false, length = 100)
    private String firstName;

    /** Last name. */
    @Column(nullable = false, length = 100)
    private String lastName;

    /** Nickname. */
    @Column(nullable = false, unique = true, length = 100)
    private String nick;

    /** E-mail. */
    @Column(nullable = false, length = 100)
    private String email;

    /** Password hash. */
    @Column(nullable = false, length = 100)
    private String passwordHash;

    /** List of blog entries associated with the user. */
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @OrderBy("createdAt")
    private List<BlogEntry> blogEntries = new ArrayList<>();

    /**
     * Getter for the user id.
     * @return user id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the user id.
     * @param id new user id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the first name.
     * @return first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for the first name.
     * @param firstName new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for the last name.
     * @return last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for the last name.
     * @param lastName new last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for the nickname.
     * @return nickname.
     */
    public String getNick() {
        return nick;
    }

    /**
     * Setter for the nickname.
     * @param nick new nickname.
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * Getter for the e-mail.
     * @return e-mail.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the e-mail.
     * @param email new e-mail.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the password hash.
     * @return  password hash.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Setter for the password hash.
     * @param passwordHash  new password hash.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Getter for the list of blog entries.
     * @return list of blog entries.
     */
    public List<BlogEntry> getBlogEntries() {
        return blogEntries;
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
        BlogUser other = (BlogUser) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
