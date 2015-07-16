package com.twitter.utils;

import android.util.Log;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

/**
 * Created by juzer on 7/16/2015.
 */
public class TweetTextFormatter {
    /*
    Always call MediaFormatter before linkFormatter
     */
    public static String mediaFormatter(Status status) {
        String tweetText;
        String mediaURL = "";
        String originalMediaURL = "";
        for(MediaEntity mediaEntity : status.getMediaEntities())
        {
           // mediaURL = mediaEntity.getMediaURL();
            originalMediaURL = mediaEntity.getURL();
             //Log.e("", originalMediaURL + "----------" + mediaURL);
        }
        tweetText = status.getText();
        tweetText = tweetText.replaceAll(originalMediaURL ,"");
        return tweetText;
    }

    public static String linkFormatter(Status status, String tweet) {
        String tweetText;
        String linkURL = "";
        String originalLinkURL = "";
        for(URLEntity urlEntity : status.getURLEntities())
        {
            linkURL = urlEntity.getDisplayURL();
            originalLinkURL = urlEntity.getURL();
          //  Log.e(TAG, originalLinkURL+"---------"+linkURL);
        }
        tweetText = tweet;
        tweetText = tweetText.replaceAll(originalLinkURL ,linkURL);

        return tweetText;
    }
}
