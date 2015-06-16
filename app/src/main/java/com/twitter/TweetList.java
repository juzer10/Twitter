package com.twitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.twitter.adapters.MyAdapter;
import com.twitter.data.TweetsSQLiteOpenHelper;
import com.twitter.models.TweetData;
import com.twitter.services.GetTimelineTweetsService;


public class TweetList extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TweetsSQLiteOpenHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(TweetList.this, GetTimelineTweetsService.class);
        startService(i);
        setContentView(R.layout.activity_tweet_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mDbHelper = new TweetsSQLiteOpenHelper(this);
        //TweetData myDataset[] = {new TweetData("@juzer10", "Juzer", "My First Tweet!",R.drawable.ic_launcher,"2m" ), new TweetData("@juzer10", "Juzer", "My First Tweet!", R.drawable.ic_launcher,"4m")};
        TweetData myDataset[] = mDbHelper.getAllTweets();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(myDataset, this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("YOLO");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
