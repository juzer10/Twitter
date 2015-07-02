package com.twitter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.adapters.MyAdapter;
import com.twitter.models.TweetData;
import com.twitter.utils.TwitterInstance;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Created by juzer on 6/16/2015.
 */
public class SingleTweet extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String TAG = "SingleTweet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_tweet);
        Bundle bundle = getIntent().getExtras();
        long statusID = bundle.getLong("id");
        Twitter twitter = TwitterInstance.getTwitterInstance(this);
        twitter4j.Status status = null;
        try {
            //status = new GetTweetTask().execute(twitter, statusID).get();
            status = new GetTweetTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, twitter, statusID).get();
        } catch(Exception e) {e.printStackTrace();}
        /*  TODO Better display of conversation
        TweetData[] inReplyTo = fetchInReplyTo(status);
        Log.w(TAG, "" + inReplyTo.length);
        mRecyclerView = (RecyclerView) findViewById(R.id.in_reply_to);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if(inReplyTo.length != 0)
        {
            mAdapter = new MyAdapter(inReplyTo, this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        */


        ImageView userImage = (ImageView) findViewById(R.id.user_image);
        Picasso.with(this).load(status.getUser().getBiggerProfileImageURL()).into(userImage);

        TextView realName = (TextView) findViewById(R.id.real_name);
        realName.setText(status.getUser().getName());

        TextView username = (TextView) findViewById(R.id.username);
        username.setText("@" + status.getUser().getScreenName());

        TextView tweet = (TextView) findViewById(R.id.tweet);
        tweet.setText(status.getText());

        ImageButton favoriteButton = (ImageButton) findViewById(R.id.favorite_button);


        List <TweetData> replies = fetchReplies(status);
        mRecyclerView = (RecyclerView) findViewById(R.id.replies);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(replies, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    protected List<TweetData> fetchInReplyTo(Status status) {
        Twitter twitter = TwitterInstance.getTwitterInstance(this);
        ArrayList<TweetData> inReplyTo = new ArrayList<TweetData>();
        while(status.getInReplyToScreenName() != null) {
            try {
                Log.w("REPLY", status.getInReplyToScreenName());
                status = new GetTweetTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, twitter, status.getInReplyToStatusId()).get();
                inReplyTo.add(new TweetData(status.getUser().getScreenName(), status.getUser().getName(), status.getText(), status.getUser().getOriginalProfileImageURL(), status.getCreatedAt().toString(), status.getId()));

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        Log.w(TAG, "Got Replies");
        return inReplyTo;
    }

    protected List<TweetData> fetchReplies(Status status) {
        List<TweetData> replies = new ArrayList<>();
        try {
            List<twitter4j.Status> statuses = new GetRepliesTask().execute("@"+status.getUser().getScreenName(), this).get();
            for (int i = statuses.size()-1; i >= 0; i--) {
                if(statuses.get(i).getInReplyToStatusId() == status.getId()) {
                    Log.e("STATUSES", statuses.get(i).getText());
                    replies.add(new TweetData(statuses.get(i).getUser().getName(), statuses.get(i).getUser().getScreenName(), statuses.get(i).getText(), statuses.get(i).getUser().getOriginalProfileImageURL(), statuses.get(i).getCreatedAt().toString(), statuses.get(i).getId()));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w(TAG, "Got Replies");
        return replies;
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

    private class GetRepliesTask extends AsyncTask<Object, Void,  List <twitter4j.Status>> {
        @Override
        protected List <twitter4j.Status> doInBackground(Object... params) {
            QueryResult results = null;
            Context ctx = (Context) params[1];
            String username = (String) params[0];
            try {
                Twitter twitter = TwitterInstance.getTwitterInstance(ctx);
                Query query = new Query(username);
                query.setCount(500);
                results = twitter.search(query);
            } catch(Exception e){e.printStackTrace();}
            List <twitter4j.Status> statuses = results.getTweets();
            return statuses;
        }
    }
}
