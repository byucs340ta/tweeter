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

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.presenter.FollowersPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the "Followers" tab.
 */
public class FollowersFragment extends Fragment implements FollowersPresenter.View {

    private static final String LOG_TAG = "FollowersFragment";
    private static final String USER_KEY = "UserKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;


    private User user;

    private FollowersRecyclerViewAdapter followersRecyclerViewAdapter;

    private FollowersPresenter presenter;

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

        presenter = new FollowersPresenter(this);
        presenter.loadMoreItems(user);

        return view;
    }

    @Override
    public void displayErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setLoadingFooter(boolean b) {
        if (b) {
            followersRecyclerViewAdapter.addLoadingFooter();
        } else {
            followersRecyclerViewAdapter.removeLoadingFooter();
        }
    }

    @Override
    public void addFollowers(List<User> followers) {
        followersRecyclerViewAdapter.addItems(followers);
    }

    @Override
    public void setIntent(User user) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
        startActivity(intent);
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
                    presenter.getUser(userAlias.getText().toString());
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
    }

    /**
     * The adapter for the RecyclerView that displays the follower data.
     */
    private class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowersHolder> {

        private final List<User> users = new ArrayList<>();

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
            if (!presenter.isLoading()) {
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
            return (position == users.size() - 1 && presenter.isLoading()) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more following
         * data.
         */
        void loadMoreItems() {
            presenter.loadMoreItems(user);
        }

        /**
         * Adds a dummy user to the list of users so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        private void addLoadingFooter() {
            addItem(new User("Dummy", "User", ""));
        }

        /**
         * Removes the dummy user from the list of users so the RecyclerView will stop displaying
         * the loading footer at the bottom of the list.
         */
        private void removeLoadingFooter() {
            removeItem(users.get(users.size() - 1));
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

            if (!presenter.isLoading() && presenter.hasMorePages()) {
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
