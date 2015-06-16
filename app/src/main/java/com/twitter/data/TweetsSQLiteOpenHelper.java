package com.twitter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.twitter.models.TweetData;

/**
 * Created by juzer_000 on 11/23/2014.
 */
public class TweetsSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Tweets";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_REAL_NAME = "real_name";
    public static final String COLUMN_TWEET = "tweet";
    public static final String COLUMN_TIME = "time"; //todo how to store time
    public static final String COLUMN_USER_IMAGE = "user_image"; //todo how to store image

    private static final String DATABASE_NAME = "home.db";
    private static final int DATABASE_VERSION = 1;


    private String[] allColumns = {TweetsSQLiteOpenHelper.COLUMN_ID, TweetsSQLiteOpenHelper.COLUMN_USERNAME,
            TweetsSQLiteOpenHelper.COLUMN_REAL_NAME, TweetsSQLiteOpenHelper.COLUMN_TWEET, TweetsSQLiteOpenHelper.COLUMN_USER_IMAGE,
            TweetsSQLiteOpenHelper.COLUMN_USER_IMAGE, TweetsSQLiteOpenHelper.COLUMN_TIME};

    private static final String DATABASE_CREATE = "create table if not exists " + TABLE_NAME + "(" + COLUMN_ID +
            " int primary key, " + COLUMN_USERNAME + " text not null, " + COLUMN_REAL_NAME +
            " text, " + COLUMN_TWEET + " text, " + COLUMN_USER_IMAGE + " text, " + COLUMN_TIME +" text);";


        public TweetsSQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TweetsSQLiteOpenHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }


/*
    public TweetsSQLiteOpenHelper open() {
        mDbHelper = new TweetsDatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
*/
    /*
    CRUD Operations
     */

    public void addTweet(String realName, String username, String tweet, String time, String userImage) {
        //TODO How to send and receive a Tweet
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REAL_NAME, realName);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_TWEET, tweet);
        values.put(COLUMN_TIME, time);
        Log.w("TIMEw", time);
        values.put(COLUMN_USER_IMAGE, userImage);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public TweetData[] getAllTweets() {
        //List<TweetData> tweetDataList = new ArrayList<TweetData>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TweetsSQLiteOpenHelper.TABLE_NAME, allColumns, null, null, null, null, null);
        TweetData[] tweetDataList = new TweetData[cursor.getCount()];
        cursor.moveToLast();

        for(int i = 0; i < cursor.getCount(); i++)
        {
            TweetData tweetData = cursorToTweetData(cursor);
            tweetDataList[i] = tweetData;
            cursor.moveToPrevious();
        }


        /*while(!cursor.isAfterLast()) {
            TweetData tweetData = cursorToTweetData(cursor);
            tweetDataList.add(tweetData);
            cursor.moveToNext();
        }*/
        cursor.close();
        return tweetDataList;
    }

    private TweetData cursorToTweetData(Cursor cursor) {
        TweetData tweetData = new TweetData();
        tweetData.setUsername(cursor.getString(1));
        tweetData.setRealName(cursor.getString(2));
        tweetData.setTweet(cursor.getString(3));
        tweetData.setUserImage(cursor.getString(4));
        tweetData.setTime(cursor.getString(6));
        return tweetData;
    }
}