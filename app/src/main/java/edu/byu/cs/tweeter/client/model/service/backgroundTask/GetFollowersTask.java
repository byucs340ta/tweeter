package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowListRequest;
import edu.byu.cs.tweeter.model.net.response.FollowListResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        try {
            ServerFacade facade = new ServerFacade();
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().toString();
            String lastFolloweeAlias = getLastItem() == null ? null : getLastItem().toString();

            FollowListRequest request = new FollowListRequest(getAuthToken(), targetUserAlias, getLimit(), lastFolloweeAlias);
            FollowListResponse response = facade.getFollowers(request);

            if (response.isSuccess()) {
                return new Pair<>(response.getFollowList(), response.getHasMorePages());
            }
            else {
                sendFailedMessage(response.getMessage());
            }
        }
        catch (Exception ex) {
            sendExceptionMessage(ex);
        }


        return null;    }
}
