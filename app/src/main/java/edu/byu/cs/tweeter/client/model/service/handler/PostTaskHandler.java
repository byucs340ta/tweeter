package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.observer.PostObserver;

public class PostTaskHandler extends BackgroundTaskHandler<PostObserver> {

    public PostTaskHandler(PostObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(PostObserver observer, Message msg) {
        observer.PostSucceeded();
    }
}
