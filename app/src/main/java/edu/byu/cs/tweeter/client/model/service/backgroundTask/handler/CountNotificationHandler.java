package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.CountNotificationObserver;

public class CountNotificationHandler extends BackgroundTaskHandler<CountNotificationObserver> {

    public CountNotificationHandler(CountNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, CountNotificationObserver observer) {
        observer.handleSuccess(data.getInt(GetFollowersCountTask.COUNT_KEY));
    }
}
