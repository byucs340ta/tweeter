package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.observer.SetFollowingObserver;

public class UnfollowTaskHandler extends BackgroundTaskHandler<SetFollowingObserver> {

    public UnfollowTaskHandler(SetFollowingObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(SetFollowingObserver observer, Message msg) {
        observer.SetFollowingSucceeded(false);
    }

}
