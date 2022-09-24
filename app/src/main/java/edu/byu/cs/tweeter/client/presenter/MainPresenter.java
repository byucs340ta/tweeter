package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
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
        void postFollowUser();
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
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerObserver());
    }

    public void startFollowing(User selectedUser) {
        followService.followUser(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowObserver());
    }

    // Mark - Inner Classes
    private class GetLogoutObserver implements UserService.GetLogoutObserver {

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }

        @Override
        public void logoutUser() {
            view.postLogoutUser();
        }
    }

    private class GetFollowersCountObserver implements FollowService.GetFollowersCountObserver {

        @Override
        public void displayFollowersCount(int count) {
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

    private class GetFollowingCountObserver implements FollowService.GetFollowingCountObserver {

        @Override
        public void displayFollowingCount(int count) {
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

    private class IsFollowerObserver implements FollowService.IsFollowerObserver {
        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }

        @Override
        public void displayFollowingRelationship(boolean isFollower) {
            view.displayFollowingRelationship(isFollower);
        }
    }

    private class GetFollowObserver implements FollowService.GetFollowObserver {

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to follow: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to follow because of exception: " + ex.getMessage());
        }

        @Override
        public void postUserFollowed() {
            view.postFollowUser();
        }
    }
}
