package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    // MARK: - Class Variables
    private View view;
    private UserService service;

    // MARK: - Interfaces
    public interface View {
        void displayMessage(String message);
        void setLoggingInToast(boolean value);
        void postLoginUser(User loggedInUser);
    }

    // MARK: - Constructors
    public LoginPresenter(View view) {
        this.view = view;
        service = new UserService();
    }

    // MARK: - Class Methods
    public void login(String alias, String password) {
        view.setLoggingInToast(true);
        service.loginUser(alias, password, new GetLoginObserver());
    }

    public void validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    // MARK: - Inner Classes
    private class GetLoginObserver implements UserService.GetLoginObserver {

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to login: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to login because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(User authenticatedUser) {
            view.postLoginUser(authenticatedUser);
            view.setLoggingInToast(false);
            view.displayMessage("Hello" + authenticatedUser.getName());
        }
    }
}
