package com.twitter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.twitter.utils.TwitterInstance;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by juzer on 6/16/2015.
 */
public class SingleTweet extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        long statusID = bundle.getLong("id");
        Twitter twitter = TwitterInstance.getTwitterInstance(this);
        try {
            twitter4j.Status status = new GetTweetTask().execute(twitter, statusID).get();
            Log.w("TWEET", ""+status.getUser().getScreenName() + "--"+status.getText());

        } catch(Exception e) {e.printStackTrace();}
        //setContentView(R.layout.activity_single_tweet);

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
