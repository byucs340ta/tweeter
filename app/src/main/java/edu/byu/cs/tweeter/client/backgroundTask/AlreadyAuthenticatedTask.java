package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Abstract class for tasks that require an AuthToken I.E, should already be authenticated
 */
public abstract class AlreadyAuthenticatedTask extends BackgroundTask {

    private static final String LOG_TAG = "AlreadyAuthenticatedTask";

    /**
     * Used to ensure active permissions to other tasks.
     */
    protected AuthToken authToken;

    protected AlreadyAuthenticatedTask(Handler messageHandler, AuthToken authToken) {
        super(messageHandler);

        this.authToken = authToken;
    }


}
