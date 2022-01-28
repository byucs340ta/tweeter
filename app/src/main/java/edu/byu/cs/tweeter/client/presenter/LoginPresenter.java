package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    public interface View {
        void logIn(User user);
        void displayErrorMessage(String message);
    }

    private View view;
    private UserService userService;

    public LoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void logIn(String alias, String password) {
        try {
            validateLogin(alias, password);
            userService.logIn(alias, password, new LoginObserver());
        } catch (Exception e) {
            throw e;
        }
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

    public class LoginObserver implements UserService.LoginObserver {

        @Override
        public void handleSuccess(User user) {
            view.logIn(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to login because of exception: " + exception.getMessage());
        }
    }
}
