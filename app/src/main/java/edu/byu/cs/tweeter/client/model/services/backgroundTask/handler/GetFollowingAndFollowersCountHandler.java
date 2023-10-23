package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.GetFollowingAndFollowersCountObserver;

public class GetFollowingAndFollowersCountHandler extends BackgroundTaskHandler<GetFollowingAndFollowersCountObserver> {
    boolean isGetFollowersCount;
    public GetFollowingAndFollowersCountHandler(GetFollowingAndFollowersCountObserver observer, boolean isGetFollowersCount) {
        super(observer);
        this.isGetFollowersCount = isGetFollowersCount;
    }

    @Override
    protected void handleSuccessMessage(GetFollowingAndFollowersCountObserver observer, Bundle data) {
        int count = data.getInt(GetCountTask.COUNT_KEY);
        if (isGetFollowersCount) {
            observer.getFollowersCountSucceeded(count);
        } else {
            observer.getFollowingCountSucceeded(count);
        }
    }
}
