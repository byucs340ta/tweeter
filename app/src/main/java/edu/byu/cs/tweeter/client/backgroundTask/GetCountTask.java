package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetCountTask extends AlreadyAuthenticatedTask {

    private static final String LOG_TAG = "GetCountTask";
    public static final String FOLLOWER_COUNT_KEY = "count";
    public static final String FOLLOWING_COUNT_KEY = "count";

    /**
     * User whose count is being received. Could be using user, could be some other person.
     */
    protected User targetUser;

    /**
     * Count returned by the server, gotten by a child.runTask()
     */
    protected int followersCount;
    protected int followingCount;

    public GetCountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    @Override
    protected boolean runTask() {
        followersCount = 20;
        followingCount = 20;
        return true;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(FOLLOWER_COUNT_KEY, followersCount);
        msgBundle.putInt(FOLLOWING_COUNT_KEY, followingCount);
    }

}
