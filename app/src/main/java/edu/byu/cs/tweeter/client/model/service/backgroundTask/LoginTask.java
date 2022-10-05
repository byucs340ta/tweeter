package edu.byu.cs.tweeter.client.model.service.backgroundTask;

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
public class LoginTask extends AuthenticateTask {

    private static final String LOG_TAG = "LoginTask";

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    private User loggedInUser;

    // TODO: should I do something with the authtoken?
    private AuthToken authToken;

    public LoginTask(String alias, String password, Handler messageHandler) {
        super(messageHandler, alias, password);
    }

    @Override
    protected void processTask() {
        Pair<User, AuthToken> loginResult = doLogin();

        loggedInUser = loginResult.getFirst();
        authToken = loginResult.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, loggedInUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }

    private Pair<User, AuthToken> doLogin() {
        User loggedInUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(loggedInUser, authToken);
    }
}
