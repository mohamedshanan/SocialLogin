package com.renasoft.sociallogin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserDataActivityFragment extends Fragment {
    Button signOut;

    public UserDataActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_data, container, false);
        GoogleSignInAccount account = (GoogleSignInAccount) getActivity().getIntent().getExtras().get("userAccount");

        ImageView profile_photo = (ImageView) rootView.findViewById(R.id.profile_photo);
        TextView id = (TextView) rootView.findViewById(R.id.id);
        TextView display_name = (TextView) rootView.findViewById(R.id.display_name);
        TextView email = (TextView) rootView.findViewById(R.id.email);
        TextView scopes = (TextView) rootView.findViewById(R.id.scopes);

        if(account.getPhotoUrl()!=null) {
            Picasso.with(getActivity()).load(account.getPhotoUrl()).into(profile_photo);
        }
        id.setText(account.getId());
        display_name.setText(account.getDisplayName()+"");
        email.setText(account.getEmail()+"");
        scopes.setText(account.getGrantedScopes().toString());

        return rootView;

    }
}
