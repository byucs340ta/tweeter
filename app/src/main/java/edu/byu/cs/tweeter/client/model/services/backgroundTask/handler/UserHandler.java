package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class UserHandler extends BackgroundTaskHandler<UserObserver>{
    public UserHandler(UserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(UserObserver observer, Bundle data) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        observer.getUserSucceeded(user);
    }
}
