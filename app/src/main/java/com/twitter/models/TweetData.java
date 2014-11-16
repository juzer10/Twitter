package com.twitter.models;

import android.graphics.Bitmap;

/**
 * Created by juzer_000 on 11/15/2014.
 */
public class TweetData {
    private String username;
    private String realName;
    private String tweet;
    private int userImage; //todo
    private String time;

    public TweetData(String username, String realName, String tweet, int userImage, String time) {
        this.realName = realName;
        this.username = username;
        this.tweet = tweet;
        this.userImage = userImage;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
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
