package edu.byu.cs.tweeter.client.presenter;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.CountService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.PostService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PostObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SetFollowingObserver;
import edu.byu.cs.tweeter.client.presenter.presenter.views.BaseView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter implements LogoutObserver, SetFollowingObserver,
PostObserver, CountObserver, IsFollowerObserver {

    public MainPresenter(View view, AuthToken authToken, User targetUser) {
        super(authToken, targetUser);
        this.view = view;
    }

    private MainPresenter.View view;

    public interface View extends BaseView {
        void logout();

        void updateFollowingandFollowersCount();
        void updateFollowingButton(boolean isFollowing);
        void setFollowButtonClickable(boolean canClick);

        void setFollowerCount(String count);
        void setFollowingCount(String count);

        void setFollowButtonVisibility(boolean isVisible);
        void setIsFollowerButton(boolean isFollower);
    }

    // This responds to when ANY observer fails.
    @Override
    public void serviceFailure(String message) {
        view.displayErrorMessage(message);
    }



    //******************************* Add and Remove Following ***************************//
    ///////////////////////////////////////////////////////////////////////////////////////

    public void follow() {
        new FollowService().addFollower(authToken, targetUser, this);
    }

    public void unfollow() {
        new FollowService().removeFollower(authToken, targetUser, this);
    }

    @Override
    public void SetFollowingSucceeded(boolean isFollowing) {
        view.updateFollowingandFollowersCount();
        view.updateFollowingButton(isFollowing);
        view.setFollowButtonClickable(true);
    }



    //************************** Followers and Following Count *************************//
    /////////////////////////////////////////////////////////////////////////////////////

    public void countFollowersAndFollowing()
    {
        new CountService().countFollowersAndFollowing(authToken, targetUser, this);
    }

    @Override
    public void CountSucceeded(int followersCount, int followingCount) {
        view.setFollowerCount(String.valueOf(followersCount));
        view.setFollowingCount(String.valueOf(followingCount));
    }



    //******************************* Logout *********************************//
    ///////////////////////////////////////////////////////////////////////////

    public void logout() {
        view.clearErrorMessage();
        view.clearInfoMessage();
        new UserService().logout(authToken, this); // THIS IS AN OBSERVER WTF?!
    }

    @Override
    public void LogoutSucceeded() {
        view.logout();
        view.clearErrorMessage();
        view.displayInfoMessage("Successfully logged out.");
    }



    //***************************** is follower *************************/
    /////////////////////////////////////////////////////////////////////

    public void verifyIsFollower() {
        if (targetUser.compareTo(Cache.getInstance().getCurrUser()) == 0) {
            view.setFollowButtonVisibility(false);
        }
        else {
            view.setFollowButtonVisibility(true);
            User follower = new User();
            new FollowService().isFollower(authToken, targetUser, follower,this);
        }
    }

    @Override
    public void IsFollowerSucceeded(boolean isFollower) {
        view.setIsFollowerButton(isFollower);
    }



    //********************************** Post *********************************//
    ////////////////////////////////////////////////////////////////////////////

    public void postStatus(String post, User user, String formattedDateTime, List<String> URLs, List<String> mentions) {
        Status newStatus = new Status(post, targetUser, formattedDateTime, URLs, mentions);
        new PostService().run(newStatus, this);
    }

    @Override
    public void PostSucceeded()  {
        view.displayInfoMessage("Successfully Posted!");
    }



    //******************************** OTHER METHODS ********************************//
    //////////////////////////////////////////////////////////////////////////////////

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
