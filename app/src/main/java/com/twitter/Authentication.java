package com.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.twitter.models.Token;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by juzer_000 on 11/20/2014.
 */
public class Authentication extends Activity {

    static String PREFERENCE_NAME = "twitter_oauth";
    public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn";

    static String CALLBACK_URL = "oauth://twitter";

    Button signInTwitter;
    WebView mWebView;

    private static Twitter twitter;
    private static RequestToken requestToken;
    private static AccessToken accessToken;

    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        SharedPreferences pref = this.getSharedPreferences("Twitter", Context.MODE_PRIVATE);
         boolean firstTime = pref.getBoolean("FirstTime", false);
        if (firstTime)
        {
            Intent i = new Intent(Authentication.this, TweetList.class);
            startActivity(i);
        }
  //      editor.putString("CONSUMER_KEY", CONSUMER_KEY);
    //    editor.putString("CONSUMER_SECRET", CONSUMER_SECRET);
      //  editor.commit();

        signInTwitter = (Button) findViewById(R.id.button_sign_in);
        mWebView = (WebView) findViewById(R.id.twitter_browser);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url != null && url.startsWith(CALLBACK_URL))
                    new AfterLoginTask().execute(url);
                else
                    webView.loadUrl(url);
                return true;
            }
        });

        //mSharedPreferences = getApplicationContext().getSharedPreferences(
        //"MyPref", 0)

        signInTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new LoginTask().execute();
            }
        });

    }

    private void loginToTwitter() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(Token.CONSUMER_KEY);
        builder.setOAuthConsumerSecret(Token.CONSUMER_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();

        try {
            requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public void handleTwitterCallback(String url) {
        Uri uri = Uri.parse(url);

        final String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

        try {
            SharedPreferences preferences = this.getSharedPreferences("Twitter", Context.MODE_PRIVATE);
            Authentication.this.accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

            Log.i("s", accessToken.getToken() + "------" + accessToken.getTokenSecret()+"---"+accessToken.getScreenName());

            SharedPreferences.Editor e = preferences.edit();
            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
            e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
            // e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
            e.apply();


            Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(Token.CONSUMER_KEY);
            builder.setOAuthConsumerSecret(Token.CONSUMER_SECRET);

            String access_token = getPreferences(MODE_PRIVATE).getString(PREF_KEY_OAUTH_TOKEN, "");
            String access_token_secret = getPreferences(MODE_PRIVATE).getString(PREF_KEY_OAUTH_SECRET, "");

            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

//TESR TWEET   twitter4j.Status response = twitter.updateStatus("XXXXXXXXXXXXXXXXX");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class LoginTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            loginToTwitter();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            signInTwitter.setVisibility(View.GONE);
            mWebView.loadUrl(requestToken.getAuthenticationURL());
            mWebView.setVisibility(View.VISIBLE);
            mWebView.requestFocus(View.FOCUS_DOWN);
        }
    }

    class AfterLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            mWebView.clearHistory();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            handleTwitterCallback(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            SharedPreferences pref = getSharedPreferences("Twitter", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("FirstTime", true);
            editor.apply();
            mWebView.setVisibility(View.GONE);
            Intent i = new Intent(Authentication.this, TweetList.class);
            startActivity(i);
            signInTwitter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.getVisibility() == View.VISIBLE) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return;
            } else {
                mWebView.setVisibility(View.GONE);
                return;
            }
        }
        super.onBackPressed();
    }

}
