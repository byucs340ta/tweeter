package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.StatusListRequest;
import edu.byu.cs.tweeter.model.net.response.StatusListResponse;

public interface IFeedDAO extends  IDAO {
    void addFeed(Status status, User user);
    StatusListResponse getFeed(StatusListRequest request);
}
