package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatePresenter extends Presenter implements AuthenticateObserver {
    public AuthenticatePresenter(View view) {
        super(view);
    }

    @Override
    public void authenticateSucceeded(AuthToken authToken, User user) {
        view.hideErrorMessage();
        view.hideInfoMessage();
        view.showInfoMessage("Hello, " + user.getName());
        view.openView(user);
    }
}
