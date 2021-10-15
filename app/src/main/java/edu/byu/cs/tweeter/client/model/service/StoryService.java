package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryService extends BaseService {

    public void getStory(AuthToken authToken, User targetUser, int numItemsToGet,
                         Status lastStatus, PagedObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(new PagedTaskHandler<Status>(observer) {
        }, authToken, targetUser, numItemsToGet, lastStatus);
        super.executeService(getStoryTask);
    }

}
