package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatedPresenter extends Presenter {
    protected User user;

    public AuthenticatedPresenter(View view, User user) {
        super(view);
        this.user = user;
    }
}
