package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class CountService {

    //******************************** Followers *********************************//
    public interface GetFollowersObserver {
        void getFollowerCountSucceeded(AuthToken authToken, User user);
        void getFollowerCountFailed(String message);
        void getFollowerCountThrewException(Exception ex);
    }



    //******************************** Following ********************************//
    public interface GetFollowingObserver {
        void getFollowingCountSucceeded(AuthToken authToken, User user);
        void getFollowingCountFailed(String message);
        void getFollowingCountThrewException(Exception ex);
    }


}
