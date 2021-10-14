package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends NeedsAuthenticationTask {

    private static final String LOG_TAG = "LoginTask";

    public LoginTask(String alias, String password, Handler messageHandler) {
        super(messageHandler, alias, password);
    }

    @Override
    protected boolean runTask() {
        this.user = getFakeData().getFirstUser();
        this.authToken = getFakeData().getAuthToken();

        BackgroundTaskUtils.loadImage(user);

        return true; // This will be replaced when we add a real server!
    }
}
