package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.StatusListRequest;
import edu.byu.cs.tweeter.model.net.response.StatusListResponse;
import edu.byu.cs.tweeter.server.service.StoryService;

public class GetStoryHandler implements RequestHandler<StatusListRequest, StatusListResponse> {
    @Override
    public StatusListResponse handleRequest(StatusListRequest input, Context context) {
        StoryService service = new StoryService();
        return service.getStory(input);
    }
}
