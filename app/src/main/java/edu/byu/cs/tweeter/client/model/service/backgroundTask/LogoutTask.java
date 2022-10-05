package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends BackgroundTask {
    private static final String LOG_TAG = "LogoutTask";

    /**
     * Auth token for logged-in user.
     */
    private AuthToken authToken;

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(messageHandler);
        this.authToken = authToken;
    }

    /* // run doesn't have much...
    @Override
    public void run() {
        try {

            sendSuccessMessage();

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }
     */
    @Override
    protected void processTask() {
        // TODO: Should something be here
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // TODO: Should something be here?
    }

}
