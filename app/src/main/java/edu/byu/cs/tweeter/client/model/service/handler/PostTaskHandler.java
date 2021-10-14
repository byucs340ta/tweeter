package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.PostService;
import edu.byu.cs.tweeter.client.model.service.ServiceObserver;

public class PostTaskHandler extends BackgroundTaskHandler {

//    PostObserver extends service {
//        // todo this (serviceObserver + success)
//    }

    public PostTaskHandler(ServiceObserver observer) {
        super(observer);
    }

    @Override
    protected  <T extends PostService.PostObserver> void handleSuccessMessage(T observer, Message msg) {
        // todo: I don't understand how to get this thing to call a PostService.PostObserver().PostSucceeded()
    }
}
