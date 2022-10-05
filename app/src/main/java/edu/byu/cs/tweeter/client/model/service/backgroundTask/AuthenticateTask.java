package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticateTask extends BackgroundTask {

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    protected String alias;
    /**
     * The user's password.
     */
    protected String password;

    protected User authenticatedUser;
    protected AuthToken authToken;

    public AuthenticateTask(Handler messageHandler, String alias, String password) {
        super(messageHandler);
        this.alias = alias;
        this.password = password;
    }

    @Override
    protected void processTask() {
        Pair<User, AuthToken> authenticationResult = runAuthenticationTask();
        authenticatedUser = authenticationResult.getFirst();
        authToken = authenticationResult.getSecond();
        sendSuccessMessage();
    }

    protected abstract Pair<User, AuthToken> runAuthenticationTask();

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }
}
