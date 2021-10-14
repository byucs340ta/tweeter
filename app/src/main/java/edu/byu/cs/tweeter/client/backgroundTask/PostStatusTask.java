package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AlreadyAuthenticatedTask {
    private static final String LOG_TAG = "PostStatusTask";
    private Status status;

    public PostStatusTask(Handler messageHandler, AuthToken authToken, Status status) {
        super(messageHandler, authToken);
        this.status = status;
    }

    @Override
    protected boolean runTask() {
        // lol do later
        return true;
    }
}
