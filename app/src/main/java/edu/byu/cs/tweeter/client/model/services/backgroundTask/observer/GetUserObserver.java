package edu.byu.cs.tweeter.client.model.services.backgroundTask.observer;

import edu.byu.cs.tweeter.model.domain.User;

public interface GetUserObserver extends ServiceObserver {
    void getUserSucceeded(User user);
}
