package edu.byu.cs.tweeter.client.presenter;

import android.view.View;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.CountService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.PostService;
import edu.byu.cs.tweeter.client.model.service.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements UserService.LogoutObserver, CountService.GetFollowersObserver,
CountService.GetFollowingObserver, FollowService.addFollowerObserver, FollowService.removeFollowerObserver,
FollowService.isFollowerObserver, PostService.PostObserver {

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

        void updateFollowingandFollowersCount();
        void updateFollowingButton(boolean isFollowing);
        void setFollowButtonClickable(boolean canClick);

        void setFollowerCount(String count);
        void setFollowingCount(String count);

        void setFollowButtonVisibility(boolean isVisible);
        void setIsFollowerButton(boolean isFollower);

        void displayErrorMessage(String message);
        void clearErrorMessage();
        void displayInfoMessage(String message);
        void clearInfoMessage();
    }

    //******************************* Add Following ***************************//

    public void follow() {
        new FollowService().addFollower(authToken, targetUser, this);
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
        new CountService().countFollowers(authToken, targetUser, this);
    }

    @Override
    public void getFollowerCountSucceeded(int countNum) {
        String countString = Integer.toString(countNum);
        view.setFollowerCount(countString);
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
        new CountService().countFollowing(authToken, targetUser, this);
    }

    @Override
    public void getFollowingCountSucceeded(int countNum) {
        String countString = Integer.toString(countNum);
        view.setFollowingCount(countString);
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



    //***************************** is follower *************************//

    public void verifyIsFollower() {
        if (targetUser.compareTo(Cache.getInstance().getCurrUser()) == 0) {
            view.setFollowButtonVisibility(false);
        }
        else {
            view.setFollowButtonVisibility(true);
            new FollowService().isFollower(authToken, targetUser, this); // THIS IS AN OBSERVER WTF?!
        }
    }

    @Override
    public void IsFollowerSucceeded(boolean isFollower) {
        view.setIsFollowerButton(isFollower);
    }

    @Override
    public void IsFollowerFailed(String message) {
        view.displayErrorMessage("Failed to determine following relationship: " + message);
    }

    @Override
    public void IsFollowerThrewException(Exception ex) {
        view.displayErrorMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
    }



    //********************************** Post *********************************//



    public void postStatus(String post, User user, String formattedDateTime, List<String> URLs, List<String> mentions) {
        Status newStatus = new Status(post, targetUser, formattedDateTime, URLs, mentions);
        new PostService().run(newStatus, this);
        // todo implement observer from postTaskHandler here!!!
    }


    @Override
    public void PostSucceeded() {
        view.displayInfoMessage("Successfully Posted!");
    }


    @Override
    public void serviceFailure(String message) {
        view.displayErrorMessage(message);
    }



    //******************************** OTHER METHODS ********************************//

    public List<String> parseURLs(String post) throws MalformedURLException {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

}
