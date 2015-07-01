package com.twitter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.R;

import java.util.List;

/**
 * Created by juzer on 6/30/2015.
 */
public class TestAdapter extends RecyclerView.Adapter {
    private static final int ORIGINAL_TWEET = 0;
    private static final int OTHER_TWEETS = 1;

    private List<String> mTweets;

    public TestAdapter(List<String> tweets) {
        mTweets = tweets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (ORIGINAL_TWEET == viewType) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_single_tweet, parent, false);
            return new OriginalTweetViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_tweet_list, parent, false);
            return new OtherTweetsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalTweetViewHolder) {
            // populate with tweet data
        } else if (holder instanceof OtherTweetsViewHolder) {
            // populate with tweet data
        }
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    @Override
    public int getItemViewType(int position) {


        if (position == mTweets.size() - 1) {
            // At bottom position
            return ORIGINAL_TWEET;
        } else {
            return OTHER_TWEETS;
        }
    }
}

 class OriginalTweetViewHolder extends RecyclerView.ViewHolder {
    // ViewHolder fields

    public OriginalTweetViewHolder(View view) {
        super(view);
    }
}

 class OtherTweetsViewHolder extends RecyclerView.ViewHolder {
    // ViewHolder fields

    public OtherTweetsViewHolder(View view) {
        super(view);
    }
}

