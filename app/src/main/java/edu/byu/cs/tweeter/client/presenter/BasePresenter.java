package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.presenter.views.BaseView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class BasePresenter {
//    protected BaseView view;
    protected AuthToken authToken;
    protected User targetUser;

    public BasePresenter(/*BaseView view,*/AuthToken authToken, User targetUser) {
//        this.view = view;
        this.authToken = authToken;
        this.targetUser = targetUser;
    }

}
