package com.twitter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.twitter.models.TweetData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juzer_000 on 11/23/2014.
 */
public class HomeSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Tweets";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_REAL_NAME = "real_name";
    public static final String COLUMN_TWEET = "tweet";
    public static final String COLUMN_TIME = "time"; //todo how to store time
    public static final String COLUMN_USER_IMAGE = "user_image"; //todo how to store image

    private static final String DATABASE_NAME = "home.db";
    private static final int DATABASE_VERSION = 1;

    private String[] allColumns = {HomeSQLiteOpenHelper.COLUMN_ID, HomeSQLiteOpenHelper.COLUMN_USERNAME,
            HomeSQLiteOpenHelper.COLUMN_REAL_NAME, HomeSQLiteOpenHelper.COLUMN_TWEET, HomeSQLiteOpenHelper.COLUMN_USER_IMAGE,
            HomeSQLiteOpenHelper.COLUMN_USER_IMAGE, HomeSQLiteOpenHelper.COLUMN_TIME};

    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_ID +
            " int primary key, " + COLUMN_USERNAME + " text not null, " + COLUMN_REAL_NAME +
            " text, " + COLUMN_TWEET + " text, " + COLUMN_TIME + " text, " + COLUMN_USER_IMAGE +" text);";

    public HomeSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(HomeSQLiteOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    /*
    CRUD Operations
     */

    public void addTweet(String username, String tweet) {
        //TODO How to send and receive a Tweet
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_TWEET, tweet);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<TweetData> getAllTweets() {
        SQLiteDatabase database = this.getWritableDatabase();
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
