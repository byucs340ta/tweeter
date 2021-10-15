package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

public interface PagedObserver <T> extends ServiceObserver {
    void pagedSuccess(List<T> items, boolean hasMorePages);
}
