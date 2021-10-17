package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handler.FollowTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.UnfollowTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SetFollowingObserver;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends BaseService {

    //******************************** Get Following *************************************//
    ///////////////////////////////////////////////////////////////////////////////////////

        public void getFollowing(AuthToken authToken, User targetUser, int numItemsToGet,
                             User lastFollowee, PagedObserver<User> observer) {
        GetFollowingTask getFollowingTask  = new GetFollowingTask(new PagedTaskHandler<User>(observer) {
        }, authToken, targetUser, numItemsToGet, lastFollowee);
        super.executeService(getFollowingTask);
    }



    //*********************************** Get Followers ************************************//
    /////////////////////////////////////////////////////////////////////////////////////////

    public void getFollowers(AuthToken authToken, User targetUser, int numItemsToGet,
                             User lastFollower, PagedObserver<User> observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(new PagedTaskHandler<User>(observer) {
        }, authToken, targetUser, numItemsToGet, lastFollower);
        super.executeService(getFollowersTask);
    }



    //******************************** Add Following *************************************//
    ///////////////////////////////////////////////////////////////////////////////////////

    public void addFollower(AuthToken authToken, User selectedUser, SetFollowingObserver observer) {
        FollowTask followTask = new FollowTask(new FollowTaskHandler(observer), authToken,
                selectedUser);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }



    //******************************** Remove Following *************************************//
    //////////////////////////////////////////////////////////////////////////////////////////

    public void removeFollower(AuthToken authToken, User selectedUser, SetFollowingObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(new UnfollowTaskHandler(observer), authToken, selectedUser);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }



    //*********************************** is Follower *************************************//
    ////////////////////////////////////////////////////////////////////////////////////////

    public void isFollower(AuthToken authToken, User beingFollowed, User follower, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(new IsFollowerHandler(observer),
                authToken, beingFollowed, follower);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }



}
