package edu.byu.cs.tweeter.client.backgroundTask;

import android.util.Log;

import edu.byu.cs.tweeter.client.util.ByteArrayUtils;
import edu.byu.cs.tweeter.shared.domain.User;

/**
 * TaskUtils contains utility methods needed by background tasks.
 */
public class TaskUtils {

    private static final String LOG_TAG = "TaskUtils";

    /**
     * Loads the profile image for the user.
     *
     * @param user the user whose profile image is to be loaded.
     */
    public static void loadImage(User user) {
        try {
            byte[] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString(), e);
        }
    }

}
