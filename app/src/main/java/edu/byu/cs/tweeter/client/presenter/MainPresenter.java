package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.AccountService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements AccountService.LogoutObserver, FollowService.FollowUserObserver, FollowService.UnfollowUserObserver,
                                        FollowService.GetNumFollowersObserver, FollowService.GetNumFollowingObserver, FollowService.IsFollowerObserver {
    private MainActivityView view;
    private User user;
    private AuthToken token;

    public MainPresenter(User user, AuthToken token, MainActivityView view) {
        this.user = user;
        this.token = token;
        this.view = view;
    }

    public User getUser() {
        return user;
    }

    public void updateSelectedUserFollowingAndFollowers() {
        new FollowService().updateSelectedUserFollowingAndFollowers(user, token, this, this);
    }

    public interface MainActivityView {
        void displayMessage(String message);
        void logoutUser();

        void setFollowButtonVisibility(boolean isVisible);
        void setFollowButtonEnabled(boolean isEnabled);
        void updateFollowButton(boolean isFollower);
        void updateNumFollowing(int count);
        void updateNumFollowers(int count);
    }

    @Override
    public void handleFollowUserSuccess() {
        new FollowService().updateSelectedUserFollowingAndFollowers(user, token, this, this);
        view.updateFollowButton(true);
        view.setFollowButtonEnabled(true);
    }

    @Override
    public void handleFollowUserFailure(String message) {
        view.displayMessage("Failed to follow: " + message);
        view.setFollowButtonEnabled(true);
    }

    @Override
    public void handleFollowUserThrewException(Exception ex) {
        view.displayMessage("Failed to follow because of exception: " + ex.getMessage());
        view.setFollowButtonEnabled(true);
    }

    @Override
    public void handleUnfollowUserSuccess() {
        new FollowService().updateSelectedUserFollowingAndFollowers(user, token, this, this);
        view.updateFollowButton(false);
        view.setFollowButtonEnabled(true);
    }

    @Override
    public void handleUnfollowUserFailure(String message) {
        view.displayMessage("Failed to unfollow: " + message);
        view.setFollowButtonEnabled(true);
    }

    @Override
    public void handleUnfollowUserThrewException(Exception ex) {
        view.displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
        view.setFollowButtonEnabled(true);
    }

    @Override
    public void handleGetNumFollowersSuccess(int num) {
        view.updateNumFollowers(num);
    }

    @Override
    public void handleGetNumFollowersFailure(String message) {
        view.displayMessage("Failed to get number of followers: " + message);
    }

    @Override
    public void handleGetNumFollowersThrewException(Exception ex) {
        view.displayMessage("Failed to get number of followers because of exception: " + ex.getMessage());
    }

    @Override
    public void handleGetNumFollowingSuccess(int num) {
        view.updateNumFollowing(num);
    }

    @Override
    public void handleGetNumFollowingFailure(String message) {
        view.displayMessage("Failed to get number of followees: " + message);
    }

    @Override
    public void handleGetNumFollowingThrewException(Exception ex) {
        view.displayMessage("Failed to get number of followees because of exception: " + ex.getMessage());
    }

    @Override
    public void handleIsFollowerSuccess(boolean isFollower) {
        view.updateFollowButton(isFollower);
    }

    @Override
    public void handleIsFollowerFailure(String message) {
        view.displayMessage("Failed to find if follower: " + message);
    }

    @Override
    public void handleIsFollowerThrewException(Exception ex) {
        view.displayMessage("Failed to find if follower because of exception: " + ex.getMessage());
    }

    @Override
    public void handleLogoutSuccess() {
        view.logoutUser();
    }

    @Override
    public void handleLogoutFailure(String message) {
        view.displayMessage("Failed to logout: " + message);
    }

    @Override
    public void handleLogoutThrewException(Exception ex) {
        view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
    }

    public void logout(){
        view.displayMessage("Logging Out...");
        new AccountService().logout(token, this);
    }

    public void followButtonClick(String buttonText) {
        view.setFollowButtonEnabled(false);
        if (buttonText.equals("Following")) {
            new FollowService().unfollowUser(token, user, this);
            view.displayMessage("Unfollowing " + user.getName() + "...");
        }
        else {
            new FollowService().followUser(token, Cache.getInstance().getCurrUser(), user, this);
            view.displayMessage("Following " + user.getName() + "...");
        }
    }

    public void  checkIfFollower() {
        if (user.compareTo(Cache.getInstance().getCurrUser()) == 0) {
            view.setFollowButtonVisibility(false);
        }
        else {
            view.setFollowButtonVisibility(true);
            new FollowService().isFollower(token, user, Cache.getInstance().getCurrUser(), this);
        }
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
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
