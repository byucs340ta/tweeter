package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.handler.ScrollableHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ScrollableObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryService extends Service {
    public void getStory(AuthToken token, User user, int limit, Status lastStatus, ScrollableObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(token, user, limit, lastStatus, new ScrollableHandler<Status>(observer));
        executeTask(getStoryTask);
    }
}
