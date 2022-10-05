package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer;

import java.util.List;

public interface PagedNotificationObserver<T> extends ServiceObserver {
    void handleSuccess(List<T> items, boolean hasMorePages);
}
