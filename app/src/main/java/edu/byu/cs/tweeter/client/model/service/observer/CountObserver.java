package edu.byu.cs.tweeter.client.model.service.observer;

public interface CountObserver extends ServiceObserver {
    void CountSucceeded(int followersCount, int followingCount);
}
