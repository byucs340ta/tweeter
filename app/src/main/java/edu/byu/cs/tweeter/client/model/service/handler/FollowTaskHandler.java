package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.observer.SetFollowingObserver;

public class FollowTaskHandler extends BackgroundTaskHandler<SetFollowingObserver> {

    public FollowTaskHandler(SetFollowingObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(SetFollowingObserver observer, Message msg) {
        observer.SetFollowingSucceeded(true);
    }

}
