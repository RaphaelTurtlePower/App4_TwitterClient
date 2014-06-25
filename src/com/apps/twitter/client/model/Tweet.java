package com.apps.twitter.client.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;

import android.text.format.DateUtils;

@Table(name = "Tweets")
public class Tweet extends Model {
	@Column(name="body")
	private String body;
	@Column(name="u_id", unique=true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name="created_at")
	private String createdAt;
	@Column(name="user")
	private User user;
	
	public static Tweet fromJSON(JSONObject jsonObject){
		try{
			Tweet tweet = findTweet(jsonObject.getLong("id"));
			if(tweet == null){
			Log.d("debug", "Tweet Not found: " + jsonObject.getLong("id"));
			}else{
				Log.d("debug", "Tweet found:" + tweet.getBody());
			}
			if(tweet == null){
				tweet =	new Tweet();
				tweet.body = jsonObject.getString("text");
				tweet.createdAt = jsonObject.getString("created_at");
				tweet.uid = jsonObject.getLong("id");
				User user = User.fromJSON(jsonObject.getJSONObject("user"));
				tweet.user = user;
				tweet.save();
				Log.d("debug", "Saving new tweet.");
			}
			return tweet;
		}catch(JSONException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static Tweet findTweet(Long id){
		return new Select().from(Tweet.class).where("u_id = ?", id).executeSingle();
	}
	
	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		for(int i=0; i<jsonArray.length(); i++){
			JSONObject tweetJson = null;
			try{
				tweetJson = jsonArray.getJSONObject(i);
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
		
			Tweet tweet = Tweet.fromJSON(tweetJson);
			if(tweet != null){
				tweets.add(tweet);
			}
		}
		return tweets;
	}
	
	public static ArrayList<Tweet> getInitialTweetList(){
		List<Tweet> tempList = new Select()
		.from(Tweet.class)
		.orderBy("Tweets.u_id DESC")
		.execute();
		
		return (ArrayList<Tweet>)tempList;
	}
	
	public Tweet(){
		
	}
	
	public String getBody() {
		return body;
	}
	
	public long getUid() {
		return uid;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}
	
	public String getRelativeTimeAgo() {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(createdAt).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
	
	public User getUser() {
		return user;
	}
	
	@Override
	public String toString(){
		return getBody() + " - " + getUser().getScreenName();
	}
	
}
