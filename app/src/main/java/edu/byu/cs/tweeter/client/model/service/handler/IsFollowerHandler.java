package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;

public class IsFollowerHandler extends BackgroundTaskHandler<IsFollowerObserver> {

    public IsFollowerHandler(IsFollowerObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(IsFollowerObserver observer, Message msg) {
        boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.IsFollowerSucceeded(isFollower);
    }
}
