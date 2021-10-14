package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class GetCountTask extends AlreadyAuthenticatedTask {

    private static final String LOG_TAG = "GetCountTask";
    public static final String COUNT_KEY = "count";

    /**
     * User whose count is being received. Could be using user, could be some other person.
     */
    protected User targetUser;

    /**
     * Count returned by the server, gotten by a child.runTask()
     */
    protected int count;

    public GetCountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
    }

}
