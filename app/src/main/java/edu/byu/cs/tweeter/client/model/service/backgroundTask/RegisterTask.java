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
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends BackgroundTask {
    private static final String LOG_TAG = "RegisterTask";

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The user's first name.
     */
    private String firstName;
    /**
     * The user's last name.
     */
    private String lastName;
    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;
    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private String image;

    private User registeredUser;
    private AuthToken authToken;

    public RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler) {
        super(messageHandler);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.image = image;
    }

    @Override
    protected void processTask() {
        Pair<User, AuthToken> registerResult = doRegister();
        registeredUser = registerResult.getFirst();
        authToken = registerResult.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, registeredUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }

    private Pair<User, AuthToken> doRegister() {
        User registeredUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(registeredUser, authToken);
    }
}
