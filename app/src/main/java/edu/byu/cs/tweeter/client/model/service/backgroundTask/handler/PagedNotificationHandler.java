package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class PagedNotificationHandler<T> extends BackgroundTaskHandler<PagedNotificationObserver> {

    public PagedNotificationHandler(PagedNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedNotificationObserver observer) {
        observer.handleSuccess((List) data.getSerializable(PagedTask.ITEMS_KEY), data.getBoolean(GetFollowersTask.MORE_PAGES_KEY));
    }
}
