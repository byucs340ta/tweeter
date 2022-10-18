package edu.byu.cs.tweeter.client.presenter;

import android.net.Uri;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.ScrollableView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends ScrollablePresenter<Status> {
    private FeedService service;

    public FeedPresenter(ScrollableView<Status> view) {
        super(view);
        service = new FeedService();
    }

    @Override
    protected void getItems(User user) {
        service.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, getLastItem(), new FeedObserver());
    }

    public class FeedObserver extends GetItemsObserver {
        @Override
        protected String getBaseMessage() {
            return "Failed to get feed";
        }
    }
}
