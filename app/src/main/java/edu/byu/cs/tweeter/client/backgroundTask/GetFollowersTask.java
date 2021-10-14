package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowersTask";

    public GetFollowersTask(Handler messageHandler, AuthToken authToken, User targetUser, int limit, User lastItem) {
        super(messageHandler, authToken, targetUser, limit, lastItem);
    }

    @Override
    protected boolean runTask() {
        Pair<List<User>, Boolean> pageOfUsers = getFakeData().getPageOfUsers((User) lastItem, limit, targetUser);
        this.items = pageOfUsers.getFirst();
        this.hasMorePages = pageOfUsers.getSecond();

        for (User u : this.items) {
            BackgroundTaskUtils.loadImage(u);
        }
        return true;
    }

}
