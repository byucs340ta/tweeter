package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer;

import edu.byu.cs.tweeter.model.domain.User;

public interface IsFollowerObserver extends ServiceObserver {
    void handleSuccess(boolean isFollower);
}
