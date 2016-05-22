package com.renasoft.sociallogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by Mohamed on 21/05/2016.
 */
public class PlusAgent implements GoogleApiClient.OnConnectionFailedListener ,GoogleApiClient.ConnectionCallbacks {
    private static int SIGN_IN_CODE = 400;
    private AppCompatActivity mActivity;
    private static GoogleApiClient googleApiClient;
    static boolean signOutFlag = false;
    public PlusAgent (AppCompatActivity activity){
        mActivity = activity;

        googleApiClient = new GoogleApiClient.Builder(activity)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .addScope(new Scope(Scopes.PLUS_ME))
                .build();
        connect();
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        mActivity.startActivityForResult(signInIntent, SIGN_IN_CODE);
    }

    public Person getPlusData(int requestCode, Intent data) {
        Person currentPerson = null;
        if (requestCode == SIGN_IN_CODE) {
            try{
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                        currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                    }
                }
            }catch (Exception ex){
                Log.e("Error", ex.getLocalizedMessage());
            }
        }
        return currentPerson;
    }

    public  void signOut(){
        if (googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            googleApiClient.disconnect();
        }
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
        Log.e("Connection failed", connectionResult.getErrorMessage());
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(googleApiClient.isConnected()){
            // logout current user if the received bundle contains a flag for sign out and it is set to true
            if (!(bundle == null) && bundle.getBoolean("signOut")){
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
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
