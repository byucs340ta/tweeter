package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PagedObserver;

public abstract class PagedHandler<T> extends BackgroundTaskHandler<PagedObserver<T>> {
    public PagedHandler(PagedObserver<T> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(PagedObserver<T> observer, Bundle data) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);
        assert items != null;
        T lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
        observer.handleSuccess(items, hasMorePages, lastItem);
    }

}
