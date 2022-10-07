package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User, FollowService> {

    public interface FollowingView extends PagedPresenter.PagedView<User> {  }

    public FollowingPresenter(FollowingView view) {
        super(view, new FollowService());
    }

    @Override
    public void loadMorePages(User user) {
        service.loadMoreItemsFollowees(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new PagedObserver());
    }
}
