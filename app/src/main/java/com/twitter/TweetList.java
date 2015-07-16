package com.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.twitter.utils.TweetTextFormatter;
import com.twitter.utils.TwitterInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;


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

      //  Intent i = new Intent(TweetList.this, GetTimelineTweetsService.class);
      //  startService(i);
        setContentView(R.layout.activity_tweet_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mDbHelper = new TweetsSQLiteOpenHelper(this);
        //TweetData myDataset[] = {new TweetData("@juzer10", "Juzer", "My First Tweet!",R.drawable.ic_launcher,"2m" ), new TweetData("@juzer10", "Juzer", "My First Tweet!", R.drawable.ic_launcher,"4m")};
        //ArrayList<TweetData> myDataset = new ArrayList<>();
       //myDataset.add(new TweetData("@juzer10", "Juzer", "My First Tweet!","","2m", 234 ));
        final List<TweetData> myDataset = mDbHelper.getAllTweets();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(myDataset, this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Twitter");

     /*   final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Intent i = new Intent(TweetList.this, GetTimelineTweetsService.class);
                //startService(i);
                int size = -1;
                try {
                    size = new GetTimelineTweetsTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, mCtx).get();
                } catch (Exception e){e.printStackTrace();}

                if(size > 0)
                {
                    List<TweetData> Dataset = mDbHelper.getAllTweets().subList(0,size);
                    for(int i = 0; i<size; i++)
                    {
                        Log.i("", Dataset.get(i).getTweet());
                        myDataset.add(i, Dataset.get(i));
                    }
                    Log.e("TWEETLIST", "" + size);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.scrollToPosition(0);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
*/
        final JellyRefreshLayout mJellyLayout = (JellyRefreshLayout) findViewById(R.id.jelly);
        mJellyLayout.setRefreshListener(new JellyRefreshLayout.JellyRefreshListener() {
            @Override
            public void onRefresh(final JellyRefreshLayout jellyRefreshLayout) {
                //Intent i = new Intent(TweetList.this, GetTimelineTweetsService.class);
                //startService(i);
                jellyRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {


               int size = -1;
                try {
                    size = new GetTimelineTweetsTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, mCtx).get();
                } catch (Exception e){e.printStackTrace();}

                if(size > 0)
                {
                    List<TweetData> Dataset = mDbHelper.getAllTweets().subList(0,size);
                    for(int i = 0; i<size; i++)
                    {
                        Log.i("", Dataset.get(i).getTweet());
                        myDataset.add(i, Dataset.get(i));
                    }
                    Log.e("TWEETLIST", "" + size);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.scrollToPosition(0);

                }
                mJellyLayout.finishRefreshing();



                }
            },500);

        }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class GetTimelineTweetsTask extends AsyncTask<Context, Void, Integer> {
        TweetsSQLiteOpenHelper db;
        long lastTweet;
        List<twitter4j.Status> statuses = null;
        @Override
        protected Integer doInBackground(Context... contexts) {
            Context ctx = contexts[0];
            db = new TweetsSQLiteOpenHelper(ctx);
            try {
                SharedPreferences pref = ctx.getSharedPreferences("Twitter", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = pref.edit();
                lastTweet = pref.getLong("Status", 1);

                Twitter twitter = TwitterInstance.getTwitterInstance(ctx);
                statuses = twitter.getHomeTimeline(new Paging(1, 200, lastTweet));
                for(int i = statuses.size() - 1; i >= 0; i--)
                {
                    String tweetText = TweetTextFormatter.mediaFormatter(statuses.get(i));
                    tweetText = TweetTextFormatter.linkFormatter(statuses.get(i), tweetText);
                   // Log.i(TAG, statuses.get(i).getCreatedAt().toString() + "--" + statuses.get(i).getText());
                    db.addTweet(statuses.get(i).getUser().getName(), statuses.get(i).getUser().getScreenName(), tweetText, statuses.get(i).getCreatedAt().toString(), statuses.get(i).getUser().getOriginalProfileImageURL(), statuses.get(i).getId());
                    if (i == 0) {
                        prefEditor.putLong("Status", statuses.get(i).getId());
                        prefEditor.apply();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return statuses.size();
        }
    }

}
