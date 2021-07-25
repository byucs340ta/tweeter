package edu.byu.cs.tweeter.client.view.main.feed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
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

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the "Feed" tab.
 */
public class FeedFragment extends Fragment {
    private static final String LOG_TAG = "FeedFragment";
    private static final String USER_KEY = "UserKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private User user;

    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;

    /**
     * Creates an instance of the fragment and places the target user in an arguments
     * bundle assigned to the fragment.
     *
     * @param user the user whose feed is being displayed (not necessarily the logged-in user).
     * @return the fragment.
     */
    public static FeedFragment newInstance(User user) {
        FeedFragment fragment = new FeedFragment();

        Bundle args = new Bundle(1);
        args.putSerializable(USER_KEY, user);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);

        RecyclerView feedRecyclerView = view.findViewById(R.id.feedRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        feedRecyclerView.setLayoutManager(layoutManager);

        try {
            feedRecyclerViewAdapter = new FeedRecyclerViewAdapter();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);

        feedRecyclerView.addOnScrollListener(new FeedRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    /**
     * The ViewHolder for the RecyclerView that displays the feed data.
     */
    private class FeedHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView post;
        private final TextView datetime;

        /**
         * Creates an instance and sets an OnClickListener for the status's row.
         *
         * @param itemView the view on which the status will be displayed.
         */
        FeedHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.statusImage);
            userAlias = itemView.findViewById(R.id.statusAlias);
            userName = itemView.findViewById(R.id.statusName);
            post = itemView.findViewById(R.id.statusPost);
            datetime = itemView.findViewById(R.id.statusDatetime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "You selected '" + userName.getText() + "'.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        /**
         * Binds the status's data to the view.
         *
         * @param status the status.
         */
        void bindStatus(Status status) {
            userImage.setImageDrawable(ImageUtils.drawableFromByteArray(status.getUser().getImageBytes()));
            userAlias.setText(status.getUser().getAlias());
            userName.setText(status.getUser().getName());
            datetime.setText(status.getDate());

            // @mentions and urls clickable
            SpannableString spannableString = new SpannableString(status.getPost());

            for (String mention : status.getMentions()) {
                int startIndex = status.getPost().indexOf(mention);
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        TextView clickedMention = (TextView) widget;
                        Spanned s = (Spanned) clickedMention.getText();
                        int start = s.getSpanStart(this);
                        int end = s.getSpanEnd(this);

                        String clickable = s.subSequence(start, end).toString();

                        if (clickable.contains("http")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickable));
                            startActivity(intent);
                        } else {
                            GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                                    clickable, new GetUserHandler());
                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            executor.execute(getUserTask);
                            Toast.makeText(getContext(), "Getting user's profile...", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void updateDrawState(@NotNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(getResources().getColor(R.color.colorAccent));
                        ds.setUnderlineText(false);
                    }
                }, startIndex, (startIndex + mention.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (String url : status.getUrls()) {
                int startIndex = status.getPost().indexOf(url);
                spannableString.setSpan(new URLSpan(url), startIndex, (startIndex + url.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            post.setText(spannableString);
            post.setClickable(true);
            post.setMovementMethod(LinkMovementMethod.getInstance());
        }

        /**
         * Message handler (i.e., observer) for GetUserTask.
         */
        private class GetUserHandler extends Handler {
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
     * The adapter for the RecyclerView that displays the feed data.
     */
    private class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedHolder> {

        private final List<Status> feed = new ArrayList<>();
        private Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        /**
         * Creates an instance and loads the first page of feed data.
         */
        FeedRecyclerViewAdapter() throws MalformedURLException {
            loadMoreItems();
        }

        /**
         * Adds new statuses to the list from which the RecyclerView retrieves the statuses it displays
         * and notifies the RecyclerView that items have been added.
         *
         * @param newStory the status to add.
         */
        void addItems(List<Status> newStory) {
            int startInsertPosition = feed.size();
            feed.addAll(newStory);
            this.notifyItemRangeInserted(startInsertPosition, newStory.size());
        }

        /**
         * Adds a single status to the list from which the RecyclerView retrieves the statuses it
         * displays and notifies the RecyclerView that an item has been added.
         *
         * @param status the status to add.
         */
        void addItem(Status status) {
            feed.add(status);
            this.notifyItemInserted(feed.size() - 1);
        }

        /**
         * Removes a status from the list from which the RecyclerView retrieves the statuses it displays
         * and notifies the RecyclerView that an item has been removed.
         *
         * @param status the status to remove.
         */
        void removeItem(Status status) {
            int position = feed.indexOf(status);
            feed.remove(position);
            this.notifyItemRemoved(position);
        }

        /**
         * Creates a view holder for a status to be displayed in the RecyclerView or for a message
         * indicating that new rows are being loaded if we are waiting for rows to load.
         *
         * @param parent   the parent view.
         * @param viewType the type of the view (ignored in the current implementation).
         * @return the view holder.
         */
        @NonNull
        @Override
        public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FeedFragment.this.getContext());
            View view;

            if (viewType == LOADING_DATA_VIEW) {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new FeedHolder(view);
        }

        /**
         * Binds the status at the specified position unless we are currently loading new data. If
         * we are loading new data, the display at that position will be the data loading footer.
         *
         * @param feedHolder the ViewHolder to which the status should be bound.
         * @param position   the position (in the list of statuses) that contains the status to be
         *                   bound.
         */
        @Override
        public void onBindViewHolder(@NonNull FeedHolder feedHolder, int position) {
            if (!isLoading) {
                feedHolder.bindStatus(feed.get(position));
            }
        }

        /**
         * Returns the current number of statuses available for display.
         *
         * @return the number of statuses available for display.
         */
        @Override
        public int getItemCount() {
            return feed.size();
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
            return (position == feed.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more feed
         * data.
         */
        void loadMoreItems() throws MalformedURLException {
            if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
                isLoading = true;
                addLoadingFooter();

                GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                        user, PAGE_SIZE, lastStatus, new GetFeedHandler());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(getFeedTask);
            }
        }

        /**
         * Adds a dummy status to the list of statuses so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        private void addLoadingFooter() throws MalformedURLException {
            addItem(new Status("Dummy Post", new User("firstName", "lastName", "@coolAlias"), "2020-10-31 00:00:00", new ArrayList<String>() {{
                add("https://youtube.com");
            }}, new ArrayList<String>() {{
                add("@Dude1");
            }}));
        }

        /**
         * Removes the dummy status from the list of statuses so the RecyclerView will stop displaying
         * the loading footer at the bottom of the list.
         */
        private void removeLoadingFooter() {
            removeItem(feed.get(feed.size() - 1));
        }


        /**
         * Message handler (i.e., observer) for GetFeedTask.
         */
        private class GetFeedHandler extends Handler {
            @Override
            public void handleMessage(@NonNull Message msg) {
                isLoading = false;
                removeLoadingFooter();

                boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
                if (success) {
                    List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
                    hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);

                    lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

                    feedRecyclerViewAdapter.addItems(statuses);
                } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                    String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                    Toast.makeText(getContext(), "Failed to get feed: " + message, Toast.LENGTH_LONG).show();
                } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                    Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                    Toast.makeText(getContext(), "Failed to get feed because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * A scroll listener that detects when the user has scrolled to the bottom of the currently
     * available data.
     */
    private class FeedRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        FeedRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
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

            if (!feedRecyclerViewAdapter.isLoading && feedRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    // Run this code later on the UI thread
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                        try {
                            feedRecyclerViewAdapter.loadMoreItems();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }, 0);
                }
            }
        }
    }

}
