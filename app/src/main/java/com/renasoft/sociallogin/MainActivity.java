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

    GoogleSignInAccount userAccount;
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

        google_button = (Button) findViewById(R.id.google_button);
        twitter_button = (Button) findViewById(R.id.twitter_button);

        google_button.setOnClickListener(this);
        twitter_button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        googleAgent = new GoogleAgent(this);
        twitterAgent = new TwitterAgent(this);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == GoogleAgent.GOOGLE_SIGNIN_CODE) {

                userAccount = googleAgent.onActivityResult(data);
                if (userAccount != null) {

                    // start UserDataActivty to display the data of the current google user "userAccount"
                    Intent googleIntent = new Intent(this, UserDataActivity.class);
                    Bundle accountBundle = new Bundle();
                    accountBundle.putParcelable(BUNDLE_KEY, userAccount);
                    googleIntent.putExtras(accountBundle);
                    startActivity(googleIntent);
                }
            } else {
                twitterAgent.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
