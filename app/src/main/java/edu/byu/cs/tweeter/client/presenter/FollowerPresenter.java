package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.ScrollableView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPresenter extends ScrollablePresenter<User> {
    private FollowService service;

    public FollowerPresenter(ScrollableView<User> view) {
        super(view);
        service = new FollowService();
    }

    @Override
    protected void getItems(User user) {
        service.getFollower(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, getLastItem(), new GetFollowerObserver());
    }

    public class GetFollowerObserver extends GetItemsObserver {
        @Override
        protected String getBaseMessage() {
            return "Failed to get followers";
        }
    }
}
