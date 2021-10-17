package edu.byu.cs.tweeter.client.model.service.observer;

public interface IsFollowerObserver extends ServiceObserver {
void IsFollowerSucceeded(boolean isFollower);
}
