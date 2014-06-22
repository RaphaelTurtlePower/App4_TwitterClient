package com.apps.twitter.client;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.apps.twitter.client.CreateTweetDialog.DialogResult;
import com.apps.twitter.client.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private TweetArrayAdapter aTweets;
	private ListView lvTweets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		populateTimeline();
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
	}
	
	public void populateTimeline(){
		client.getTwitterTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onFailure(Throwable e, String s){
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}
			
			@Override
			public void onSuccess(JSONArray json){
				aTweets.addAll(Tweet.fromJSONArray(json));
			}
		});
	}
	
	// Inflate the menu; this adds items to the action bar if it is present.
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.timeline, menu);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle item selection
		    if(item.getItemId() == R.id.new_tweet) {
		    	CreateTweetDialog newDialog = new CreateTweetDialog();
		    	newDialog.setDialogResult(new DialogResult(){
		    		@Override
					public void finish(Tweet result) {
		    			Log.d("debug", result.getBody() + result.getUid());
		    			Toast.makeText(TimelineActivity.this, result.getBody(), Toast.LENGTH_LONG).show();
		    			aTweets.insert(result,0);
					}
		    		
		    	});
		    	newDialog.show(getFragmentManager(), "dialog");
		    
		    }
		    return true;
		}
		
	
}
