package edu.byu.cs.tweeter.client.model.services;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public interface GetFollowingObserver {
        void getFollowingSucceeded(List<User> followees, boolean hasMorePages);

        void getFollowingFailed(String message);
    }

    public void getFollowing(AuthToken authToken, String alias, int pageSize, User user, GetFollowingObserver observer) {

    }
}
