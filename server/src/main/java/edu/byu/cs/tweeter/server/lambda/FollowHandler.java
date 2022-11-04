package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.response.SuccessResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class FollowHandler implements RequestHandler<FollowUserRequest, SuccessResponse> {
    @Override
    public SuccessResponse handleRequest(FollowUserRequest input, Context context) {
        FollowService followService = new FollowService();
        return followService.followUser(input);
    }
}
