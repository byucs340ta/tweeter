package edu.byu.cs.tweeter.client.presenter;

import android.net.Uri;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.ScrollableView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends ScrollablePresenter<Status> {
    private StoryService service;

    public StoryPresenter(ScrollableView<Status> view) {
        super(view);
        service = new StoryService();
    }


    @Override
    protected void getItems(User user) {
        service.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, getLastItem(), new StoryObserver());
    }

    public class StoryObserver extends GetItemsObserver {
        @Override
        protected String getBaseMessage() {
            return "Failed to get story";
        }
    }
}
