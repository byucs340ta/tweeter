package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserTaskHandler extends BackgroundTaskHandler<GetUserObserver> {

    public GetUserTaskHandler(GetUserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(GetUserObserver observer, Message msg) {
        User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
        observer.GetUserSucceeded(user);
    }

}
