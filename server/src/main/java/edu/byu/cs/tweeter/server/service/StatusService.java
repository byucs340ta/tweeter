package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.SuccessResponse;

public class StatusService {
    public SuccessResponse postStatus(PostStatusRequest request) {
        return new SuccessResponse(true);
    }
}
