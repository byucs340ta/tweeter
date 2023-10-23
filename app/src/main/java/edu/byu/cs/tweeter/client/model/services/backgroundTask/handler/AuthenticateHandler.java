package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticateHandler extends BackgroundTaskHandler<AuthenticateObserver> {
    public AuthenticateHandler(AuthenticateObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(AuthenticateObserver observer, Bundle data) {
        User authenticatedUser = (User) data.getSerializable(AuthenticateTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);

        Cache.getInstance().setCurrUser(authenticatedUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        observer.authenticateSucceeded(authToken, authenticatedUser);
    }
}
