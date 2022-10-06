package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status, StatusService> {

    // MARK - Constructor
    public StoryPresenter(PagedView<Status> view) {
        super(view, new StatusService());
    }

    @Override
    public void loadMorePages(User user) {
        service.loadMoreItemsStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new PagedObserver());
    }
}