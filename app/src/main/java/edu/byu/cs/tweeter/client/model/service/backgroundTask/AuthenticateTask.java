package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

public abstract class AuthenticateTask extends BackgroundTask {

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    protected String alias;
    /**
     * The user's password.
     */
    protected String password;

    public AuthenticateTask(Handler messageHandler, String alias, String password) {
        super(messageHandler);
        this.alias = alias;
        this.password = password;
    }
}
