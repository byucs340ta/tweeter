package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.AccountService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.AuthView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends AuthPresenter {

    public LoginPresenter(AuthView view) {
        super(view);
    }

    public void login(String username, String password) {
        String errorMessage = validateLogin(username, password);
        AuthView view = getView();
        if (errorMessage == null) {
            view.clearErrorMessage();
            view.clearInfoMessage();
            view.displayMessage("Logging in...");
            accountService.login(username, password, new LoginObserver());
        }
        else {
            view.displayErrorMessage(errorMessage);
        }
    }

    public class LoginObserver extends AuthObserver {
        @Override
        protected String getBaseMessage() {
            return "Failed to login";
        }
    }
}
