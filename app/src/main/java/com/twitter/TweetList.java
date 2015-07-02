package com.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.twitter.adapters.MyAdapter;
import com.twitter.data.TweetsSQLiteOpenHelper;
import com.twitter.models.TweetData;
import com.twitter.services.GetTimelineTweetsService;

import java.util.ArrayList;
import java.util.List;


public class TweetList extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Context mCtx;

    TweetsSQLiteOpenHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = this.getSharedPreferences("Twitter", Context.MODE_PRIVATE);
        boolean firstTime = pref.getBoolean("FirstTime", false);
        mCtx = this;
        if(!firstTime)
        {
            Intent i = new Intent(TweetList.this, Authentication.class);
            startActivity(i);
        }

        Intent i = new Intent(TweetList.this, GetTimelineTweetsService.class);
        startService(i);
        setContentView(R.layout.activity_tweet_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mDbHelper = new TweetsSQLiteOpenHelper(this);
        //TweetData myDataset[] = {new TweetData("@juzer10", "Juzer", "My First Tweet!",R.drawable.ic_launcher,"2m" ), new TweetData("@juzer10", "Juzer", "My First Tweet!", R.drawable.ic_launcher,"4m")};
        //ArrayList<TweetData> myDataset = new ArrayList<>();
       //myDataset.add(new TweetData("@juzer10", "Juzer", "My First Tweet!","","2m", 234 ));
        List<TweetData> myDataset = mDbHelper.getAllTweets();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(myDataset, this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Twitter");

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent i = new Intent(TweetList.this, GetTimelineTweetsService.class);
                startService(i);
                List<TweetData> myDataset = mDbHelper.getAllTweets();
                mRecyclerView.setAdapter(mAdapter);
                mAdapter = new MyAdapter(myDataset, mCtx);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
