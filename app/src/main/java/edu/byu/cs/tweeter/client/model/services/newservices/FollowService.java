package edu.byu.cs.tweeter.client.model.services.newservices;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetUsersHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetFollowingAndFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.isFollowerHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.FollowObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.GetFollowingAndFollowersCountObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public void follow(User selectedUser, FollowObserver observer) {
        FollowTask task = new FollowTask(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new FollowHandler(observer, false));
        BackgroundTaskUtils.runTask(task);
    }

    public void unfollow(User selectedUser, FollowObserver observer) {
        UnfollowTask task = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new FollowHandler(observer, true));
        BackgroundTaskUtils.runTask(task);
    }

    public void getFollowing(User user, int limit, User lastFollowee, PagedObserver<User> observer) {
        GetFollowingTask task = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(), user, limit, lastFollowee, new GetUsersHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }

    public void getFollowers(User user, int limit, User lastFollower, PagedObserver<User> observer) {
        GetFollowingTask task = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(), user, limit, lastFollower, new GetUsersHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }

    public void updateFollowingAndFollowersCount(User user, GetFollowingAndFollowersCountObserver observer) {
        GetFollowersCountTask task = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(), user, new GetFollowingAndFollowersCountHandler(observer, true));
        BackgroundTaskUtils.runTask(task);
        GetFollowingCountTask task2 = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(), user, new GetFollowingAndFollowersCountHandler(observer, false));
        BackgroundTaskUtils.runTask(task2);
    }

    public void isFollower(User selectedUser, IsFollowerObserver observer) {
        IsFollowerTask task = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, new isFollowerHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }
}
