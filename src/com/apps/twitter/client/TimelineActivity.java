package com.apps.twitter.client;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.apps.twitter.client.CreateTweetDialog.DialogResult;
import com.apps.twitter.client.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private TweetArrayAdapter aTweets;
	private PullToRefreshListView lvTweets;
	private Long lastId;
	private Long firstId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		populateTimeline(new Long(1), null);
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		lvTweets.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				fetchTimelineAsync();
			}
		});

		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener(){

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline(null, lastId);
			}
			
		});
	}
	
	public void populateTimeline(Long start_id, Long max_id){
		client.getTwitterTimeline(start_id,max_id, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(Throwable e, String s){
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}
			
			@Override
			public void onSuccess(JSONArray json){
				aTweets.addAll(Tweet.fromJSONArray(json));
				Integer totalCount = aTweets.getCount();
				firstId = aTweets.getItem(0).getUid();
				lastId = aTweets.getItem(totalCount-1).getUid();			
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
		    			firstId = result.getUid();
					}
		    		
		    	});
		    	newDialog.show(getFragmentManager(), "dialog");
		    
		    }
		    return true;
		}
		
	    public void fetchTimelineAsync() {
	        client.getTwitterTimeline(firstId, null, new JsonHttpResponseHandler() {
	            public void onSuccess(JSONArray json) {
	            	ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
	            	for(int i=newTweets.size()-1; i>=0; i--){
	            		aTweets.insert(newTweets.get(i), 0);
	            	}
	            	// ...the data has come back, finish populating listview...
	                // Now we call onRefreshComplete to signify refresh has finished
	                lvTweets.onRefreshComplete();
	            }

	            public void onFailure(Throwable e) {
	                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
	            }
	        });
	    }
}
