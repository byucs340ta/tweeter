package edu.byu.cs.tweeter.client.presenter;

import java.net.MalformedURLException;
import java.text.ParseException;

import edu.byu.cs.tweeter.client.model.service.MainService;
import edu.byu.cs.tweeter.client.model.service.PostStatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements UserService.LogoutObserver, PostStatusService.PostStatusObserver, MainService.FollowObserver, MainService.UnfollowObserver,
        MainService.GetFollowersCountObserver, MainService.GetFollowingCountObserver, MainService.IsFollowerObserver {

    public interface View {
        void displayErrorMessage(String message);
        void displayInfoMessage(String message);

        void logout();

        void updateFollowButton(boolean removed);
        void setFollowButton(boolean enabled);

        void setFollowerCount(int count);
        void setFollowingCount(int count);

        void setIsFollowerButton();
        void setIsNotFollowerButton();
    }

    private View view;

    public MainPresenter(View view) {
        this.view = view;
    }

    public void logout() {
        view.displayInfoMessage("Logging Out...");
        new UserService().logout(this);
    }

    public void postStatus(String post) throws ParseException, MalformedURLException {
        view.displayInfoMessage("Posting Status...");
        new PostStatusService().postStatus(post, this);
    }

    public void isFollower() {
        new MainService().isFollower(this);
    }

    @Override
    public void handleSuccessLogout() {
        view.logout();
    }

    @Override
    public void handleFailureLogout(String message) {
        view.displayErrorMessage("Failed to logout: " + message);
    }

    @Override
    public void handleExceptionLogout(Exception e) {
        view.displayErrorMessage("Failed to logout because of exception: " + e.getMessage());
    }

    @Override
    public void handleSuccessPostStatus(String message) {
        view.displayInfoMessage(message);
    }

    @Override
    public void handleFailurePostStatus(String message) {
        view.displayErrorMessage("Failed to post status: " + message);
    }

    @Override
    public void handleExceptionPostStatus(Exception e) {
        view.displayErrorMessage("Failed to post status because of exception: " + e.getMessage());
    }

    @Override
    public void handleSuccessFollow(User user) {
        view.displayInfoMessage("Adding " + user.getName() + "...");
        new MainService().follow(this, user);
    }

    @Override
    public void handleFailureFollow(String message) {
        view.displayErrorMessage("Failed to follow: " + message);
    }

    @Override
    public void handleExceptionFollow(Exception e) {
        view.displayErrorMessage("Failed to follow because of exception: " + e.getMessage());
    }

    @Override
    public void handleSuccessUnfollow(User user) {
        view.displayErrorMessage("Unfollowing " + user.getName() + "...");
        new MainService().unfollow(this, user);
    }

    @Override
    public void handleFailureUnfollow(String message) {
        view.displayErrorMessage("Failed to unfollow: " + message);
    }

    @Override
    public void handleExceptionUnfollow(Exception e) {
        view.displayErrorMessage("Failed to unfollow because of exception: " + e.getMessage());
    }

    @Override
    public void setFollowButton(boolean enabled) {
        view.setFollowButton(enabled);
    }

    @Override
    public void handleUpdateFollowButton(boolean removed) {
        view.updateFollowButton(removed);
    }

    @Override
    public void updateSelectedUserFollowingAndFollowers(User user) {
        new MainService().updateSelectedUserFollowingAndFollowers(this, this, user);
    }

    @Override
    public void handleSuccessGetFollowersCount(User user) {

    }

    @Override
    public void handleFailureGetFollowersCount(String message) {
        view.displayErrorMessage("Failed to get followers count: " + message);
    }

    @Override
    public void handleExceptionGetFollowersCount(Exception e) {
        view.displayErrorMessage("Failed to get followers count because of exception: " + e.getMessage());
    }

    @Override
    public void setFollowersCount(int count) {
        view.setFollowerCount(count);
    }

    @Override
    public void handleSuccessGetFollowingCount(User user) {

    }

    @Override
    public void handleFailureGetFollowingCount(String message) {
        view.displayErrorMessage("Failed to get following count: " + message);
    }

    @Override
    public void handleExceptionGetFollowingCount(Exception e) {
        view.displayErrorMessage("Failed to get following count because of exception: " + e.getMessage());
    }

    @Override
    public void setFollowingCount(int count) {
        view.setFollowingCount(count);
    }

    @Override
    public void handleSuccessIsFollow(User user) {

    }

    @Override
    public void handleFailureIsFollow(String message) {
        view.displayErrorMessage("Failed to determine following relationship: " + message);
    }

    @Override
    public void handleExceptionIsFollow(Exception e) {
        view.displayErrorMessage("Failed to determine following relationship because of exception: " + e.getMessage());
    }

    @Override
    public void setIsFollowerButton() {
        view.setIsFollowerButton();
    }

    @Override
    public void setIsNotFollowerButton() {
        view.setIsNotFollowerButton();
    }
}
