package edu.byu.cs.tweeter.client.model.service.handler;
import android.os.Message;
import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;

public class LogoutHandler extends BackgroundTaskHandler<LogoutObserver> {

    public LogoutHandler(LogoutObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(LogoutObserver observer, Message msg) {
        observer.LogoutSucceeded();
    }
}
