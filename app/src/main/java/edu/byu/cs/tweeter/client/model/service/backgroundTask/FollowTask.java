package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.response.SuccessResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    /**
     * The user that is being followed.
     */
    private final User followee;
    private final User targetUser;

    public FollowTask(AuthToken authToken, User targetUser, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
        this.targetUser = targetUser;
    }

    @Override
    protected void runTask() {

        try {
            FollowUserRequest request = new FollowUserRequest(getAuthToken(), targetUser, followee);
            ServerFacade facade = new ServerFacade();
            SuccessResponse response = facade.followUser(request);

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
