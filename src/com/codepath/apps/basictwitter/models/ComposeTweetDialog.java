package com.codepath.apps.basictwitter.models;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;

public class ComposeTweetDialog extends DialogFragment{

	private EditText tweet_text;
	private TextView charactersCount;
	private Button btnPost;
	private Button btnCancel;
	
	public ComposeTweetDialog()
	{
		// Empty constructor required for DialogFragment
	}
	
	
	public interface ComposeTweetDialogListener
	{
		//void onFinishEditDialog(String inputText);
		void finish(String result);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.compose_tweet_fragment, container);
		getDialog().setTitle("Post Tweet");
		if( getDialog().getActionBar() != null)
		{
			getDialog().getActionBar().setTitle("Composing Tweet");
		}	
		
		tweet_text = (EditText) view.findViewById(R.id.etTweetText);
		//tweet_text.setText("Just Testing");
		
		charactersCount = (TextView) view.findViewById(R.id.tvRemainingChar);
		btnPost = (Button) view.findViewById(R.id.btnPost);
		btnCancel = (Button) view.findViewById(R.id.btnCancel);

		tweet_text.addTextChangedListener(new TextWatcher() {
	  		  
	 		   public void afterTextChanged(Editable s) {
	 		   }
	 		 
	 		   public void onTextChanged(CharSequence s, int start, 
	 		     int before, int count) {
	 			  
	 			   String countCharacters = String.valueOf( 100 - (tweet_text.getText().toString().length()));
	 			  charactersCount.setText(countCharacters +" characters remaining");
	 		   }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
	    });
		
		// Buttons methods...
		
		btnPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	ComposeTweetDialogListener result = (ComposeTweetDialogListener) getActivity();
            	result.finish(tweet_text.getText().toString());
            	dismiss();
            }
        });
		
		
		
		
		// Show soft keyboard automatically
		
				tweet_text.requestFocus();
				getDialog().getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		return view;
	}
	
	
	void cancelPosting(View v){
		this.dismiss();
	}
	
	
	
    public static ComposeTweetDialog newInstance(String title) {
    	ComposeTweetDialog frag = new ComposeTweetDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
	
}
