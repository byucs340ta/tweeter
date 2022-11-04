package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.StatusListRequest;
import edu.byu.cs.tweeter.model.net.response.StatusListResponse;
import edu.byu.cs.tweeter.server.service.FeedService;

public class GetFeedHandler implements RequestHandler<StatusListRequest, StatusListResponse> {
    @Override
    public StatusListResponse handleRequest(StatusListRequest input, Context context) {
        FeedService service = new FeedService();
        return service.getFeed(input);
    }
}
