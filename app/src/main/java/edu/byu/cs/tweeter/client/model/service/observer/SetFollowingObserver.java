package edu.byu.cs.tweeter.client.model.service.observer;

public interface SetFollowingObserver extends ServiceObserver {
    void SetFollowingSucceeded(boolean isFollowing);
}
