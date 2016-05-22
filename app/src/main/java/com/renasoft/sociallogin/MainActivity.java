package com.renasoft.sociallogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.twitter.sdk.android.core.models.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String BUNDLE_KEY = "userAccount";
    GoogleAgent googleAgent;
    TwitterAgent twitterAgent;
    User twitter_user;

    Button google_button;
    Button twitter_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        googleAgent = new GoogleAgent(this);
        twitterAgent = new TwitterAgent(this);

        google_button = (Button) findViewById(R.id.google_button);
        twitter_button = (Button) findViewById(R.id.twitter_button);

        google_button.setOnClickListener(this);
        twitter_button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case (R.id.google_button):
                googleAgent.signIn();
                break;
            case (R.id.twitter_button):
                twitter_user = twitterAgent.login();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GoogleAgent.GOOGLE_SIGNIN_CODE) {

            GoogleSignInAccount userAccount = googleAgent.checkSignIn(requestCode, data);
            if (userAccount != null) {
                Intent googleIntent = new Intent(this, UserDataActivity.class);
                Bundle accountBundle = new Bundle();
                accountBundle.putParcelable(BUNDLE_KEY, userAccount);
                googleIntent.putExtras(accountBundle);
                startActivity(googleIntent);
            }
        }

        else {
            twitterAgent.onActivityResult(requestCode, resultCode, data);
            if (twitter_user != null) {
                Intent twitterIntent = new Intent(this, TwitterDetails.class);
                twitterIntent.putExtra("name", twitter_user.name);
                twitterIntent.putExtra("email", twitter_user.email);
                twitterIntent.putExtra("createdAt", twitter_user.createdAt);
                twitterIntent.putExtra("description", twitter_user.description);
                twitterIntent.putExtra("image", twitter_user.profileBackgroundImageUrl);

                startActivity(twitterIntent);
            }
        }
    }
}
