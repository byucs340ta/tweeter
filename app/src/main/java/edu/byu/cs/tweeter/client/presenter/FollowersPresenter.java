package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.presenter.presenter.views.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

    public class FollowersPresenter extends PagedPresenter<User> implements PagedObserver<User>, GetUserObserver {
        private static final String LOG_TAG = "FollowersPresenter";

        private PagedView<User> view;

        public FollowersPresenter(PagedView<User> view, AuthToken authToken, User targetUser) {
            super(view, authToken, targetUser);
            this.view = view;
        }

        @Override
        protected void runGetService() {
            new FollowService().getFollowers(authToken, targetUser, PAGE_SIZE, lastItem, this);
        }


        public void goToUser(String alias) {
            view.displayInfoMessage("Getting user's profile...");
            new UserService().getUser(authToken, alias, this);
        }

    }
