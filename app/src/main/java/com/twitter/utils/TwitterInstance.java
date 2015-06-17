package com.twitter.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.twitter.Authentication;
import com.twitter.models.Token;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by juzer on 6/17/2015.
 */
public class TwitterInstance {
    public static Twitter getTwitterInstance(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences("Twitter", Context.MODE_PRIVATE);
        ConfigurationBuilder builder = new ConfigurationBuilder();
        String access_token = pref.getString(Authentication.PREF_KEY_OAUTH_TOKEN, "");
        String access_secret = pref.getString(Authentication.PREF_KEY_OAUTH_SECRET, "");
        builder.setOAuthConsumerKey(Token.CONSUMER_KEY);
        builder.setOAuthConsumerSecret(Token.CONSUMER_SECRET);
        builder.setOAuthAccessToken(access_token);
        builder.setOAuthAccessTokenSecret(access_secret);

        AccessToken accessToken = new AccessToken(access_token, access_secret);
        return new TwitterFactory(builder.build()).getInstance(accessToken);
    }
}
