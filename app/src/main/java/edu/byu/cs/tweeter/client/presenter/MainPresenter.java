package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.PostService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements FollowService.UnfollowObserver, FollowService.FollowObserver, FollowService.GetFollowersCountObserver, FollowService.GetFollowingCountObserver, FollowService.IsFollowerObserver, UserService.LogoutObserver, PostService.PostObserver {
    @Override
    public void unfollowSucceeded() {
        this.updateSelectedUserFollowingAndFollowers();
        view.updateFollowButton(true);
        view.followButtonSetEnabled();
    }

    @Override
    public void unfollowFailed(String message) {
        view.showErrorMessage(message);
        view.followButtonSetEnabled();
    }

    @Override
    public void followSucceeded() {
        this.updateSelectedUserFollowingAndFollowers();
        view.updateFollowButton(false);
        view.followButtonSetEnabled();
    }

    @Override
    public void followFailed(String message) {
        view.showErrorMessage(message);
        view.followButtonSetEnabled();
    }

    @Override
    public void getFollowersCountSucceeded(int count) {
        view.setFollowerCount(count);
    }

    @Override
    public void getFollowersCountFailed(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void getFollowingCountSucceeded(int count) {
        view.setFollowingCount(count);
    }

    @Override
    public void getFollowingCountFailed(String message) {
        view.showErrorMessage(message);
    }


    @Override
    public void isFollowerSucceeded(boolean isFollower) {
        view.isFollower(isFollower);
    }

    @Override
    public void isFollowerFailed(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void logoutSucceeded() {
        view.loginActivity();
    }

    @Override
    public void logoutFailed(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void postSucceeded() {
        view.showInfoMessage("Successfully Posted!");
    }

    @Override
    public void postFailed(String message) {
        view.showErrorMessage(message);
    }

    public interface View {
        void showInfoMessage(String message);
        void hideInfoMessage();
        void showErrorMessage(String message);
        void hideErrorMessage();

        void openMainView(User user);
        void updateFollowButton(boolean removed);
        void followButtonSetEnabled();
        void isFollower(boolean isFollower);
        void loginActivity();
        void setFollowerCount(int count);
        void setFollowingCount(int count);
    }
    private final View view;
    private final User selectedUser;
    public MainPresenter(View view, User selectedUser) {
        this.view = view;
        this.selectedUser = selectedUser;
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
        followService.updateSelectedUserFollowingAndFollowers(selectedUser, this, this);
    }

    public void isFollower() {
        var followService = new FollowService();
        followService.isFollower(selectedUser, this);
    }

    public void logout() {
        var userService = new UserService();
        userService.logout(this);
    }

    public void postStatus(String post) {
        view.showInfoMessage("Posting Status...");
        var postService = new PostService();
        postService.postStatus(post, this);
    }
}
