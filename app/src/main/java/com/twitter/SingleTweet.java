package com.twitter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.adapters.MyAdapter;
import com.twitter.models.TweetData;
import com.twitter.utils.TwitterInstance;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by juzer on 6/16/2015.
 */
public class SingleTweet extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_tweet);
        Bundle bundle = getIntent().getExtras();
        long statusID = bundle.getLong("id");
        Twitter twitter = TwitterInstance.getTwitterInstance(this);
        twitter4j.Status status = null;
        try {
            status = new GetTweetTask().execute(twitter, statusID).get();
            //twitter4j.Status status = new GetTweetTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, twitter, statusID).get();
        } catch(Exception e) {e.printStackTrace();}
        TweetData[] inReplyTo = fetchInReplyTo(status);
        mRecyclerView = (RecyclerView) findViewById(R.id.in_reply_to);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(inReplyTo, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ImageView userImage = (ImageView) findViewById(R.id.user_image);
        Picasso.with(this).load(status.getUser().getBiggerProfileImageURL()).into(userImage);

        TextView realName = (TextView) findViewById(R.id.real_name);
        realName.setText(status.getUser().getName());

        TextView username = (TextView) findViewById(R.id.username);
        username.setText("@"+status.getUser().getScreenName());

        TextView tweet = (TextView) findViewById(R.id.tweet);
        tweet.setText(status.getText());

        ScrollView sv = (ScrollView)findViewById(R.id.scrollView);
        sv.scrollTo(0, userImage.getScrollY());
    }

    protected TweetData[] fetchInReplyTo(Status status) {
        Twitter twitter = TwitterInstance.getTwitterInstance(this);
        ArrayList<TweetData> inReplyTo = new ArrayList<TweetData>();
        while(status.getInReplyToScreenName() != null) {
            try {
                Log.w("REPLY", status.getInReplyToScreenName());
                status = new GetTweetTask().execute(twitter, status.getInReplyToStatusId()).get();
                inReplyTo.add(new TweetData(status.getUser().getScreenName(), status.getUser().getName(), status.getText(), status.getUser().getOriginalProfileImageURL(), status.getCreatedAt().toString(), status.getId()));

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        TweetData[] reply = new TweetData[inReplyTo.size()];
        inReplyTo.toArray(reply);
        return reply;
    }


    private class GetTweetTask extends AsyncTask<Object, Void, twitter4j.Status> {
        @Override
        protected twitter4j.Status doInBackground(Object... params ) {
            twitter4j.Status status = null;
            Twitter twitter = (Twitter)params[0];
            long statusID = (long)params[1];
            try {
                status = twitter.showStatus(statusID);
            } catch(TwitterException e) {e.printStackTrace();}

            return status;
        }
    }
}
