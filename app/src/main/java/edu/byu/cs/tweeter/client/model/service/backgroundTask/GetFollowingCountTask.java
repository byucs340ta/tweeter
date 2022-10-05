package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.service.GetCountTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {
    private static final String LOG_TAG = "GetFollowingCountTask";

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected int runCountTask() {
        return 20;
    }
}
