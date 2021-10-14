package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.PostTaskHandler;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class PostService extends BaseService {

//    protected PostService(Status newStatus, ServiceObserver observer) {
//        super(new PostStatusTask(new PostTaskHandler(observer),
//                Cache.getInstance().getCurrUserAuthToken(),
//                newStatus),
//                new PostTaskHandler(observer));
//    }

    public interface PostObserver extends ServiceObserver {
        void PostSucceeded();
    }

    public void run(Status newStatus, ServiceObserver observer) {
        PostStatusTask taskToExecute = new PostStatusTask(new PostTaskHandler(observer),
                Cache.getInstance().getCurrUserAuthToken(),
                newStatus);
        super.executeService(taskToExecute);
    }


    /*
    public interface PostObserver {
        void PostSucceeded();
        void PostFailed(String message);
        void PostThrewException(Exception ex);
    }

    public void post(Status newStatus, PostObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(new PostStatusHandler(observer),
                Cache.getInstance().getCurrUserAuthToken(),
                newStatus);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(statusTask);
    }

    // PostStatusHandler

    private class PostStatusHandler extends Handler {
        PostStatusHandler(PostObserver observer) { this.observer = observer; }
        PostObserver observer;

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
            if (success) {
                observer.PostSucceeded();
            } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
                observer.PostFailed(message);
            } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
                observer.PostThrewException(ex);
            }
        }
    }
     */

}
