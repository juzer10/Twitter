package com.twitter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.squareup.picasso.Picasso;
import com.twitter.adapters.MyAdapter;
import com.twitter.models.TweetData;
import com.twitter.utils.Constants;
import com.twitter.utils.Regex;
import com.twitter.utils.TweetTextFormatter;
import com.twitter.utils.TwitterInstance;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;

/**
 * Created by juzer on 6/16/2015.
 */
public class SingleTweet extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String TAG = "SingleTweet";
    public Context mContext;
    public boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_tweet);
        mContext= this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Twitter");
        //toolbar.setNavigationIcon(R.drawable.star);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /*
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "In UP button");

            }
        });

        */


        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected)
            Toast.makeText(mContext, "No Internet Connection Available", Toast.LENGTH_SHORT).show();


        Bundle bundle = getIntent().getExtras();
        long statusID = bundle.getLong(Constants.STATUS_ID);
        String mRealName = bundle.getString(Constants.REAL_NAME);
        String mUsername = bundle.getString(Constants.USERNAME);
        String mUserImage = bundle.getString(Constants.USER_IMAGE);
        String mTweet = bundle.getString(Constants.TWEET);
        // TODO time


        Twitter twitter = TwitterInstance.getTwitterInstance(this);
        twitter4j.Status status = null;

        ImageView userImage = (ImageView) findViewById(R.id.user_image);
        TextView realName = (TextView) findViewById(R.id.real_name);
        TextView username = (TextView) findViewById(R.id.username);
        TextView tweet = (TextView) findViewById(R.id.tweet);

        Picasso.with(this).load(mUserImage).into(userImage);
        realName.setText(mRealName);
        username.setText( mUsername);
        tweet.setText(mTweet);

        try {
             new GetTweetTask().execute(twitter, statusID);
            //status = new GetTweetTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, twitter, statusID).get();
        } catch(Exception e) {e.printStackTrace();}


        mRecyclerView = (RecyclerView) findViewById(R.id.replies);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
/*
        Picasso.with(this).load(status.getUser().getBiggerProfileImageURL()).into(userImage);
        realName.setText(status.getUser().getName());
        username.setText("@" + status.getUser().getScreenName());*/


     //   ImageButton favoriteButton = (ImageButton) findViewById(R.id.favorite_button);



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

        Log.e(TAG, "End of SingleTweet");
    }


    private class GetTweetTask extends AsyncTask<Object, Void, twitter4j.Status> {
        @Override
        protected twitter4j.Status doInBackground(Object... params ) {
            twitter4j.Status status = null;
            if (isConnected) {
                Twitter twitter = (Twitter) params[0];
                long statusID = (long) params[1];
                try {
                    status = twitter.showStatus(statusID);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
            return status;
        }

        @Override
        protected void onPostExecute(twitter4j.Status status) {
            super.onPostExecute(status);
            TextView tweet = (TextView) findViewById(R.id.tweet);
            if(isConnected) {
                String tweetText = TweetTextFormatter.linkFormatter(status, TweetTextFormatter.mediaFormatter(status));
                tweet.setText(tweetText);


                String mediaURL = "";
                ImageView tweetImage = (ImageView) findViewById(R.id.tweet_image);
                MediaEntity[] mediaEntities = status.getMediaEntities();
                if (mediaEntities.length > 0) {
                    mediaURL = mediaEntities[0].getMediaURL();
                    Picasso.with(mContext).load(mediaURL).into(tweetImage);
                }
            }
            new LinkBuilder(tweet).addLinks(getTweetLinks()).build();

Log.e(TAG, "Before RecyclerView");
            List <TweetData> replies = fetchReplies(status);
            mAdapter = new MyAdapter(replies, mContext);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        }
    }


    protected List<TweetData> fetchReplies(Status status) {
        List<TweetData> replies = new ArrayList<>();
        try {
            List<twitter4j.Status> statuses = new GetRepliesTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "@" + status.getUser().getScreenName(), this).get();
            for (int i = statuses.size()-1; i >= 0; i--) {
                if(statuses.get(i).getInReplyToStatusId() == status.getId()) {
                    Log.e("STATUSES", statuses.get(i).getText());
Log.e(TAG, "Fetching Replies");
                    replies.add(new TweetData(statuses.get(i).getUser().getName(), statuses.get(i).getUser().getScreenName(), statuses.get(i).getText(), statuses.get(i).getUser().getOriginalProfileImageURL(), statuses.get(i).getCreatedAt().toString(), statuses.get(i).getId()));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
Log.e(TAG, "Got Replies");
        return replies;
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

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);
        }

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




    private List<Link> getTweetLinks() {
        List <Link> links = new ArrayList<>();

        Link mentions = new Link(Pattern.compile("@\\w{1,15}"));
        mentions.setTextColor(getResources().getColor(R.color.primary));
        mentions.setHighlightAlpha(.4f);
        mentions.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Toast.makeText(mContext, clickedText, Toast.LENGTH_SHORT).show();
            }
        });

        Link hashtags = new Link(Pattern.compile("#\\w{1,139}"));
        hashtags.setTextColor(getResources().getColor(R.color.primary));
        hashtags.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Toast.makeText(mContext, clickedText, Toast.LENGTH_SHORT).show();
            }
        });

        Link url = new Link(Regex.VALID_URL);
        url.setTextColor(getResources().getColor(R.color.primary));
        url.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Toast.makeText(mContext, clickedText, Toast.LENGTH_SHORT).show();
            }
        });

        links.add(mentions);
        links.add(hashtags);
        links.add(url);
        return links;
    }
}
