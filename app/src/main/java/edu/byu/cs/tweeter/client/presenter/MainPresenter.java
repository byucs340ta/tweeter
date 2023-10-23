package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.FollowObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.GetFollowingAndFollowersCountObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PostObserver;
import edu.byu.cs.tweeter.client.model.services.newservices.FollowService;
import edu.byu.cs.tweeter.client.model.services.newservices.StatusService;
import edu.byu.cs.tweeter.client.model.services.newservices.UserService;
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
        view.showErrorMessage(message);
        view.followButtonSetEnabled();
    }

    @Override
    public void handleException(Exception exception) {
        view.showErrorMessage(exception.getMessage());
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
        followService.unfollow(selectedUser, this);
        view.showInfoMessage("Removing " + selectedUser.getName() + "...");
    }

    public void follow(User selectedUser) {
        var followService = new FollowService();
        followService.follow(selectedUser, this);
        view.showInfoMessage("Adding " + selectedUser.getName() + "...");
    }
    public void updateSelectedUserFollowingAndFollowers() {
        var followService = new FollowService();
        followService.updateFollowingAndFollowersCount(user, this);
    }

    public void isFollower() {
        var followService = new FollowService();
        followService.isFollower(user, this);
    }
    public void logout() {
        var logoutService = new UserService();
        logoutService.logout(this);
    }
//
    public void postStatus(String post) {
        view.showInfoMessage("Posting Status...");
        var postService = new StatusService();
        postService.postStatus(post, this);
    }
}
