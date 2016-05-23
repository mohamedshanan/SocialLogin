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
    TwitterAuthClient mTwitterAuthClient;
    String mEmail;
    User user;
    private Activity mActivity;
    private String Tag = "TwitterAgent";

    public TwitterAgent(Activity activity){
        mActivity = activity;
        mEmail = "not found.";
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(mActivity, new Twitter(authConfig));
        mTwitterAuthClient = new TwitterAuthClient();
    }

    public User login() {
        mTwitterAuthClient.authorize(mActivity, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                final TwitterSession session = twitterSessionResult.data;

                // get twitter user instance to send to the details activity
                Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false, new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        user = result.data;
                        if (user != null) displayUserData(user);
                        Toast.makeText(mActivity, user.screenName, Toast.LENGTH_SHORT).show();

                        mTwitterAuthClient.requestEmail(session, new Callback<String>() {
                            @Override
                            public void success(Result<String> result) {
                                mEmail = result.data;
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                Log.d(Tag, "Email request faild");
                                exception.printStackTrace();
                                mTwitterAuthClient = new TwitterAuthClient();
                            }
                        });

                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.d(Tag, exception.getLocalizedMessage());

                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                Log.d(Tag, e.getLocalizedMessage());
            }
        });

        return user;
    }

    private void displayUserData(User twitter_user) {
        if (twitter_user != null) {
            Intent twitterIntent = new Intent(mActivity, TwitterDetails.class);
            twitterIntent.putExtra("name", twitter_user.name);
            twitterIntent.putExtra("email", mEmail);
            twitterIntent.putExtra("location", twitter_user.location);
            twitterIntent.putExtra("description", twitter_user.url);
            twitterIntent.putExtra("image", twitter_user.profileImageUrl);

            mActivity.startActivity(twitterIntent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }
}
