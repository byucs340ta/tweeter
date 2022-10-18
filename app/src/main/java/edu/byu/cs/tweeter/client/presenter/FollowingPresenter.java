package edu.byu.cs.tweeter.client.presenter;

import android.net.Uri;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.ScrollableView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends ScrollablePresenter<User> {
    private FollowService service;

    public FollowingPresenter(ScrollableView<User> view) {
        super(view);
        service = new FollowService();
    }

    @Override
    protected void getItems(User user) {
        service.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, getLastItem(), new GetFollowingObserver());
    }
    public class GetFollowingObserver extends GetItemsObserver {
        @Override
        protected String getBaseMessage() {
            return "Failed to get following";
        }
    }
}
