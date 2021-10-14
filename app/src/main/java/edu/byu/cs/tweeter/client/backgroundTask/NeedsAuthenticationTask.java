package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Abstract class for tasks that need an AuthToken I.E, need to be authenticated
 */
public abstract class NeedsAuthenticationTask extends BackgroundTask {

    private static final String LOG_TAG = "AuthenticateTask";

    // Output. Used to pack the bundle
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The user's username E.g., "@susan"
     */
    private String alias;

    /**
     * The user's password
     */
    private String password;

    /**
     * The logged-in user returned by the server.
     */
    protected User user;

    /**
     * The auth token returned by the server.
     */
    protected AuthToken authToken;

    protected NeedsAuthenticationTask(Handler messageHandler, String alias, String password) {
        super(messageHandler);

        this.alias = alias;
        this.password = password;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, this.user);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, this.authToken);
    }

}
