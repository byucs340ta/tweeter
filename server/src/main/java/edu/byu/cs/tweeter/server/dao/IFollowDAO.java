package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.FollowListResponse;

public interface IFollowDAO extends IDAO {
    void followUser(User follower, User followee);
    void unfollowUser(User follower, User followee);
    boolean isFollower(User follower, User followee);
    FollowListResponse getFollowers(String user, String lastItem);
    FollowListResponse getFollowees(String user, String lastItem);
}
