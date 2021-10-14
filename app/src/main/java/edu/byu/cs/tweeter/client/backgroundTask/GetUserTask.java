package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AlreadyAuthenticatedTask {
    private static final String LOG_TAG = "GetUserTask";
    public static final String USER_KEY = "user";
    private String alias; // used to get the correct user
    private User user; // user being got and returned up

    public GetUserTask(Handler messageHandler, AuthToken authToken, String alias) {
        super(messageHandler, authToken);
        this.alias = alias;
    }

    @Override
    protected boolean runTask() {
        user = getFakeData().findUserByAlias(alias); // fix with service pass
        return true;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
    }
}
