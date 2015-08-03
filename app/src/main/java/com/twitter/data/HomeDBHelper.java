package com.twitter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

/**
 * Created by juzer_000 on 11/23/2014.
 */
public class HomeDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Home";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_REAL_NAME = "real_name";
    public static final String COLUMN_TWEET = "tweet";
    public static final String COLUMN_TIME = "time"; //todo how to store time
    public static final String COLUMN_USER_IMAGE = "user_image"; //todo how to store image

    private static final String DATABASE_NAME = "home.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_ID +
            " integer primary key autoincrement, " + COLUMN_USERNAME + " text not null, " + COLUMN_REAL_NAME +
            " text, " + COLUMN_TWEET + " text, " + COLUMN_TIME + " text, " + COLUMN_USER_IMAGE +" text);";

    public HomeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(HomeDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean addTweet(String username, String realName, String tweet, Date tweetDate ) {
        //TODO How to send and receive a Tweet
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_REAL_NAME, realName);
        contentValues.put(COLUMN_TWEET, tweet);
        //contentValues.put(COLUMN_TIME, tweetDate);
        Log.e("test", "Reached DB");

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }
}
