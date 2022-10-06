package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends BasePresenter<StoryPresenter.StoryView> {

    // MARK: - Class Variables

    private static final int PAGE_SIZE = 10;
    private StatusService statusService;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public interface StoryView extends BasePresenter.View {
        void setLoadingFooter(boolean value);
        void addStatusesToStory(List<Status> statuses);
        void getUserProfile(User user);
    }

    // MARK - Constructor
    public StoryPresenter(StoryView view) {
        super(view);
        statusService = new StatusService();
    }

    // MARK - Getter Methods
    public boolean hasMorePages() {
        return hasMorePages;
    }
    public boolean isLoading() {
        return isLoading;
    }


    // MARK - Class Methods
    public void getUserProfile(String userAlias) {
        userService.getUserProfile(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserProfileObserver());
    }

    public void loadMoreItems(User user) {
        statusService.loadMoreItemsStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetStoryObserver());
        isLoading = true;
        view.setLoadingFooter(true);
    }

    // MARK: - Inner Classes
    private class GetStoryObserver extends BaseObserver implements PagedNotificationObserver<Status> {

        public GetStoryObserver() {
            super("get story");
        }

        @Override
        public void handleSuccess(List<Status> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastStatus = (items.size() > 0) ? items.get(items.size() - 1) : null;
            view.addStatusesToStory((List<Status>) items);
            StoryPresenter.this.hasMorePages = hasMorePages;
        }

        @Override
        public void processFailure() {
            isLoading = false;
            view.setLoadingFooter(false);
        }
    }

    // TODO: Duplicates of this exist
    private class GetUserProfileObserver extends BaseObserver implements GetUserObserver {

        public GetUserProfileObserver() {
            super("get user's profile");
        }

        @Override
        public void handleSuccess(User user) {
            view.getUserProfile(user);
        }
    }
}