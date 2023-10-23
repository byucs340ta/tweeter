package edu.byu.cs.tweeter.client.model.services.newservices;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.AuthenticateHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.LogoutObserver;

public class UserService {
    public void login(String alias, String password, AuthenticateObserver observer) {
        LoginTask task = new LoginTask(alias, password, new AuthenticateHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }

    public void register(String firstName, String lastName, String alias, String password, String imageUrl, AuthenticateObserver observer) {
        RegisterTask task = new RegisterTask(firstName, lastName, alias, password, imageUrl, new AuthenticateHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }

    public void getUser(String alias, GetUserObserver observer) {
        GetUserTask task = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(), alias, new GetUserHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }

    public void logout(LogoutObserver observer) {
        LogoutTask task = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
    }
}
