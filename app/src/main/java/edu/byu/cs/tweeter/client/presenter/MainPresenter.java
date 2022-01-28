package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {



    public interface View {
        void displayErrorMessage(String message);
        void cancelPostingToast();
        void updateSelectedUserFollowingAndFollowers();
        void updateFollowButton(boolean b);
        void setFollowerCount(int count);
        void setFolloweeCount(int count);
        void cancelLogoutToast();
        void enableFollowButton(boolean b);
        void logoutUser();
    }

    private FollowService followService;
    private StatusService statusService;
    private UserService userService;
    private View view;

    public MainPresenter(View view) {
        this.view = view;
        this.statusService = new StatusService();
        this.followService = new FollowService();
        this.userService = new UserService();
    }

    public void postStatus(String post) throws ParseException {
        try {
            Status newStatus = new Status(post,  Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
            statusService.postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus, new PostStatusObserver());

        } catch (Exception e) {
            throw(e);
        }
    }

    public void unfollow(User user) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), user, new UnfollowObserver());
    }

    public void follow(User user) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), user, new FollowObserver());
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerObserver());
    }

    public void updateSelectedUserFollowingAndFollowers(User user) {
        followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(), user, new GetFollowersCountObserver(), new GetFollowingCountObserver());
    }

    public void logoutUser() {
        userService.logoutUser(new LogoutObserver());
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }


    // put all this crap in presenter
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



    public class PostStatusObserver implements StatusService.PostStatusObserver {

        @Override
        public void handleSuccess() {
            view.cancelPostingToast();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to post status because of an exception: " + exception.getMessage());
        }
    }

    public class UnfollowObserver implements FollowService.UnfollowObserver {

        @Override
        public void handleSuccess() {
            view.updateSelectedUserFollowingAndFollowers();
            view.updateFollowButton(true);
            view.enableFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to unfollow: " + message);
            view.enableFollowButton(true);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to unfollow because of exception: " + exception.getMessage());
            view.enableFollowButton(true);
        }
    }

    public class GetFollowersCountObserver implements FollowService.GetFollowersCountObserver {

        @Override
        public void handleSuccess(int count) {
            view.setFollowerCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get followers count because of exception: " + exception.getMessage());
        }
    }

    public class GetFollowingCountObserver implements FollowService.GetFollowingCountObserver {

        @Override
        public void handleSuccess(int count) {
            view.setFolloweeCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get following count because of exception: " + exception.getMessage());
        }
    }

    public class IsFollowerObserver implements FollowService.IsFollowerObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            view.updateFollowButton(!isFollower);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to determine following relationship because of exception: " + exception.getMessage());
        }
    }

    public class FollowObserver implements FollowService.FollowObserver {

        @Override
        public void handleSuccess() {
            view.updateSelectedUserFollowingAndFollowers();
            view.updateFollowButton(false);
            view.enableFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to follow: " + message);
            view.enableFollowButton(true);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to follow because of exception: " + exception.getMessage());
            view.enableFollowButton(true);
        }
    }

    public class LogoutObserver implements UserService.LogoutObserver {

        @Override
        public void handleSuccess() {
            view.cancelLogoutToast();
            view.logoutUser();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to logout because of exception: " + exception.getMessage());
        }
    }

}
