package edu.byu.cs.tweeter.client.presenter.view;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface ScrollableView<T> extends BaseView {
    void setLoadingStatus(boolean value);
    void addItems(List<T> items);
    void goToUser(User user);
}
