package edu.byu.cs.tweeter.client.view.main.followers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the "Followers" tab.
 */
public class FollowersFragment extends Fragment {

    private static final String LOG_TAG = "FollowersFragment";
    private static final String USER_KEY = "UserKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private User user;

    private FollowersRecyclerViewAdapter followersRecyclerViewAdapter;

    /**
     * Creates an instance of the fragment and places the target user in an arguments
     * bundle assigned to the fragment.
     *
     * @param user the user whose followers are being displayed (not necessarily the logged-in user).
     * @return the fragment.
     */
    public static FollowersFragment newInstance(User user) {
        FollowersFragment fragment = new FollowersFragment();

        Bundle args = new Bundle(1);
        args.putSerializable(USER_KEY, user);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);

        RecyclerView followersRecyclerView = view.findViewById(R.id.followersRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followersRecyclerView.setLayoutManager(layoutManager);

        followersRecyclerViewAdapter = new FollowersRecyclerViewAdapter();
        followersRecyclerView.setAdapter(followersRecyclerViewAdapter);

        followersRecyclerView.addOnScrollListener(new FollowRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    /**
     * The ViewHolder for the RecyclerView that displays the follower data.
     */
    private class FollowersHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;

        /**
         * Creates an instance and sets an OnClickListener for the user's row.
         *
         * @param itemView the view on which the user will be displayed.
         */
        FollowersHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                            userAlias.getText().toString(), new GetUserHandler());
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(getUserTask);
                    Toast.makeText(getContext(), "Getting user's profile...", Toast.LENGTH_LONG).show();
                }
            });
        }

        /**
         * Binds the user's data to the view.
         *
         * @param user the user.
         */
        void bindUser(User user) {
            if (user == null)
                Log.e(LOG_TAG, "user is null!");
            userAlias.setText(user.getAlias());
            userName.setText(user.getName());

            Picasso.get().load(user.getImageUrl()).into(userImage);

        }

        /**
         * Message handler (i.e., observer) for GetUserTask.
         */
        private class GetUserHandler extends Handler {

            public GetUserHandler() {
                super(Looper.getMainLooper());
            }

            @Override
            public void handleMessage(@NonNull Message msg) {
                boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
                if (success) {
                    User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
                    startActivity(intent);
                } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                    String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                    Toast.makeText(getContext(), "Failed to get user's profile: " + message, Toast.LENGTH_LONG).show();
                } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                    Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                    Toast.makeText(getContext(), "Failed to get user's profile because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * The adapter for the RecyclerView that displays the follower data.
     */
    private class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowersHolder> {

        private final List<User> users = new ArrayList<>();

        private User lastFollower;

        private boolean hasMorePages;
        private boolean isLoading = false;

        /**
         * Creates an instance and loads the first page of following data.
         */
        FollowersRecyclerViewAdapter() {
            loadMoreItems();
        }

        /**
         * Adds new users to the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that items have been added.
         *
         * @param newUsers the users to add.
         */
        void addItems(List<User> newUsers) {
            int startInsertPosition = users.size();
            users.addAll(newUsers);
            this.notifyItemRangeInserted(startInsertPosition, newUsers.size());
        }

        /**
         * Adds a single user to the list from which the RecyclerView retrieves the users it
         * displays and notifies the RecyclerView that an item has been added.
         *
         * @param user the user to add.
         */
        void addItem(User user) {
            users.add(user);
            this.notifyItemInserted(users.size() - 1);
        }

        /**
         * Removes a user from the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that an item has been removed.
         *
         * @param user the user to remove.
         */
        void removeItem(User user) {
            int position = users.indexOf(user);
            users.remove(position);
            this.notifyItemRemoved(position);
        }

        /**
         * Creates a view holder for a follower to be displayed in the RecyclerView or for a message
         * indicating that new rows are being loaded if we are waiting for rows to load.
         *
         * @param parent   the parent view.
         * @param viewType the type of the view (ignored in the current implementation).
         * @return the view holder.
         */
        @NonNull
        @Override
        public FollowersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FollowersFragment.this.getContext());
            View view;

            if (viewType == LOADING_DATA_VIEW) {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.user_row, parent, false);
            }

            return new FollowersHolder(view);
        }

        /**
         * Binds the follower at the specified position unless we are currently loading new data. If
         * we are loading new data, the display at that position will be the data loading footer.
         *
         * @param followingHolder the ViewHolder to which the follower should be bound.
         * @param position        the position (in the list of followers) that contains the follower to be
         *                        bound.
         */
        @Override
        public void onBindViewHolder(@NonNull FollowersHolder followingHolder, int position) {
            if (!isLoading) {
                followingHolder.bindUser(users.get(position));
            }
        }

        /**
         * Returns the current number of followers available for display.
         *
         * @return the number of followers available for display.
         */
        @Override
        public int getItemCount() {
            return users.size();
        }

        /**
         * Returns the type of the view that should be displayed for the item currently at the
         * specified position.
         *
         * @param position the position of the items whose view type is to be returned.
         * @return the view type.
         */
        @Override
        public int getItemViewType(int position) {
            return (position == users.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more following
         * data.
         */
        void loadMoreItems() {
            if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
                isLoading = true;
                addLoadingFooter();

                GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                        user, PAGE_SIZE, lastFollower, new GetFollowersHandler());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(getFollowersTask);
            }
        }

        /**
         * Adds a dummy user to the list of users so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        private void addLoadingFooter() {
            addItem(new User("Dummy", "User", "dummyurl"));
        }

        /**
         * Removes the dummy user from the list of users so the RecyclerView will stop displaying
         * the loading footer at the bottom of the list.
         */
        private void removeLoadingFooter() {
            removeItem(users.get(users.size() - 1));
        }


        /**
         * Message handler (i.e., observer) for GetFollowersTask.
         */
        private class GetFollowersHandler extends Handler {

            public GetFollowersHandler() {
                super(Looper.getMainLooper());
            }

            @Override
            public void handleMessage(@NonNull Message msg) {
                isLoading = false;
                removeLoadingFooter();

                boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
                if (success) {
                    List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                    hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);

                    lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;

                    followersRecyclerViewAdapter.addItems(followers);
                } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                    String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                    Toast.makeText(getContext(), "Failed to get followers: " + message, Toast.LENGTH_LONG).show();
                } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                    Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                    Toast.makeText(getContext(), "Failed to get followers because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * A scroll listener that detects when the user has scrolled to the bottom of the currently
     * available data.
     */
    private class FollowRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        FollowRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        /**
         * Determines whether the user has scrolled to the bottom of the currently available data
         * in the RecyclerView and asks the adapter to load more data if the last load request
         * indicated that there was more data to load.
         *
         * @param recyclerView the RecyclerView.
         * @param dx           the amount of horizontal scroll.
         * @param dy           the amount of vertical scroll.
         */
        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!followersRecyclerViewAdapter.isLoading && followersRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    // Run this code later on the UI thread
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                        followersRecyclerViewAdapter.loadMoreItems();
                    }, 0);
                }
            }
        }
    }

}
