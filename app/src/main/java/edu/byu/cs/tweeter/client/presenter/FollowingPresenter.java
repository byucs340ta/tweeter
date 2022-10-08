package edu.byu.cs.tweeter.client.presenter;

import android.net.Uri;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter implements UserService.GetUserObserver, FollowService.FollowingObserver {
    private User user;
    private AuthToken token;
    private FollowingView view;
    private boolean isLoading;
    private boolean hasMorePages;
    private User lastFollowee;

    public FollowingPresenter(User user, AuthToken token, FollowingView view) {
        this.user = user;
        this.token = token;
        this.view = view;
        isLoading = false;
        hasMorePages = true;
        lastFollowee = null;
    }

    @Override
    public void handleGetUserSuccess(User user) {
        view.displayMessage("Getting user's profile...");
        view.navigateToUser(user);
    }

    @Override
    public void handleGetUserFailed(String message) {
        view.displayMessage("Failed to get user's profile: " + message);
    }

    @Override
    public void handleGetUserThrewException(Exception ex) {
        view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
    }

    @Override
    public void handleGetFollowingSuccess(List<User> following, boolean morePages) {
        isLoading = false;
        view.setLoading(false);
        if (following.size() > 0) {
            lastFollowee = following.get(following.size()-1);
        }
        else {
            lastFollowee = null;
        }
        view.addItems(following);
    }

    @Override
    public void handleGetFollowingFailure(String message) {
        isLoading = false;
        view.setLoading(false);
        view.displayMessage("Failed to get following: " + message);
    }

    @Override
    public void handleGetFollowingThrewException(Exception ex) {
        isLoading = false;
        view.setLoading(false);
        view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
    }

    public interface FollowingView {
        void addItems(List<User> following);
        void setLoading(boolean loading);
        void displayMessage(String message);
        void navigateToUser(User user);
    }

    public User getUser() {
        return  user;
    }
    public AuthToken getToken() {
        return  token;
    }
    public boolean isLoading() {
        return isLoading;
    }
    public boolean hasMorePage() {
        return hasMorePages;
    }
    public void loadItems() {
        if (!isLoading) {
            isLoading = true;
            view.setLoading(true);
            new FollowService().getFollowing(token, user, 10, lastFollowee, this);
        }
    }
    public void selectUser(String clickable) {
        new UserService().getUser(token, clickable, this);
    }
}
