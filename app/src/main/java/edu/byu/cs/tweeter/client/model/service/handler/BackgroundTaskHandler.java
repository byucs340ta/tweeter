package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public abstract class BackgroundTaskHandler<T extends ServiceObserver> extends Handler {

    protected final T observer;

    protected BackgroundTaskHandler(T observer) {
        super(Looper.getMainLooper());
        this.observer = observer; }

    /**
     * Takes data out of bundle and interprets it. However, data is always different. We need lots of
     * sublcass handling. that's where handleSuccessMessage() comes in!
     * @param msg
     */
    @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                handleSuccessMessage(observer, msg);
            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                handleFailureMessage(observer, msg);
            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                String exceptionMessage = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
//                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                observer.serviceException(exceptionMessage); // may want to make own function later? Wilkerson doesn't.
            }
        }

    /**
     * On success, the subclasses will know how to handle their bundle of info!
     * @param observer
     * @param msg
     */
    protected abstract void handleSuccessMessage(T observer, Message msg);

    protected void handleFailureMessage(T observer, Message msg) {
        String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
        observer.serviceFailure(message);
    }

}
