package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User, FollowService> {

    public interface FollowersView extends PagedPresenter.PagedView<User> { }

    public FollowersPresenter(FollowersView view) {
        super(view, new FollowService());
    }

    @Override
    public void loadMorePages(User user) {
        service.loadMoreItemsFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new PagedObserver());
    }
}