package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.response.SuccessResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {

    /**
     * The user that is being followed.
     */
    private final User followee;
    private final User targetUser;

    public UnfollowTask(AuthToken authToken, User followee, User targetUser, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
        this.targetUser = targetUser;
    }

    @Override
    protected void runTask() {
        try {
            FollowUserRequest request = new FollowUserRequest(getAuthToken(), targetUser, followee);
            ServerFacade facade = new ServerFacade();
            SuccessResponse response = facade.unfollowUser(request);

            if (response.isSuccess()) {
                sendSuccessMessage();
            }
            else {
                sendFailedMessage(response.getMessage());
            }
        }
        catch (Exception ex) {
            sendExceptionMessage(ex);
        }
    }


}
