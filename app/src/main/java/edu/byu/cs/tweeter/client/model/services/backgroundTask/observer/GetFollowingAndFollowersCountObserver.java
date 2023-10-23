package edu.byu.cs.tweeter.client.model.services.backgroundTask.observer;

public interface GetFollowingAndFollowersCountObserver extends ServiceObserver {
    void getFollowersCountSucceeded(int followersCount);
    void getFollowingCountSucceeded(int followingCount);
}
