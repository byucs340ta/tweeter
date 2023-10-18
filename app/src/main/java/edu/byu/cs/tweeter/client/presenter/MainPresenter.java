package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.PostService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements FollowService.UnfollowObserver, FollowService.FollowObserver, FollowService.GetFollowersCountObserver, FollowService.GetFollowingCountObserver, FollowService.IsFollowerObserver, UserService.LogoutObserver, PostService.PostObserver {
    @Override
    public void unfollowSucceeded() {
        this.updateSelectedUserFollowingAndFollowers();
        view.updateFollowButton(true);
        view.followButtonSetEnabled(true);
    }

    @Override
    public void unfollowFailed(String message) {
        view.showErrorMessage(message);
        view.followButtonSetEnabled(true);
    }

    @Override
    public void getFollowersCountSucceeded(int count) {

    }

    @Override
    public void getFollowersCountFailed(String message) {

    }

    @Override
    public void getFollowingCountSucceeded(int count) {

    }

    @Override
    public void getFollowingCountFailed(String message) {

    }

    @Override
    public void followSucceeded() {

    }

    @Override
    public void followFailed(String message) {

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

    }

    @Override
    public void postFailed(String message) {

    }

    public interface View {
        void showInfoMessage(String message);
        void hideInfoMessage();
        void showErrorMessage(String message);
        void hideErrorMessage();

        void openMainView(User user);
        void updateFollowButton(boolean removed);
        void followButtonSetEnabled(boolean enabled);
        void isFollower(boolean isFollower);
        void loginActivity();
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
