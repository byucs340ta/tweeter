package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends BasePresenter<LoginPresenter.LoginView> {

    // MARK: - Interfaces
    public interface LoginView extends BasePresenter.View {
        void setLoggingInToast(boolean value);
        void postLoginUser(User loggedInUser);
    }

    // MARK: - Constructors
    public LoginPresenter(LoginView view) {
        super(view);
    }

    // MARK: - Class Methods
    public void login(String alias, String password) {
        view.setLoggingInToast(true);
        userService.loginUser(alias, password, new GetLoginObserver());
    }

    // MOve to parent of login/register class
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
    protected class GetLoginObserver extends BaseObserver implements AuthenticateNotificationObserver {

        public GetLoginObserver() {
            super("login");
        }

        // Make this abstract in parent class
        @Override
        public void handleSuccess(User authenticatedUser) {
            view.postLoginUser(authenticatedUser);
            view.setLoggingInToast(false);
            view.displayMessage("Hello " + authenticatedUser.getName());
        }
    }
}