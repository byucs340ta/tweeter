package edu.byu.cs.tweeter.client.model.services.backgroundTask.observer;

public interface ServiceObserver {
    void handleFailure(String message);
    void handleException(Exception exception);
}
