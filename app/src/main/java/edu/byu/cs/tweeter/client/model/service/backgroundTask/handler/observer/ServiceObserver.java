package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer;

public interface ServiceObserver {
    void displayErrorMessage(String message);
    void displayException(Exception ex);
}
