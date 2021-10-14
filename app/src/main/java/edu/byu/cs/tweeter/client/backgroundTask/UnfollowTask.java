package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AlreadyAuthenticatedTask {
    private static final String LOG_TAG = "UnfollowTask";

    /**
     * The user used in runTask() to un-follow
     */
    private User toUnfollow;

    public UnfollowTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.toUnfollow = targetUser;
    }

    @Override
    protected boolean runTask() {
        return true;
    }

}
