package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PostObserver;

public class PostHandler extends BackgroundTaskHandler<PostObserver> {

    public PostHandler(PostObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(PostObserver observer, Bundle data) {
        observer.postSucceeded();
    }
}
