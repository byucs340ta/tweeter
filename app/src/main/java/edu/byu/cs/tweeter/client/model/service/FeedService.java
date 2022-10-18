package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.handler.ScrollableHandler;
import edu.byu.cs.tweeter.client.model.service.observer.ScrollableObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedService extends Service {
    public void getFeed(AuthToken token, User user, int limit, Status lastStatus, ScrollableObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(token, user, limit, lastStatus, new ScrollableHandler<Status>(observer));
        executeTask(getFeedTask);
    }
}
