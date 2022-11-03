package edu.byu.cs.tweeter.client.model.net;

import java.util.List;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;

public class TweeterServerException extends TweeterRemoteException {


    public TweeterServerException(String message, String remoteExceptionType, List<String> remoteStakeTrace) {
        super(message, remoteExceptionType, remoteStakeTrace);
    }
}
