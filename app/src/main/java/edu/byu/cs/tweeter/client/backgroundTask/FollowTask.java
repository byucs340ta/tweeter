package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AlreadyAuthenticatedTask {
    private static final String LOG_TAG = "FollowTask";

    /**
     * The user used in runTask() to follow
     */
    private User toFollow;

    public FollowTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.toFollow = targetUser;
    }

    @Override
    protected boolean runTask() {
        // Does nothing rn
        return true;
    }
}
