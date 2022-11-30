package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.Objects;

public class Follower implements Comparable<Follower>, Serializable {

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


    public Follower() {}

    public Follower(String firstName, String lastName, String alias) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
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
    public int compareTo(Follower follower) {
        return this.getAlias().compareTo(follower.getAlias());
    }
}
