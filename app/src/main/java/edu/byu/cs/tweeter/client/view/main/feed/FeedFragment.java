package edu.byu.cs.tweeter.client.view.main.feed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.presenter.FeedPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the "Feed" tab.
 */
public class FeedFragment extends Fragment implements FeedPresenter.View {
    private static final String LOG_TAG = "FeedFragment";
    private static final String USER_KEY = "UserKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private User user;

    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;

    private FeedPresenter presenter;

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

        feedRecyclerViewAdapter = new FeedRecyclerViewAdapter();
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);

        feedRecyclerView.addOnScrollListener(new FeedRecyclerViewPaginationScrollListener(layoutManager));

        presenter = new FeedPresenter(this);
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
            feedRecyclerViewAdapter.addLoadingFooter();
        } else {
            feedRecyclerViewAdapter.removeLoadingFooter();
        }
    }

    @Override
    public void addFeed(List<Status> statuses) {
        feedRecyclerViewAdapter.addItems(statuses);
    }

    @Override
    public void setIntent(User user) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
        startActivity(intent);
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
                    presenter.getUser(userAlias.getText().toString());
                    Toast.makeText(getContext(), "Getting user's profile...", Toast.LENGTH_LONG).show();
                }
            });
        }

        /**
         * Binds the status's data to the view.
         *
         * @param status the status.
         */
        void bindStatus(Status status) {
            Picasso.get().load(status.getUser().getImageUrl()).into(userImage);
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
                            presenter.getUser(userAlias.getText().toString());
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
    }

    /**
     * The adapter for the RecyclerView that displays the feed data.
     */
    private class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedHolder> {

        private final List<Status> feed = new ArrayList<>();

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
            if (!presenter.isLoading()) {
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
            return (position == feed.size() - 1 && presenter.isLoading()) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more feed
         * data.
         */
        void loadMoreItems(){
            presenter.loadMoreItems(user);
        }

        /**
         * Adds a dummy status to the list of statuses so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        private void addLoadingFooter() {
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

            if (!presenter.isLoading() && presenter.hasMorePages()) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    // Run this code later on the UI thread
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                            feedRecyclerViewAdapter.loadMoreItems();
                    }, 0);
                }
            }
        }
    }

}
