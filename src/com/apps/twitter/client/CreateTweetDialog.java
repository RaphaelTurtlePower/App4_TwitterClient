package com.apps.twitter.client;

import org.json.JSONObject;

import com.apps.twitter.client.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class CreateTweetDialog extends DialogFragment {
	private TwitterClient client;
	private View dialogView;
	DialogResult dialogResult;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        client = TwitterApplication.getRestClient();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("Submit a new tweet");
        dialogView = inflater.inflate(R.layout.new_tweet, null);
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
    
    public interface DialogResult{
        void finish(Tweet result);
     }
    
}
