package com.twitter.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by juzer on 6/16/2015.
 */
public class DateTimeConverter {
    public static String DateToMinutes(String oldDate) {
        String time = "";
        long hour = 3600000;
        try {
            DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date date = formatter.parse(oldDate);
            long timeOfTweet = date.getTime();
            long currentTime = System.currentTimeMillis();
            if(hour < (currentTime - timeOfTweet))
            {
                time = String.format("%dh", TimeUnit.MILLISECONDS.toHours(currentTime - timeOfTweet));
            }
            else
            {
                time = String.format("%dm", TimeUnit.MILLISECONDS.toMinutes(currentTime - timeOfTweet));
            }
        } catch(ParseException e){e.printStackTrace();}
        return time;
    }
}
