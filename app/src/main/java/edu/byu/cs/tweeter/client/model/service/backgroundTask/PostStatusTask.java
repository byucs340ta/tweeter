package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.SuccessResponse;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {

    /**
     * The new status being sent. Contains all properties of the status,
     * including the identity of the user sending the status.
     */
    private final Status status;
    private final User user;

    public PostStatusTask(AuthToken authToken, Status status, User user, Handler messageHandler) {
        super(authToken, messageHandler);
        this.status = status;
        this.user = user;
    }

    @Override
    protected void runTask() {
        try {
            ServerFacade facade = new ServerFacade();
            PostStatusRequest request = new PostStatusRequest(getAuthToken(), user, status);
            SuccessResponse response = facade.postStatus(request);

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
