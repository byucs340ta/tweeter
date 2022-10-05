package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;

/**
 * Message handler (i.e., observer) for GetFollowingCountHandler
 */
public class GetFollowingCountHandler extends Handler {

    private FollowService.GetFollowingCountObserver observer;

    public GetFollowingCountHandler(FollowService.GetFollowingCountObserver observer) {
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
        if (success) {
            int count = msg.getData().getInt(GetCountTask.COUNT_KEY);
            observer.displayFollowingCount(count);
        } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
            observer.displayErrorMessage(message);
        } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
            observer.displayException(ex);
        }
    }
}