package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.CountNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.CountNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    // MARK - Call Methods
    public void loadMoreItemsFollowees(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, PagedNotificationObserver<User> getFollowingObserver) {
        // TODO: Combine this in 2B with the loardMoreItemsFollowers
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new PagedNotificationHandler<User>(getFollowingObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void loadMoreItemsFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, PagedNotificationObserver<User> getFollowersObserver) {
        // TODO: Combine this in 2B with the loadMoreItemsFollowees
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new PagedNotificationHandler<User>(getFollowersObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void getFollowCounts(AuthToken authToken, User selectedUser, CountNotificationObserver followersObserver, CountNotificationObserver followingObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                selectedUser, new CountNotificationHandler(followersObserver));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                selectedUser, new CountNotificationHandler(followingObserver));
        executor.execute(followingCountTask);
    }

    public void checkFollowRelationship(AuthToken authToken, User currentUser, User selectedUser, IsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, currentUser, selectedUser, new IsFollowerHandler(isFollowerObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public void followUser(AuthToken authToken, User selectedUser, SimpleNotificationObserver getFollowObserver) {
        FollowTask followTask = new FollowTask(authToken, selectedUser, new SimpleNotificationHandler(getFollowObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    public void unfollowUser(AuthToken authToken, User selectedUser, SimpleNotificationObserver getUnfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(getUnfollowObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }
}