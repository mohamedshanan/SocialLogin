package com.renasoft.sociallogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class TwitterDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_details);
        if(!bindData()){
            Toast.makeText(TwitterDetails.this, "No Data Received", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean bindData() {
        if( getIntent()!= null ){
            String image = getIntent().getStringExtra("image");
            String name = getIntent().getStringExtra("name");
            String email = getIntent().getStringExtra("email") != "" ? getIntent().getStringExtra("email") : "Not provided";
            String location = getIntent().getStringExtra("location") != "" ? getIntent().getStringExtra("location") : "Not provided";


            ImageView profile_photo = (ImageView) findViewById(R.id.t_photo);
            TextView tv_name = (TextView) findViewById(R.id.t_name);
            TextView tv_email = (TextView) findViewById(R.id.t_email);
            TextView tv_location = (TextView) findViewById(R.id.t_location);

            if( image !=null && image != "" ) {
                Picasso.with(this).load(image).into(profile_photo);
            }
            tv_name.setText(name);
            tv_email.setText(email);
            tv_location.setText(location);
            return true;
        } else {
            return false;
        }
    }
}
