package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {

    // MARK: - Class Variables

    private static final int PAGE_SIZE = 10;
    private StatusService statusService;
    private UserService userService;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private View view;

    public interface View {
        void displayMessage(String message);
        void setLoadingFooter(boolean value);
        void addStatusesToStory(List<Status> statuses);
        void getUserProfile(User user);
    }

    // MARK - Constructor
    public StoryPresenter(View view) {
        this.view = view;
        statusService = new StatusService();
        userService = new UserService();
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
        isLoading = true;
        view.setLoadingFooter(true);
        statusService.loadMoreItemsStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetStoryObserver());
    }


    // MARK: - Inner Classes
    private class GetStoryObserver implements PagedNotificationObserver<Status> {

        @Override
        public void displayErrorMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get story: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get story because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(List<Status> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastStatus = (items.size() > 0) ? items.get(items.size() - 1) : null;
            view.addStatusesToStory((List<Status>) items);
            StoryPresenter.this.hasMorePages = hasMorePages;
        }
    }

    // TODO: Duplicates of this exist
    private class GetUserProfileObserver implements GetUserObserver {

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }

        // TODO: I have this duplicated 3 other places..
        @Override
        public void handleSuccess(User user) {
            view.getUserProfile(user);
        }
    }
}