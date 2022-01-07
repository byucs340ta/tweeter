package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a user in the system (i.e., a "user profile").
 */
public class User implements Comparable<User>, Serializable {

    /**
     * The user's first name.
     */
    public String firstName;
    /**
     * The user's last name.
     */
    public String lastName;
    /**
     * The users "alias" or "handle" by which they are known to other users (e.g., @susan)
     */
    public String alias;
    /**
     * URL that points to the user's profile image.
     */
    public String imageUrl;


    public User() {
    }

    public User(String firstName, String lastName, String imageURL) {
        this(firstName, lastName, String.format("@%s%s", firstName, lastName), imageURL);
    }

    public User(String firstName, String lastName, String alias, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.imageUrl = imageURL;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getAlias() {
        return alias;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return alias.equals(user.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", alias='" + alias + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public int compareTo(User user) {
        return this.getAlias().compareTo(user.getAlias());
    }
}
