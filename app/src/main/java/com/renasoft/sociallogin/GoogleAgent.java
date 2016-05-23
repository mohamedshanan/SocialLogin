package com.renasoft.sociallogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

public class GoogleAgent implements GoogleApiClient.OnConnectionFailedListener ,GoogleApiClient.ConnectionCallbacks{

    public static int GOOGLE_SIGNIN_CODE = 400;
    private static GoogleApiClient googleApiClient;
    private AppCompatActivity mActivity;
    public GoogleAgent (AppCompatActivity activity){
        mActivity = activity;
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(activity)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();
        connect();
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        mActivity.startActivityForResult(signInIntent, GOOGLE_SIGNIN_CODE);
    }

    public GoogleSignInAccount onActivityResult(Intent data) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                return result.getSignInAccount();
            }
            else return null;
    }



    public  void signOut(){
        if (!googleApiClient.isConnected()) {
            connect();
        }
        Bundle b = new Bundle();
        b.putBoolean("signOut", true);
        onConnected(b);
    }

    private void connect(){
        if (!googleApiClient.isConnected() ||
                !googleApiClient.isConnecting())
            googleApiClient.connect();
    }

    private void disConnect(){
        if (googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Connection failed", connectionResult.getErrorMessage());
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(googleApiClient.isConnected()){
            // logout current user if the received bundle contains a flag for sign out and it is set to true
            if (!(bundle == null) && bundle.getBoolean("signOut")){
                Log.d("inside onConnected if", " I am about to sign out");
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<com.google.android.gms.common.api.Status>() {
                    @Override
                    public void onResult(com.google.android.gms.common.api.Status status) {
                        Log.d("agent", "you signed out");
                        disConnect();
                    }
                });
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("connect", "connection suspended");
    }
}