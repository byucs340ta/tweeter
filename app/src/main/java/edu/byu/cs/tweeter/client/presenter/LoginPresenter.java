package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.AccountService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter implements AccountService.LoginObserver {

    private LoginView view;
    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    public void login(String username, String password) {
        String errorMessage = validateLogin(username, password);
        if (errorMessage == null) {
            view.clearInfoMessage();
            view.clearErrorMessage();
            view.displayInfoMessage("Logging In...");
            new AccountService().login(username, password, this);
        }
        else {
            view.displayErrorMessage(errorMessage);
        }

    }

    public interface LoginView {
        void displayErrorMessage(String message);
        void clearErrorMessage();

        void displayInfoMessage(String message);
        void clearInfoMessage();

        void navigateToUser(User user);
    }

    public String validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }

        return null;
    }

    @Override
    public void handleLoginSuccess(User user, AuthToken authToken) {
        view.clearInfoMessage();
        view.clearErrorMessage();

        view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
        view.navigateToUser(user);
    }

    @Override
    public void handleLoginFailure(String message) {
        view.displayInfoMessage("Failed to login: " + message);
    }

    @Override
    public void handleLoginThrewException(Exception ex) {
        view.displayInfoMessage("Failed to login because of exception: " + ex.getMessage());
    }

}
