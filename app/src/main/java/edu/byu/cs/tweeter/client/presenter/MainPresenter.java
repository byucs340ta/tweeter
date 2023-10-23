package edu.byu.cs.tweeter.client.presenter;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.FollowObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.GetFollowingAndFollowersCountObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PostObserver;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.utils.StatusUtil;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends AuthenticatedPresenter implements LogoutObserver, FollowObserver, IsFollowerObserver, PostObserver, GetFollowingAndFollowersCountObserver {
    public interface View extends AuthenticatedPresenter.View {
        void loginActivity();
        void updateFollowButton(boolean removed);
        void followButtonSetEnabled();
        void isFollower(boolean isFollower);
        void setFollowersCount(int followersCount);
        void setFollowingCount(int followingCount);
    }
    protected View view;
    public MainPresenter(View view, User user) {
        super(view, user);
        this.view = view;
    }

    @Override
    public void handleFailure(String message) {
        view.showErrorMessage("Failed to " + this.errorMessage + ": " + message);
        view.followButtonSetEnabled();
    }

    @Override
    public void handleException(Exception exception) {
        view.showErrorMessage("Failed to " + this.errorMessage + " because of exception: " + exception.getMessage());
        view.followButtonSetEnabled();
    }

    @Override
    public void unfollowSucceeded() {
        this.updateSelectedUserFollowingAndFollowers();
        view.updateFollowButton(true);
        view.followButtonSetEnabled();
    }

    @Override
    public void followSucceeded() {
        this.updateSelectedUserFollowingAndFollowers();
        view.updateFollowButton(false);
        view.followButtonSetEnabled();
    }

    @Override
    public void isFollowerSucceeded(boolean isFollower) {
        view.isFollower(isFollower);
    }

    @Override
    public void logoutSucceeded() {
        view.loginActivity();
    }
    @Override
    public void postSucceeded() {
        view.showInfoMessage("Successfully Posted!");
    }
    @Override
    public void getFollowersCountSucceeded(int followersCount) {
        view.setFollowersCount(followersCount);
    }

    @Override
    public void getFollowingCountSucceeded(int followingCount) {
        view.setFollowingCount(followingCount);
    }
    public void unfollow(User selectedUser) {
        var followService = new FollowService();
        this.errorMessage = "unfollow";
        followService.unfollow(selectedUser, this);
        view.showInfoMessage("Removing " + selectedUser.getName() + "...");
    }

    public void follow(User selectedUser) {
        var followService = new FollowService();
        this.errorMessage = "follow";
        followService.follow(selectedUser, this);
        view.showInfoMessage("Adding " + selectedUser.getName() + "...");
    }
    public void updateSelectedUserFollowingAndFollowers() {
        var followService = new FollowService();
        this.errorMessage = "update following and followers count";
        followService.updateFollowingAndFollowersCount(user, this);
    }

    public void isFollower() {
        var followService = new FollowService();
        this.errorMessage = "determine following relationship";
        followService.isFollower(user, this);
    }
    public void logout() {
        var logoutService = new UserService();
        this.errorMessage = "logout";
        logoutService.logout(this);
    }
//
    public void postStatus(String post, User user, long time) {
        view.showInfoMessage("Posting Status...");
        this.errorMessage = "post status";
        Status status = new Status(post, user, time, StatusUtil.parseURLs(post), StatusUtil.parseMentions(post));
        var service = createStatusService();
        service.postStatus(status, this);
    }

    public StatusService createStatusService() {
        return new StatusService();
    }
}
