package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Base class for tasks that receive pages of stuff
 * @param <T>
 */
public abstract class PagedTask<T> extends AlreadyAuthenticatedTask {

    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user to get these paged items from
     */
    protected User targetUser;

    /**
     * The number of items to get
     */
    protected int limit;

    /**
     * The last item returned on success
     */
    protected T lastItem;

    /**
     * List of items returned on success. Must be defined in children classes
     */
    protected List<T> items;

    /**
     * Whether or not there are more pages, returned on success. Must be defined in child class.
     */
    protected boolean hasMorePages;

    public PagedTask(Handler messageHandler, AuthToken authToken, User targetUser, int limit, T lastItem) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) this.items);
        msgBundle.putBoolean(MORE_PAGES_KEY, this.hasMorePages);
    }
}
