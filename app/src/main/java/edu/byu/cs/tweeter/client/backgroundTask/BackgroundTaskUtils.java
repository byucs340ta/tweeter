package edu.byu.cs.tweeter.client.backgroundTask;

import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.util.ByteArrayUtils;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * BackgroundTaskUtils contains utility methods needed by background tasks.
 */
public class BackgroundTaskUtils {

    private static final String LOG_TAG = "BackgroundTaskUtils";

    /**
     * Loads the profile image for the user.
     *
     * @param user the user whose profile image is to be loaded.
     */
    public static void loadImage(User user) throws IOException {
        try {
            byte[] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString(), e);
            throw e;
        }
    }

}
