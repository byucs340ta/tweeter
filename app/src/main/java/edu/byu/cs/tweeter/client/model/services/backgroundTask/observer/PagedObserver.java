package edu.byu.cs.tweeter.client.model.services.backgroundTask.observer;

import java.util.List;

public interface PagedObserver<T> extends ServiceObserver { // T is going to be Status or User
    void handleSuccess(List<T> items, boolean hasMorePages, T lastItem);
}
