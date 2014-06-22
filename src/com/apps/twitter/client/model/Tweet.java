package com.apps.twitter.client.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.text.format.DateUtils;

public class Tweet {
	private String body;
	private long uid;
	private String createdAt;
	private User user;
	
	public static Tweet fromJSON(JSONObject jsonObject){
		Tweet tweet = new Tweet();
		try{
			tweet.body = jsonObject.getString("text");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.uid = jsonObject.getLong("id");
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
		}catch(JSONException e){
			e.printStackTrace();
			tweet = null;
		}
		return tweet;
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
