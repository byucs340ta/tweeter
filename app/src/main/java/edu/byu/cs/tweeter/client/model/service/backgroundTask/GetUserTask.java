package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends BackgroundTask {

    // leave tag for now
    private static final String LOG_TAG = "GetUserTask";

    public static final String USER_KEY = "user";

    /**
     * Auth token for logged-in user.
     */
    private AuthToken authToken;
    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String alias;

    private User user;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(messageHandler);
        this.authToken = authToken;
        this.alias = alias;
    }

    private User getUser() {
        User user = getFakeData().findUserByAlias(alias);
        return user;
    }

    @Override
    protected void processTask() {
        this.user = getUser();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
    }
}
