package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;

public class MainPresenter {

    private View view;
    private FollowService followService;
    private UserService userService;
    private StatusService statusService;

    // MARK - Interface Methods
    public interface View {
        void displayMessage(String message);
        void postLogoutUser();
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
}
