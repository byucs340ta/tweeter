package edu.byu.cs.tweeter.client.presenter.presenter.views;

import edu.byu.cs.tweeter.model.domain.User;

public interface NavigateToUserView extends BaseView {
    void navigateToUser(User user);

}
