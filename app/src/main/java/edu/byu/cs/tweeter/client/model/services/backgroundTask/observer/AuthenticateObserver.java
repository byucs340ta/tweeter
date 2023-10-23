package edu.byu.cs.tweeter.client.model.services.backgroundTask.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticateObserver extends ServiceObserver {
    void authenticateSucceeded(AuthToken authToken, User user);
}
