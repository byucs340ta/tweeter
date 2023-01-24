package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a status (or tweet) posted by a user.
 */
public class Status implements Serializable {
    /**
     * Text for the status.
     */
    public String post;
    /**
     * User who sent the status.
     */
    public User user;
    /**
     * String representation of the date/time at which the status was sent.
     */
    public long timestamp;
    /**
     * URLs contained in the post text.
     */
    public List<String> urls;
    /**
     * User mentions contained in the post text.
     */
    public List<String> mentions;

    public Status() {
    }

    public Status(String post, User user, long timestamp, List<String> urls, List<String> mentions) {
        this.post = post;
        this.user = user;
        this.timestamp = timestamp;
        this.urls = urls;
        this.mentions = mentions;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFormattedDate() {
        return new SimpleDateFormat("E MMM d k:mm:ss z y", Locale.US).format(new Date(timestamp));
    }
    public String getPost() {
        return post;
    }

    public List<String> getUrls() {
        return urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(post, status.post) &&
                Objects.equals(user, status.user) &&
                Objects.equals(timestamp, status.timestamp) &&
                Objects.equals(mentions, status.mentions) &&
                Objects.equals(urls, status.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, user, timestamp, mentions, urls);
    }

    @Override
    public String toString() {
        return "Status{" +
                "post='" + post + '\'' +
                ", user=" + user +
                ", timestamp=" + timestamp +
                ", mentions=" + mentions +
                ", urls=" + urls +
                '}';
    }

}
