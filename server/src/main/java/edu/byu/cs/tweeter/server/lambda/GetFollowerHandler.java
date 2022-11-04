package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.FollowListRequest;
import edu.byu.cs.tweeter.model.net.response.FollowListResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowerHandler implements RequestHandler<FollowListRequest, FollowListResponse> {
    @Override
    public FollowListResponse handleRequest(FollowListRequest input, Context context) {
        FollowService service = new FollowService();
        return service.getFollowers(input);
    }
}
