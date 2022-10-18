package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthHandler extends BackgroundTaskHandler<AuthObserver> {
    public AuthHandler(AuthObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(AuthObserver observer, Bundle data) {
        User user = (User) data.getSerializable(AuthenticateTask.USER_KEY);
        AuthToken token = (AuthToken) data.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);

        observer.handleSuccess(user, token);
    }
}
