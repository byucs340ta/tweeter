package edu.byu.cs.tweeter.client.model.services.backgroundTask.observer;

public interface FollowObserver extends ServiceObserver {
    void followSucceeded();
    void unfollowSucceeded();
}
