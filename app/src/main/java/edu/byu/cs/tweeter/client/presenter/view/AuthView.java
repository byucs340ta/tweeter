package edu.byu.cs.tweeter.client.presenter.view;

import edu.byu.cs.tweeter.model.domain.User;

public interface AuthView extends BaseView {
    void authSuccessful(User user);
    void displayErrorMessage(String message);
    void clearErrorMessage();
    void clearInfoMessage();
}
