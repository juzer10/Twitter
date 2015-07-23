package com.twitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.R;
import com.twitter.SingleTweet;
import com.twitter.TweetList;
import com.twitter.models.TweetData;
import com.twitter.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by juzer_000 on 11/14/2014.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<TweetData> mDataSet;
    Context mContext;

    public MyAdapter(List<TweetData> dataSet, Context ctx) {
        mDataSet = dataSet;
        mContext = ctx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView userImage;
        public TextView realName;
        public TextView username;
        public TextView tweet;
        public TextView time;
        private long mItem;
        private TweetData mTweetData;
        private Context mCtx;

        public ViewHolder(View itemLayoutView, Context ctx) {
            super(itemLayoutView);
            userImage = (ImageView) itemLayoutView.findViewById(R.id.user_image);
            realName = (TextView) itemLayoutView.findViewById(R.id.real_name);
            username = (TextView) itemLayoutView.findViewById(R.id.username);
            tweet = (TextView) itemLayoutView.findViewById(R.id.tweet);
            time = (TextView) itemLayoutView.findViewById(R.id.time);
            mCtx = ctx;
            itemLayoutView.setOnClickListener(this);
        }

        public void setItem(long item, TweetData tweetData) {
            mItem = item;
            mTweetData = tweetData;
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(mCtx, SingleTweet.class);
            i.putExtra(Constants.STATUS_ID, mItem);
            i.putExtra(Constants.REAL_NAME, mTweetData.getRealName());
            i.putExtra(Constants.USERNAME, mTweetData.getUsername());
            i.putExtra(Constants.TIME, mTweetData.getTime());
            i.putExtra(Constants.TWEET, mTweetData.getTweet());
            i.putExtra(Constants.USER_IMAGE, mTweetData.getUserImage());

            mCtx.startActivity(i);
            //Log.d("ONCLICK", getPosition() + "-" + mItem);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_layout, null);

        ViewHolder viewHolder = new ViewHolder(itemLayout, mContext);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String url = mDataSet.get(position).getUserImage();
        Context ctx = viewHolder.userImage.getContext();
        Picasso.with(ctx).load(url).into(viewHolder.userImage);

        viewHolder.username.setText(mDataSet.get(position).getUsername());
        viewHolder.realName.setText(mDataSet.get(position).getRealName());
        viewHolder.tweet.setText(mDataSet.get(position).getTweet());
        viewHolder.time.setText(mDataSet.get(position).getTime());
        viewHolder.setItem(mDataSet.get(position).getStatusID(), mDataSet.get(position));

        viewHolder.userImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //TODO go to user profile
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

