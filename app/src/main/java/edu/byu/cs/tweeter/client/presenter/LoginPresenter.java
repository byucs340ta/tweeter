package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.view.login.LoginFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter implements UserService.LoginObserver {


    public interface View {
        void showInfoMessage(String message);
        void hideInfoMessage();
        void showErrorMessage(String message);
        void hideErrorMessage();

        void openMainView(User user);
    }

    private View view;

    public LoginPresenter(View view) {
        this.view = view;
    }

    public void login(String alias, String password) {
        if (validateLogin(alias, password)) {
            view.hideErrorMessage();

//        loginToast = Toast.makeText(getContext(), "Logging In...", Toast.LENGTH_LONG);
//        loginToast.show();
            view.showInfoMessage("Logging In...");

            var userService = new UserService();
            userService.login(alias, password, this);
        }
    }

    public boolean validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            view.showErrorMessage("Alias must begin with @.");
            return false;
        }
        if (alias.length() < 2) {
            view.showErrorMessage("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            view.showErrorMessage("Password cannot be empty.");
            return false;
        }
        return true;
    }

    @Override
    public void loginSucceeded(AuthToken authToken, User user) {
        view.hideErrorMessage();
        view.hideInfoMessage();
        view.showInfoMessage("Hello, " + user.getName());
        view.openMainView(user);
    }

    @Override
    public void logingFailed(String message) {
    view.showErrorMessage(message);
    }
}
