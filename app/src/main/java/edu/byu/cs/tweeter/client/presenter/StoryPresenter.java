package edu.byu.cs.tweeter.client.presenter;

import android.net.Uri;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter implements StoryService.StoryObserver, UserService.GetUserObserver {
    private User user;
    private AuthToken token;
    private StoryView view;
    private boolean isLoading;
    private boolean hasMorePages;
    private Status lastStatus;

    public StoryPresenter(User user, AuthToken token, StoryView view) {
        this.user = user;
        this.token = token;
        this.view = view;
        isLoading = false;
        hasMorePages = true;
        lastStatus = null;
    }

    @Override
    public void handleStorySuccess(List<Status> statuses, boolean hasMorePages) {
        isLoading = false;
        view.setLoading(false);
        if (statuses.size() > 0) {
            lastStatus = statuses.get(statuses.size()-1);
        }
        else {
            lastStatus = null;
        }
        view.setStatuses(statuses);
    }

    @Override
    public void handleStoryFailure(String message) {
        isLoading = false;
        view.setLoading(false);
        view.displayMessage("Failed to get story: " + message);
    }

    @Override
    public void handleStoryThrewException(Exception ex) {
        isLoading = false;
        view.setLoading(false);
        view.displayMessage("Failed to get story because of exception: " + ex.getMessage());
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

    public interface StoryView {
        void setStatuses(List<Status> statuses);
        void setLoading(boolean loading);
        void displayMessage(String message);
        void navigateToUser(User user);
        void navigateToUri(Uri uri);
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
            new StoryService().getStory(token, user, 10, lastStatus, this);
        }
    }
    public void selectUser(String clickable) {
        if (clickable.contains("http")) {
            Uri uri = Uri.parse(clickable);
            view.navigateToUri(uri);
        }
        else {
            new UserService().getUser(token, clickable, this);
        }
    }
}
