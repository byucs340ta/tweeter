package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;

public class CountTaskHandler extends BackgroundTaskHandler<CountObserver> {

    public CountTaskHandler(CountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(CountObserver observer, Message msg) {
        int followersCount = msg.getData().getInt(GetCountTask.FOLLOWER_COUNT_KEY);
        int followingCount = msg.getData().getInt(GetCountTask.FOLLOWING_COUNT_KEY);
        observer.CountSucceeded(followersCount, followingCount);
    }
}
