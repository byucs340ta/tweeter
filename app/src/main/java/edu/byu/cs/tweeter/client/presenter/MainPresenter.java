package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.CountNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter<MainPresenter.MainView> {

    // TODO: Move these to the basePresenter class and prepare them to be mocked.
    private FollowService followService;
    private StatusService statusService;

    // MARK - Interface Methods
    public interface MainView extends BasePresenter.View {
        void postLogoutUser();
        void displayFollowersCount(int count);
        void displayFollowingCount(int count);
        void displayFollowingRelationship(boolean isFollower);
        void postFollowUserStatusChange(boolean canFollowUser);
        void postStatusPosted();
    }

    // MARK - Class Constructor
    public MainPresenter(MainView view) {
        super(view);
        followService = new FollowService();
        statusService = new StatusService();
    }

    // MARK - Methods

    public void logoutUser() {
        getUserService().logoutUser(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
    }

    public void getFollowCounts(User selectedUser) {
        followService.getFollowCounts(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                new GetFollowersCountObserver(), new GetFollowingCountObserver());
    }

    public void checkFollowRelationship(User selectedUser) {
        followService.checkFollowRelationship(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new GetIsFollowerObserver());
    }

    public void startFollowing(User selectedUser) {
        followService.followUser(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowObserver());
    }

    public void unfollowUser(User selectedUser) {
        followService.unfollowUser(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetUnfollowObserver());
    }

    public void postStatus(String post) throws ParseException {
        statusService.postStatus(post, Cache.getInstance().getCurrUser(), new PostStatusObserver());
    }

    // Mark - Inner Classes

    private class GetFollowersCountObserver extends BaseObserver implements CountNotificationObserver {
        public GetFollowersCountObserver() {
            super("get followers count");
        }
        @Override
        public void handleSuccess(int count) {
            view.displayFollowersCount(count);
        }
    }

    private class GetFollowingCountObserver extends BaseObserver implements CountNotificationObserver {
        public GetFollowingCountObserver() {
            super("get following count");
        }
        @Override
        public void handleSuccess(int count) {
            view.displayFollowingCount(count);
        }
    }


    private class GetIsFollowerObserver extends BaseObserver implements IsFollowerObserver {
        public GetIsFollowerObserver() {
            super("determine following relationship");
        }
        @Override
        public void handleSuccess(boolean isFollower) {
            view.displayFollowingRelationship(isFollower);
        }
    }

    private class GetFollowObserver extends BaseObserver implements SimpleNotificationObserver {
        public GetFollowObserver() {
            super("follow");
        }
        @Override
        public void handleSuccess() {
            view.postFollowUserStatusChange(false);
        }
    }

    private class GetUnfollowObserver extends BaseObserver implements SimpleNotificationObserver {
        public GetUnfollowObserver() {
            super("unfollow");
        }
        @Override
        public void handleSuccess() {
            view.postFollowUserStatusChange(true);
        }
    }

    private class PostStatusObserver extends BaseObserver implements SimpleNotificationObserver {
        public PostStatusObserver() {
            super("post status");
        }
        @Override
        public void handleSuccess() {
            view.postStatusPosted();
        }
    }

    // MARK - Inner Classes
    public class LogoutObserver extends BaseObserver implements SimpleNotificationObserver {
        public LogoutObserver() {
            super("logout");
        }

        @Override
        public void handleSuccess() {
            // view.clearInfoMessage();
            //Clear user data (cached data).
            Cache.getInstance().clearCache();
            view.postLogoutUser();
        }
    }
}