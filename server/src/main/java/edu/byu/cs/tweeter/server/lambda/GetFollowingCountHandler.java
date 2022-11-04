package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowCountResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowingCountHandler implements RequestHandler<FollowCountRequest, FollowCountResponse> {
    @Override
    public FollowCountResponse handleRequest(FollowCountRequest input, Context context) {
        FollowService service = new FollowService();
        return service.getFolloweeCount(input);
    }
}
