package com.apps.twitter.client;


import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.apps.twitter.client.R;
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
		    	startActivity(i);
		    	Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
			}
    	});
    	
     
    }
    
    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }
    
    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {
        getClient().connect();
    }

}
