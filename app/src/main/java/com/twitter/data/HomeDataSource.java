// DEPRECATED



/*package com.twitter.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.twitter.models.TweetData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
*/
/**
 * Created by juzer_000 on 11/26/2014.
 */
/*
public class HomeDataSource {
    private SQLiteDatabase database;
    private HomeSQLiteOpenHelper dbHelper;
    private String[] allColumns = {HomeSQLiteOpenHelper.COLUMN_ID, HomeSQLiteOpenHelper.COLUMN_USERNAME,
            HomeSQLiteOpenHelper.COLUMN_REAL_NAME, HomeSQLiteOpenHelper.COLUMN_TWEET, HomeSQLiteOpenHelper.COLUMN_USER_IMAGE,
            HomeSQLiteOpenHelper.COLUMN_USER_IMAGE, HomeSQLiteOpenHelper.COLUMN_TIME};


    public HomeDataSource(Context ctx) {
        dbHelper = new HomeSQLiteOpenHelper(ctx);
    }

    public void open()throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addTweet() {
        //TODO How to send and receive a Tweet
    }

    public List<TweetData> getAllTweets() {
        List<TweetData> tweetDataList = new ArrayList<TweetData>();

        Cursor cursor = database.query(HomeSQLiteOpenHelper.TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            TweetData tweetData = cursorToTweetData(cursor);
            tweetDataList.add(tweetData);
            cursor.moveToNext();
        }
        cursor.close();
        return tweetDataList;
    }

    private TweetData cursorToTweetData(Cursor cursor) {
        TweetData tweetData = new TweetData();
        tweetData.setUsername(cursor.getString(1));
        tweetData.setRealName(cursor.getString(2));
        tweetData.setTweet(cursor.getString(3));
       //Todo tweetData.setUserImage(cursor.getString(4));
       //todo tweetData.setTime(cursor.getString(5));
        return tweetData;
    }
}

*/