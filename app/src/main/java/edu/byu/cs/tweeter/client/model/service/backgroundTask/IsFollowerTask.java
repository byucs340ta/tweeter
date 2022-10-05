package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Random;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends BackgroundTask {
    private static final String LOG_TAG = "IsFollowerTask";

    public static final String IS_FOLLOWER_KEY = "is-follower";

    /**
     * Auth token for logged-in user.
     */
    private AuthToken authToken;
    /**
     * The alleged follower.
     */
    private User follower;
    /**
     * The alleged followee.
     */
    private User followee;

    private boolean isFollower;

    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(messageHandler);
        this.authToken = authToken;
        this.follower = follower;
        this.followee = followee;
    }

    /* // WHAT TO DO ABOUT sendSuccessMessage?
    @Override
    public void run() {
        try {

            sendSuccessMessage(new Random().nextInt() > 0);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }
     */

    @Override
    protected void processTask() {
        isFollower = new Random().nextInt() > 0;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
    }
}
