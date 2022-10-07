package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status, StatusService> {

    // MARK - Interface Classes
    public interface FeedView extends PagedPresenter.PagedView<Status> {  }

    // MARK - Constructor
    public FeedPresenter(FeedView view) {
        super(view, new StatusService());
    }

    @Override
    public void loadMorePages(User user) {
        service.loadMoreItemsFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new PagedObserver());
    }
}