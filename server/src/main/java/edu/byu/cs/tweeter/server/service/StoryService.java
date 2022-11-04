package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.StatusListRequest;
import edu.byu.cs.tweeter.model.net.response.StatusListResponse;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StoryService {
    public StatusListResponse getStory(StatusListRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }


        return getStoryDAO().getStory(request);
    }

    StoryDAO getStoryDAO() {
        return new StoryDAO();
    }
}
