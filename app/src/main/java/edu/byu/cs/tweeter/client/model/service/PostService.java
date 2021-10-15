package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handler.PostTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PostObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class PostService extends BaseService {

    public void run(Status newStatus, PostObserver observer) {
        PostStatusTask taskToExecute = new PostStatusTask(new PostTaskHandler(observer),
                Cache.getInstance().getCurrUserAuthToken(),
                newStatus);
        super.executeService(taskToExecute);
    }
}
