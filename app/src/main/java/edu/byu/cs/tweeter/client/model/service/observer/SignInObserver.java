package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface SignInObserver extends ServiceObserver {
    void SignInSucceeded(AuthToken authToken, User user);
}
