package edu.byu.cs.tweeter.client.model.service.observer;

public interface ServiceObserver {
    void handleFailure(String message);
    void handleException(Exception exception);
}
