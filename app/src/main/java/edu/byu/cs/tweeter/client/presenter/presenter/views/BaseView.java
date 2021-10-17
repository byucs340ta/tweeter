package edu.byu.cs.tweeter.client.presenter.presenter.views;

import edu.byu.cs.tweeter.model.domain.User;

public interface BaseView {
    void displayInfoMessage(String message);
    void clearInfoMessage();

    void displayErrorMessage(String message);
    void clearErrorMessage();
}
