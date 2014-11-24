package com.twitter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by juzer_000 on 11/23/2014.
 */
public class HomeSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Home";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_REAL_NAME = "real_name";
    public static final String COLUMN_TWEET = "tweet";
    public static final String COLUMN_TIME = "time"; //todo how to store time
    public static final String COLUMN_USER_IMAGE = "user_image"; //todo how to store image

    private static final String DATABASE_NAME = "home.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table" + TABLE_NAME + "(" + COLUMN_ID +
            "integer primary key autoincrement, " + COLUMN_USERNAME + "text not null, " + COLUMN_REAL_NAME +
            "text, " + COLUMN_TWEET + "text, " + COLUMN_TIME + "text, " + COLUMN_USER_IMAGE +"text);";

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
}
