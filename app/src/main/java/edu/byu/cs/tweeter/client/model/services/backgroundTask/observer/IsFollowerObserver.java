package edu.byu.cs.tweeter.client.model.services.backgroundTask.observer;

public interface IsFollowerObserver extends ServiceObserver {
    public void isFollowerSucceeded(boolean isFollower);
}
