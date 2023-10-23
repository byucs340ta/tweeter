package edu.byu.cs.tweeter.client.model.services;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetStatusesHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.PostHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PostObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    public void getFeed(User user, int pageSize, Status lastItem, PagedObserver<Status> observer) {
        GetFeedTask task = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(), user, pageSize, lastItem, new GetStatusesHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }

    public void getStory(User user, int pageSize, Status lastItem, PagedObserver<Status> observer) {
        GetStoryTask task = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(), user, pageSize, lastItem, new GetStatusesHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }

    public void postStatus(Status status, PostObserver observer) {
        PostStatusTask task = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(), status, new PostHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }
}
