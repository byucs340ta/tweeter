package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends BasePresenter<FeedPresenter.FeedView> {

    // MARK - Class Variables
    private static final int PAGE_SIZE = 10;
    private StatusService statusService;
    private Status lastStatus;
    private boolean hasMoreStatuses;
    private boolean isLoading = false;

    // MARK - Interface Classes
    public interface FeedView extends BasePresenter.View {
        void displayMessage(String message);
        void setLoadingFooter(boolean value);
        void addStatusToFeed(List<Status> statuses);
        void getUserProfile(User user);
    }

    // MARK - Constructor
    public FeedPresenter(FeedView view) {
        super(view);
        statusService = new StatusService();
    }

    // MARK - Getter Methods
    public boolean hasMorePages() {
        return hasMoreStatuses;
    }
    public boolean isLoading() {
        return isLoading;
    }

    // MARK - Class Methods
    public void getUserProfile(String userAlias) {
        userService.getUserProfile(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserProfileObserver());
    }

    public void loadMoreItems(User user) {
        statusService.loadMoreItemsFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetFeedObserver());
        isLoading = true;
        view.setLoadingFooter(true);
    }

    // MARK - Inner Classes
    private class GetFeedObserver extends BaseObserver implements PagedNotificationObserver<Status> {
        public GetFeedObserver() {
            super("get feed");
        }

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMoreStatuses) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addStatusToFeed(statuses);
            FeedPresenter.this.hasMoreStatuses = hasMoreStatuses;
        }

        @Override
        public void processFailure() {
            isLoading = false;
            view.setLoadingFooter(false);
        }
    }

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