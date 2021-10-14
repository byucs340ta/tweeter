package edu.byu.cs.tweeter.client.model.service;

public interface ServiceObserver {
    void serviceFailure(String message);
//    void handleException(String message);
}
