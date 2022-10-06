package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {

    // MARK - Class Variables
    private static final int PAGE_SIZE = 10;
    private View view;
    private StatusService statusService;
    private UserService userService;
    private Status lastStatus;
    private boolean hasMoreStatuses;
    private boolean isLoading = false;

    // MARK - Interface Classes
    public interface View {
        void displayMessage(String message);
        void setLoadingFooter(boolean value);
        void addStatusToFeed(List<Status> statuses);
        void getUserProfile(User user);
    }

    // MARK - Constructor
    public FeedPresenter(View view) {
        this.view = view;
        statusService = new StatusService();
        userService = new UserService();
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
        isLoading = true;
        view.setLoadingFooter(true);
        statusService.loadMoreItemsFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetFeedObserver());
    }

    // MARK - Inner Classes
    private class GetFeedObserver implements StatusService.GetFeedObserver {
        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMoreStatuses) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addStatusToFeed(statuses);
            FeedPresenter.this.hasMoreStatuses = hasMoreStatuses;
        }

        @Override
        public void displayErrorMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get feed: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get feed because of exception: " + ex.getMessage());
        }
    }

    private class GetUserProfileObserver implements UserService.GetUserProfileObserver {

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(User user) {
            view.getUserProfile(user);
        }
    }
}
