package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {

    // MARK - Class Variables
    private View view;
    private static final int PAGE_SIZE = 10;
    private FollowService followService;
    private UserService userService;
    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;

    // MARK - Interface Methods
    public interface View {
        void displayMessage(String message);
        void setLoadingFooter(boolean value);
        void addFollowers(List<User> followers);
        void getUserProfile(User user);
    }

    // MARK - Class constructor
    public FollowersPresenter(View view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
    }

    // MARK - Getter Methods
    public boolean isLoading() {
        return isLoading;
    }
    public boolean hasMorePages() {
        return hasMorePages;
    }

    // MARK - Methods
    public void getUserProfile(String userAlias) {
        userService.getUserProfile(Cache.getInstance().getCurrUserAuthToken(), userAlias, new FollowersPresenter.GetUserProfileObserver());
    }

    public void loadMoreItems(User user) {
        isLoading = true;
        view.setLoadingFooter(true);
        followService.loadMoreItemsFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new GetFollowersObserver());
    }

    // MARK - Inner Classes
    private class GetFollowersObserver implements FollowService.GetFollowersObserver {
        @Override
        public void addFollowers(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            view.addFollowers(followers);
            FollowersPresenter.this.hasMorePages = hasMorePages;
        }

        @Override
        public void displayErrorMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get followers: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get followers because of exception: " + ex.getMessage());
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
        public void getUserProfile(User user) {
            view.getUserProfile(user);
        }
    }
}