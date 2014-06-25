package com.apps.twitter.client.activities;


import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.apps.twitter.client.R;
import com.apps.twitter.client.helpers.TwitterApplication;
import com.apps.twitter.client.helpers.TwitterClient;
import com.apps.twitter.client.model.AppUser;
import com.apps.twitter.client.model.User;
import com.codepath.oauth.OAuthLoginActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> {

	TwitterClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {
    	client = TwitterApplication.getRestClient();
    	client.getVerifiedCredentials(new JsonHttpResponseHandler(){
    		@Override
			public void onFailure(Throwable e, String s){
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}
			
			@Override
			public void onSuccess(JSONObject json){
				AppUser.setAppUser(User.fromJSON(json));
				Intent i = new Intent(LoginActivity.this, TimelineActivity.class);
		    	i.putExtra("isConnected", true);
				startActivity(i);
		    	Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
			}
    	});
    }
    
    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }
    
    public Boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
    
    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {
    	if(isNetworkAvailable()){
    		getClient().connect();
    	}else{
    		Intent i = new Intent(LoginActivity.this, TimelineActivity.class);
    		i.putExtra("isConnected", false);
    		startActivity(i);
    		Toast.makeText(LoginActivity.this,  "Offline Mode", Toast.LENGTH_LONG).show();
    	}
    }
    
}
