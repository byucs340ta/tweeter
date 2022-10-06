package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticatePresenter extends BasePresenter<AuthenticatePresenter.AuthenticateView> {

    protected String messageType;

    public AuthenticatePresenter(AuthenticateView view, String messageType) {
        super(view);
        this.messageType = messageType;
    }

    public interface AuthenticateView extends BasePresenter.View {
        void AuthenticatedToast(boolean value);
        void postAuthenticateUser(User loggedInUser);
    }

    public void validateCredentials(String alias, String password) {
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
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

    protected class GetAuthenticateObserver extends BaseObserver implements AuthenticateNotificationObserver {

        public GetAuthenticateObserver() {
            super(AuthenticatePresenter.this.messageType);
        }

        // Make this abstract in parent class
        @Override
        public void handleSuccess(User authenticatedUser) {
            view.postAuthenticateUser(authenticatedUser);
            view.AuthenticatedToast(false);
            view.displayMessage("Hello " + authenticatedUser.getName());
        }
    }
}