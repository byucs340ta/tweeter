package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer;

public interface CountNotificationObserver extends ServiceObserver {
    void handleSuccess(int count);
}
