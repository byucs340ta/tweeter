package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ScrollableObserver;

public class ScrollableHandler<T> extends BackgroundTaskHandler<ScrollableObserver<T>> {
    public ScrollableHandler(ScrollableObserver<T> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(ScrollableObserver<T> observer, Bundle data) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        boolean morePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, morePages);
    }
}
