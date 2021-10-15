package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedService extends BaseService {

    public void getFeed(AuthToken authToken, User targetUser, int numItemsToGet,
                        Status lastStatus, PagedObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(new PagedTaskHandler<Status>(observer) {
        }, authToken, targetUser, numItemsToGet, lastStatus);
        super.executeService(getFeedTask);
    }

}

