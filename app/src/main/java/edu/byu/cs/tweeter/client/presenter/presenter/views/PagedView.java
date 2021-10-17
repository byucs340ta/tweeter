package edu.byu.cs.tweeter.client.presenter.presenter.views;

import java.util.List;

public interface PagedView <T> extends NavigateToUserView {
    void addItems(List<T> items);
    void setLoading(boolean isLoading);
}