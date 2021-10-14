package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AlreadyAuthenticatedTask {
    private static final String LOG_TAG = "LogoutTask";

    public LogoutTask(Handler messageHandler, AuthToken authToken) {
        super(messageHandler, authToken);
    }

    @Override
    protected boolean runTask() {
        // Does nothing yet...
        return true;
    }
}
