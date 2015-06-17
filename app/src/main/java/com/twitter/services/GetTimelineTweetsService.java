package com.twitter.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.twitter.Authentication;
import com.twitter.data.TweetsSQLiteOpenHelper;
import com.twitter.models.Token;
import com.twitter.utils.TwitterInstance;

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
    TweetsSQLiteOpenHelper db = new TweetsSQLiteOpenHelper(this);
    public static List<Status> statuses = null;
    public long lastTweet;

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
            SharedPreferences.Editor prefEditor = pref.edit();
            lastTweet = pref.getLong("Status", 1);

            Twitter twitter = TwitterInstance.getTwitterInstance(this);
            statuses = twitter.getHomeTimeline(new Paging(1, 200, lastTweet));
            for(int i = statuses.size() - 1; i >= 0; i--)
            {
                Log.i(TAG, statuses.get(i).getCreatedAt().toString() + "--" + statuses.get(i).getText());
                db.addTweet(statuses.get(i).getUser().getName(), statuses.get(i).getUser().getScreenName(), statuses.get(i).getText(), statuses.get(i).getCreatedAt().toString(), statuses.get(i).getUser().getOriginalProfileImageURL(), statuses.get(i).getId());
                if (i == 0) {
                    prefEditor.putLong("Status", statuses.get(i).getId());
                    prefEditor.apply();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Status> getTweetsList() {
        return statuses;
    }
}
