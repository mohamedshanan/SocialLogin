package com.renasoft.sociallogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
            String name = getIntent().getStringExtra("name");
            String email = getIntent().getStringExtra("email");
            String createdAt = getIntent().getStringExtra("createdAt");
            String description = getIntent().getStringExtra("description");
            String image = getIntent().getStringExtra("image");

            ImageView profile_photo = (ImageView) findViewById(R.id.t_photo);
            TextView tv_name = (TextView) findViewById(R.id.t_name);
            TextView tv_email = (TextView) findViewById(R.id.t_email);
            TextView tv_createdAt = (TextView) findViewById(R.id.t_createdAt);
            TextView tv_desc = (TextView) findViewById(R.id.t_descrition);

            if( image !=null && image != "" ) {
                Picasso.with(this).load(image).into(profile_photo);
            }
            tv_name.setText(name);
            tv_email.setText(email);
            tv_createdAt.setText(createdAt);
            tv_desc.setText(description);
            return true;
        } else {
            return false;
        }
    }
}
