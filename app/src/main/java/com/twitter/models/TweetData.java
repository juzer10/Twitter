package com.twitter.models;

import android.graphics.Bitmap;
import android.util.Log;

import com.twitter.utils.DateTimeConverter;

/**
 * Created by juzer_000 on 11/15/2014.
 */
public class TweetData {
    private String username;
    private String realName;
    private String tweet;
    private String userImage; //todo
    private String time;
    private long statusID;

    public TweetData() {

    }

    public TweetData(String username, String realName, String tweet, String userImage, String time, long statusID) {
        this.realName = realName;
        this.username = username;
        this.tweet = tweet;
        this.userImage = userImage;
        this.time = time;
        this.statusID = statusID;
    }

    public long getStatusID() {
        return statusID;
    }

    public void setStatusID(long statusID) {
        this.statusID = statusID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String date) {
        String time = DateTimeConverter.DateToMinutes(date);
        this.time = time;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = "@"+username;
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
