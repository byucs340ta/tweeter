package edu.byu.cs.tweeter.client.backgroundTask;

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
public class GetFeedTask extends PagedTask<Status> {
    private static final String LOG_TAG = "GetFeedTask";

    public GetFeedTask(Handler messageHandler, AuthToken authToken, User targetUser, int limit, Status lastItem) {
        super(messageHandler, authToken, targetUser, limit, lastItem);
    }

    @Override
    protected boolean runTask() {
        Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(lastItem, limit);

        this.items = pageOfStatus.getFirst();
        this.hasMorePages = pageOfStatus.getSecond();

        for (Status s : this.items) {
            BackgroundTaskUtils.loadImage(s.getUser());
        }

        return true;
    }
}
