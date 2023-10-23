package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.FollowObserver;

public class FollowHandler extends BackgroundTaskHandler<FollowObserver> {
    boolean isUnfollow;
    public FollowHandler(FollowObserver observer, boolean isUnfollow) {
        super(observer);
        this.isUnfollow = isUnfollow;
    }

    @Override
    protected void handleSuccessMessage(FollowObserver observer, Bundle data) {
        if (isUnfollow) {
            observer.unfollowSucceeded();
        } else {
            observer.followSucceeded();
        }
    }
}
