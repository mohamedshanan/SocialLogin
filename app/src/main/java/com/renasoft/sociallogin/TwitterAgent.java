package com.renasoft.sociallogin;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;

public class TwitterAgent {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "iRJ9hrAHvW33DNJIJkqN06VCv";
    private static final String TWITTER_SECRET = "cyLNKYKdelCouThO4kwhMTi0gtrhkWyw72pgkYDos50gaTngRG";
    private Activity mActivity;
    TwitterAuthClient mTwitterAuthClient;

    User user;

    public TwitterAgent(Activity activity){
        mActivity = activity;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(mActivity, new Twitter(authConfig));
        mTwitterAuthClient = new TwitterAuthClient();
    }

    public User login() {
        mTwitterAuthClient.authorize(mActivity, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                TwitterSession session = twitterSessionResult.data;

                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();

                // get twitter user instance to send to the details activity
                Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false, new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        user = result.data;
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.d("Error getting user data", exception.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

        return user;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }
}
