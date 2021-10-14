package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public abstract class BackgroundTask implements Runnable {

    private static final String LOG_TAG = "BackgroundTask";

    // Output. Used to pack the bundle
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";

    /**
     * Message handler to be notified when task completes
     */
    protected Handler messageHandler;

    /**
     * Error message indicating why the task failed
     */
    protected String errorMessage;

    protected BackgroundTask(Handler messageHandler) { this.messageHandler = messageHandler; }

    @Override
    public void run() {
        try {
            if (runTask()) {
                sendSuccessMessage();
            }
            else {
                sendFailedMessage();
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    protected void sendSuccessMessage() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        loadSuccessBundle(msgBundle);
        sendMessage(msgBundle);
    }

    protected void sendFailedMessage() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putString(MESSAGE_KEY, (LOG_TAG + " failed: " + this.errorMessage));
        sendMessage(msgBundle);
    }

    protected void sendExceptionMessage(Exception ex) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putString(MESSAGE_KEY, (LOG_TAG + " failed due to exception: " + ex.getMessage())); // fixme: Should this return exception or message? I think it's up to me.
        msgBundle.putSerializable(EXCEPTION_KEY, ex); // currently passing this through, but it is only used for verification.
        sendMessage(msgBundle);
    }

    protected void sendMessage(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    protected FakeData getFakeData() {
        return new FakeData();
    }

    /**
     * Abstract function to be overridden by children tasks
     * @return
     */
    protected abstract boolean runTask();

    /**
     * Not abstract so that class that don't need this can just return easily.
     * @param msgBundle
     */
    protected void loadSuccessBundle(Bundle msgBundle) {
        return;
    }

}
