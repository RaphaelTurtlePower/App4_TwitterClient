package com.apps.twitter.client.fragments;

import org.json.JSONObject;

import com.apps.twitter.client.R;
import com.apps.twitter.client.helpers.TwitterApplication;
import com.apps.twitter.client.helpers.TwitterClient;
import com.apps.twitter.client.interfaces.DialogResult;
import com.apps.twitter.client.model.AppUser;
import com.apps.twitter.client.model.Tweet;
import com.apps.twitter.client.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CreateTweetDialog extends DialogFragment {
	public static final int MAX_LENGTH = 140;
	private TwitterClient client;
	private View dialogView;
	DialogResult dialogResult;
	User user;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        client = TwitterApplication.getRestClient();
        user = AppUser.getAppUser();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.new_tweet, null);
        TextView uName = (TextView) dialogView.findViewById(R.id.new_tweet_screen_name);
        TextView name = (TextView) dialogView.findViewById(R.id.new_tweet_name);
        uName.setText(user.getScreenName());
        name.setText(user.getName());
     
        EditText searchText = (EditText) dialogView.findViewById(R.id.text);
        searchText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				TextView textCount = (TextView) dialogView.findViewById(R.id.textCount);
				textCount.setText(Integer.valueOf(MAX_LENGTH - s.length()).toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                      EditText tweet = (EditText) dialogView.findViewById(R.id.text);
                      client.postTweet(new JsonHttpResponseHandler(){
                  		@Override
            			public void onFailure(Throwable e, String s){
            				Log.d("debug", e.toString());
            				Log.d("debug", s);
            			}
            			
            			@Override
            			public void onSuccess(JSONObject jsonObject){
            					dialogResult.finish(Tweet.fromJSON(jsonObject));
            			}  
                      }, tweet.getEditableText().toString());
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                      getDialog().cancel();
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
    public void setDialogResult(DialogResult dr){
    	this.dialogResult = dr;
    }
    
    
}
