package edu.byu.cs.tweeter.client.model.net;

import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowListRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.StatusListRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowListResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.StatusListResponse;
import edu.byu.cs.tweeter.model.net.response.SuccessResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://iyc33sckd6.execute-api.us-east-1.amazonaws.com/prod";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/login", request, null, LoginResponse.class);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowListResponse getFollowees(FollowListRequest request)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/getfollowing", request, null, FollowListResponse.class);
    }

    public SuccessResponse followUser(FollowUserRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/follow", request, null, SuccessResponse.class);
    }

    public StatusListResponse getFeed(StatusListRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/getfeed", request, null, StatusListResponse.class);
    }

    public FollowCountResponse getFollowerCount(FollowCountRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/getfollowerscount", request, null, FollowCountResponse.class);
    }

    public FollowListResponse getFollowers(FollowListRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/getfollowers", request, null, FollowListResponse.class);
    }

    public FollowCountResponse getFollowingCount(FollowCountRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/getfollowingcount", request, null, FollowCountResponse.class);
    }

    public StatusListResponse getStory(StatusListRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/getstory", request, null, StatusListResponse.class);
    }

    public UserResponse getUser(UserRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/getuser", request, null, UserResponse.class);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/isfollower", request, null, IsFollowerResponse.class);
    }

    public SuccessResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/logout", request, null, SuccessResponse.class);
    }

    public SuccessResponse postStatus(PostStatusRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/poststatus", request, null, SuccessResponse.class);
    }

    public LoginResponse register(RegisterRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/register", request, null, LoginResponse.class);
    }

    public SuccessResponse unfollowUser(FollowUserRequest request) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost("/unfollow", request, null, SuccessResponse.class);
    }
}
