package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.ServiceObserver;

public class LogoutHandler extends BackgroundTaskHandler<LogoutObserver> {
    public LogoutHandler(LogoutObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(LogoutObserver observer, Bundle data) {
        observer.logoutSucceeded();
    }
}
