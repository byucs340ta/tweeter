package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService extends Service {
    public void getUser(AuthToken token, String alias, GetUserObserver observer){
        GetUserTask getUserTask = new GetUserTask(token, alias, new GetUserHandler(observer));
        executeTask(getUserTask);
    }
}
