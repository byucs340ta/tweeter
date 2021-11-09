package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
//import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
//import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.FollowersPresenter;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.client.view.login.StatusDialogFragment;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements StatusDialogFragment.Observer,
        MainPresenter.View {

    private static final String LOG_TAG = "MainActivity";

    public static final String CURRENT_USER_KEY = "CurrentUser";

    private Toast logOutToast;
    private Toast postingToast;
    private User selectedUser;
    private TextView followeeCount;
    private TextView followerCount;
    private Button followButton;

    private MainPresenter presenter;

    @Override
    public void logout() {
        //Revert to login screen.
        Intent intent = new Intent(this, LoginActivity.class);
        //Clear everything so that the main activity is recreated with the login page.
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Clear user data (cached data).
        Cache.getInstance().clearCache();
        startActivity(intent);
    }

    @Override
    public void updateFollowingandFollowersCount() {
        updateSelectedUserFollowingAndFollowers();
    }

    @Override
    public void setFollowerCount(String count) {
        followerCount.setText(getString(R.string.followerCount, String.valueOf(count)));
    }
    @Override
    public void setFollowingCount(String count) {
        followeeCount.setText(getString(R.string.followeeCount, String.valueOf(count)));
    }

    /**
     * Updates to removed or added!
     * @param removed
     */
    @Override
    public void updateFollowingButton(boolean removed) {
        updateFollowButton(removed);
    }

    @Override
    public void setFollowButtonClickable(boolean canClick) {
        followButton.setEnabled(canClick);
    }

    @Override
    public void setFollowButtonVisibility(boolean isVisible) {
        if (isVisible) {
            followButton.setVisibility(android.view.View.VISIBLE);
        }
        else {
            followButton.setVisibility(android.view.View.GONE);
        }
    }

    @Override
    public void setIsFollowerButton(boolean isFollower) {
        if (isFollower) {
            followButton.setText(R.string.following);
            followButton.setBackgroundColor(getResources().getColor(R.color.white));
            followButton.setTextColor(getResources().getColor(R.color.lightGray));
        } else {
            followButton.setText(R.string.follow);
            followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    public void displayErrorMessage(String message) {
        // FOR NOW. Might want to copy Login's more elegant error messages
        clearInfoMessage();
        logOutToast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
        logOutToast.show();
    }

    @Override
    public void clearErrorMessage() {
        if (logOutToast != null) {
            logOutToast.cancel();
            logOutToast = null;
        }
    }

    @Override
    public void displayInfoMessage(String message) {
        clearInfoMessage();
        logOutToast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
        logOutToast.show();
    }

    @Override
    public void clearInfoMessage() {
        if (logOutToast != null) {
            logOutToast.cancel();
            logOutToast = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedUser = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if (selectedUser == null) {
            throw new RuntimeException("User not passed to activity");
        }

        presenter = new MainPresenter(this, Cache.getInstance().getCurrUserAuthToken(), selectedUser); // now following view has a presenter to work with

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), selectedUser);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusDialogFragment statusDialogFragment = new StatusDialogFragment();
                statusDialogFragment.show(getSupportFragmentManager(), "post-status-dialog");
            }
        });

        updateSelectedUserFollowingAndFollowers();

        TextView userName = findViewById(R.id.userName);
        userName.setText(selectedUser.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(selectedUser.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(selectedUser.getImageBytes()));

        followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, "..."));

        followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, "..."));

        followButton = findViewById(R.id.followButton);

        // At start check if follower and from there set visibility.
        presenter.verifyIsFollower();

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followButton.setEnabled(false);
                boolean isNotAlreadyFollowing = followButton.getText().toString().
                        equals(v.getContext().getString(R.string.following));
                if (isNotAlreadyFollowing) {
                    presenter.follow();
                }
                else {
                    presenter.unfollow();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutMenu) {
            logOutToast = Toast.makeText(this, "Logging Out...", Toast.LENGTH_LONG);
            logOutToast.show();
            presenter.logout();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStatusPosted(String post) throws ParseException, MalformedURLException {
        presenter.postStatus(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(),
                presenter.parseURLs(post), presenter.parseMentions(post));
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public void updateSelectedUserFollowingAndFollowers() {
        presenter.countFollowersAndFollowing();
//        presenter.countFollowers();
//        presenter.countFollowing();
    }

    public void updateFollowButton(boolean removed) {
        // If follow relationship was removed.
        if (removed) {
            followButton.setText(R.string.follow);
            followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            followButton.setText(R.string.following);
            followButton.setBackgroundColor(getResources().getColor(R.color.white));
            followButton.setTextColor(getResources().getColor(R.color.lightGray));
        }
    }
}
