package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask<T> extends AuthenticatedTask {

    public static final String ITEMS_KEY = "followers-or-statuses";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose following is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;
    /**
     * Maximum number of followed users to return (i.e., page size).
     */
    protected int limit;
    /**
     * The last person being followed returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected T lastItem;
    protected List<T> items;
    protected boolean hasMorePages;

    public PagedTask(Handler messageHandler, AuthToken authToken, User targetUser, int limit, T lastItem) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    @Override
    protected void processTask() {
        Pair<List<T>, Boolean> pageOfItems = getItems();
        items = pageOfItems.getFirst();
        hasMorePages = pageOfItems.getSecond();
        sendSuccessMessage();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(GetFollowersTask.ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(GetFollowersTask.MORE_PAGES_KEY, hasMorePages);
    }

    protected abstract Pair<List<T>, Boolean> getItems();

    public int getLimit() {
        return limit;
    }
    public T getLastItem() {
        return lastItem;
    }
    public User getTargetUser() {
        return targetUser;
    }
    public boolean hasMorePages() {
        return hasMorePages;
    }
}
