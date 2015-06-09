package com.twitter.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.twitter.Authentication;
import com.twitter.data.HomeSQLiteOpenHelper;
import com.twitter.models.Token;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by juzer_000 on 11/27/2014.
 */
public class GetTimelineTweetsService extends IntentService {
    public static final String TAG = "GetTimelineTweetService";
    HomeSQLiteOpenHelper db = new HomeSQLiteOpenHelper(this);
    public static List<Status> statuses = null;

    public GetTimelineTweetsService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "In service");
        fetchTweets();
    }

    public void fetchTweets() {
        try {
            SharedPreferences pref = this.getSharedPreferences("Twitter", Context.MODE_PRIVATE);

            ConfigurationBuilder builder = new ConfigurationBuilder();
            String access_token = pref.getString(Authentication.PREF_KEY_OAUTH_TOKEN, "");
            String access_secret = pref.getString(Authentication.PREF_KEY_OAUTH_SECRET, "");
           // Log.e("fadfadf", access_secret + "-------" + access_secret);
            builder.setOAuthConsumerKey(Token.CONSUMER_KEY);
            builder.setOAuthConsumerSecret(Token.CONSUMER_SECRET);
            builder.setOAuthAccessToken(access_token);
            builder.setOAuthAccessTokenSecret(access_secret);

            AccessToken accessToken = new AccessToken(access_token, access_secret);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
            //twitter4j.Status response = twitter.updateStatus("YYYY");
            statuses = twitter.getHomeTimeline();
            for(Status status: statuses) {
                Log.i(TAG, status.getUser().getName() + "--" + status.getText());
                db.addTweet(status.getUser().getName(), status.getText());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Status> getTweetsList() {
        return statuses;
    }
}
