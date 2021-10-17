//package edu.byu.cs.tweeter.client.backgroundTask;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
//import edu.byu.cs.tweeter.model.domain.AuthToken;
//import edu.byu.cs.tweeter.model.domain.User;
//
///**
// * Background task that queries how many followers a user has.
// */
//public class GetFollowersCountTask extends GetCountTask {
//    private static final String LOG_TAG = "GetFollowersCountTask";
//
//    public GetFollowersCountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
//        super(messageHandler, authToken, targetUser);
//    }
//
//    @Override
//    protected boolean runTask() {
//        this.count = 20; // will fix with server
//        return true;
//    }
//}
