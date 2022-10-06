package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.CountNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {

    private View view;
    private FollowService followService;
    private UserService userService;
    private StatusService statusService;

    // MARK - Interface Methods
    public interface View {
        void displayMessage(String message);
        void postLogoutUser();
        void displayFollowersCount(int count);
        void displayFollowingCount(int count);
        void displayFollowingRelationship(boolean isFollower);
        void postFollowUserStatusChange(boolean canFollowUser);
        void postStatusPosted();
    }

    // MARK - Class Constructor
    public MainPresenter(View view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
        statusService = new StatusService();
    }

    // MARK - Methods

    public void logoutUser() {
        userService.logoutUser(Cache.getInstance().getCurrUserAuthToken(), new GetLogoutObserver());
        //Clear user data (cached data).
        Cache.getInstance().clearCache();
    }

    public void getFollowCounts(User selectedUser) {
        followService.getFollowCounts(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                new GetFollowersCountObserver(), new GetFollowingCountObserver());
    }

    public void checkFollowRelationship(User selectedUser) {
        followService.checkFollowRelationship(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new GetIsFollowerObserver());
    }

    public void startFollowing(User selectedUser) {
        followService.followUser(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowObserver());
    }

    public void unfollowUser(User selectedUser) {
        followService.unfollowUser(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetUnfollowObserver());
    }

    public void postStatus(String post) throws ParseException {
        statusService.postStatus(post, Cache.getInstance().getCurrUser(), new PostStatusObserver());
    }

    // Mark - Inner Classes
    private class GetLogoutObserver implements SimpleNotificationObserver {

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess() {
            view.postLogoutUser();
        }
    }

    private class GetFollowersCountObserver implements CountNotificationObserver {

        @Override
        public void handleSuccess(int count) {
            view.displayFollowersCount(count);
        }

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to get followers count: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get followers count because of exception: " + ex.getMessage());
        }
    }

    private class GetFollowingCountObserver implements CountNotificationObserver {

        @Override
        public void handleSuccess(int count) {
            view.displayFollowingCount(count);
        }

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to get following count: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get following count because of exception: " + ex.getMessage());
        }
    }

    private class GetIsFollowerObserver implements IsFollowerObserver {
        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(boolean isFollower) {
            view.displayFollowingRelationship(isFollower);
        }
    }

    private class GetFollowObserver implements SimpleNotificationObserver {

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to follow: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to follow because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess() {
            view.postFollowUserStatusChange(false);
        }
    }

    private class GetUnfollowObserver implements SimpleNotificationObserver {
        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to unfollow: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess() {
            view.postFollowUserStatusChange(true);
        }
    }

    private class PostStatusObserver implements SimpleNotificationObserver {
        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to post status: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to post status because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess() {
            view.postStatusPosted();
        }
    }
}