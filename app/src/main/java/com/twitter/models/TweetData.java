package com.twitter.models;

/**
 * Created by juzer_000 on 11/15/2014.
 */
public class TweetData {
    private String username;
    private String realName;
    private String tweet;

    public TweetData(String username, String realName, String tweet) {
        this.realName = realName;
        this.username = username;
        this.tweet = tweet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }


}
