package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends AuthenticatedTask {
    private static final String LOG_TAG = "GetFeedTask";

    public static final String STATUSES_KEY = "statuses";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose feed is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;
    /**
     * Maximum number of statuses to return (i.e., page size).
     */
    private int limit;
    /**
     * The last status returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private Status lastStatus;

    private List<Status> statuses;
    private boolean hasMorePages;

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    @Override
    protected void processTask() {
        Pair<List<Status>, Boolean> pageOfStatus = getFeed();

        statuses = pageOfStatus.getFirst();
        hasMorePages = pageOfStatus.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(STATUSES_KEY, (Serializable) statuses);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }

    private Pair<List<Status>, Boolean> getFeed() {
        Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(lastStatus, limit);
        return pageOfStatus;
    }
}
