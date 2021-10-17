package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.handler.GetUserTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.LoginTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.service.handler.RegisterTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SignInObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService extends BaseService {

    //*********************************** Register **************************************//
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * Sends the requests for information using a handler
     */
    public void register(String firstName, String lastName, String alias, String password,
                         String imageBytesBase64, SignInObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new RegisterTaskHandler(observer));
        executeService(registerTask);
    }



    //*********************************** Login **************************************//
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Presenter one layer up calls this function. It passes in an observer (interfaced on this layer
     * but defined in the presenter) so that we can interact with it, allowing indirect communication
     * with above layer.
     * @param alias
     * @param password
     * @param observer
     */
    public void login(String alias, String password, SignInObserver observer) {
        // Run a LoginTask to login the user
        LoginTask loginTask = new LoginTask(alias, password,
                new LoginTaskHandler(observer));
        executeService(loginTask);
    }



    //*********************************** GetUser **************************************//
    /////////////////////////////////////////////////////////////////////////////////////

    public void getUser(AuthToken authToken, String alias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(new GetUserTaskHandler(observer), authToken, alias);
        super.executeService(getUserTask);
    }



    //*********************************** Logout **************************************//
    ////////////////////////////////////////////////////////////////////////////////////

    public void logout(AuthToken authToken, LogoutObserver observer) {
        // Run a LoginTask to login the user
        LogoutTask logoutTask = new LogoutTask(new LogoutHandler(observer), authToken);
        super.executeService(logoutTask);
    }



}
