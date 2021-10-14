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
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends NeedsAuthenticationTask {

    private static final String LOG_TAG = "RegisterTask";

    /**
     * The user's first name.
     */
    private String firstName;
    /**
     * The user's last name.
     */
    private String lastName;

    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private String image;

    public RegisterTask(String firstName, String lastName, String alias, String password,
                        String image, Handler messageHandler) {
        super(messageHandler, alias, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }

    @Override
    protected boolean runTask() {
        this.user = getFakeData().getFirstUser();
        this.authToken = getFakeData().getAuthToken();

        BackgroundTaskUtils.loadImage(user);

        return true; // for now
    }
}
