package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public abstract class PagedTaskHandler <T> extends BackgroundTaskHandler<PagedObserver<T>> {
    protected PagedTaskHandler(PagedObserver<T> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(PagedObserver<T> observer, Message msg) {
        List<T> items = (List<T>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);
        observer.pagedSuccess(items, hasMorePages);
    }
}
