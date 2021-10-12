package edu.byu.cs.tweeter.client.presenter;

import android.widget.TextView;

import edu.byu.cs.tweeter.client.model.service.CountService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements UserService.LogoutObserver, CountService.GetFollowersObserver,
CountService.GetFollowingObserver, FollowService.addFollowerObserver, FollowService.removeFollowerObserver{

    public MainPresenter(View view, AuthToken authToken, User targetUser) {
        this.view = view;
        this.authToken = authToken;
        this.targetUser = targetUser;
    }

    private MainPresenter.View view;
    private AuthToken authToken;
    private User targetUser;

    public interface View {
        void logout();

        // todo: all of the other features in main, etc...
        void updateFollowingandFollowersCount();
        void updateFollowingButton(boolean isFollowing);
        void setFollowButtonClickable(boolean canClick);

        void setFollowerCount(String count);
        void setFollowingCount(String count);

        void displayErrorMessage(String message);
        void clearErrorMessage();
        void displayInfoMessage(String message);
        void clearInfoMessage();
    }

    //******************************* Add Following ***************************//

    public void follow() {
        new FollowService().addFollower(authToken, targetUser, this);
//            UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
//                    selectedUser, new MainActivity.UnfollowHandler());
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            executor.execute(unfollowTask);
//            view.displayInfoMessage("Removing " + selectedUser.getName() + "...");
//            Toast.makeText(MainActivity.this, "Removing " + selectedUser.getName() + "...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void AddFollowersSucceeded() {
        view.updateFollowingandFollowersCount();
        view.updateFollowingButton(true);
        view.setFollowButtonClickable(true);
    }

    @Override
    public void AddFollowersFailed(String message) {
        view.displayErrorMessage("Failed to follow: " + message);
        view.setFollowButtonClickable(true);
    }

    @Override
    public void AddFollowersThrewException(Exception ex) {
        view.displayErrorMessage("Failed to follow because of exception: " + ex.getMessage());
        view.setFollowButtonClickable(true);
    }



    //******************************* Remove Following ***************************//

    public void unfollow() {
        new FollowService().removeFollower(authToken, targetUser, this);

//        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
//                    selectedUser, new MainActivity.FollowHandler());
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            executor.execute(followTask);
//            view.displayInfoMessage("Adding " + selectedUser.getName() + "...");
//            Toast.makeText(MainActivity.this, "Adding " + selectedUser.getName() + "...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void RemoveFollowersSucceeded() {
        view.updateFollowingandFollowersCount();
        view.updateFollowingButton(false);
        view.setFollowButtonClickable(true);
    }

    @Override
    public void RemoveFollowersFailed(String message) {
        view.displayErrorMessage("Failed to un-follow: " + message);
        view.setFollowButtonClickable(true);
    }

    @Override
    public void RemoveFollowersThrewException(Exception ex) {
        view.displayErrorMessage("Failed to un-follow because of exception: " + ex.getMessage());
        view.setFollowButtonClickable(true);
    }



    //******************************* Followers Count *********************************//
    public void countFollowers() {
        // todo
        new CountService().countFollowers(authToken, targetUser, this);
//        new CountService.GetFollowersObserver(authToken, targetUser, this);
    }

    @Override
    public void getFollowerCountSucceeded(int countNum) {
        // add something?
        String countString = Integer.toString(countNum);
        view.setFollowerCount(countString);
        view.updateFollowingandFollowersCount();
    }

    @Override
    public void getFollowerCountFailed(String message) {
        view.displayErrorMessage("Failed to get followers count: " + message);
    }

    @Override
    public void getFollowerCountThrewException(Exception ex) {
        view.displayErrorMessage("Failed to get followers count because of exception: " + ex.getMessage());
    }

    //***************************** Following Count  **********************************//

    public void countFollowing() {
        // todo
        new CountService().countFollowing(authToken, targetUser, this);
    }

    @Override
    public void getFollowingCountSucceeded(int countNum) {
        String countString = Integer.toString(countNum);
        view.setFollowingCount(countString);
        view.updateFollowingandFollowersCount();
    }

    @Override
    public void getFollowingCountFailed(String message) {
        view.displayErrorMessage("Failed to get following count: " + message);
    }

    @Override
    public void getFollowingCountThrewException(Exception ex) {
        view.displayErrorMessage("Failed to get following count because of exception: " + ex.getMessage());
    }



    //******************************* Logout *********************************//

    public void logout() {
        view.clearErrorMessage();
        view.clearInfoMessage();
        new UserService().logout(this); // THIS IS AN OBSERVER WTF?!
    }

    @Override
    public void logoutSucceeded() {
        view.logout();
        view.clearErrorMessage();
        view.displayInfoMessage("Successfully logged out.");
    }

    @Override
    public void logoutFailed(String message) {
        view.displayErrorMessage("Failed to logout: " + message);
    }

    @Override
    public void logoutThrewException(Exception ex) {
        view.displayErrorMessage("Failed to logout because of exception: " + ex.getMessage());
    }
}
