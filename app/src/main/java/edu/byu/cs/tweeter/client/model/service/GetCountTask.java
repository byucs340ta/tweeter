package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class GetCountTask extends AuthenticatedTask {

    public static final String COUNT_KEY = "count";
    /**
     * The user whose following count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;
    protected int count;

    public GetCountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    @Override
    protected void processTask() {
        count = runCountTask();
        sendSuccessMessage();
    }

    protected abstract int runCountTask();

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
    }
}
