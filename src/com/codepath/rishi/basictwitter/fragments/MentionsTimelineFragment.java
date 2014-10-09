package com.codepath.rishi.basictwitter.fragments;

import org.json.JSONArray;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.models.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MentionsTimelineFragment extends TweetsListFragments {

	private TwitterClient client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		client = TwitterApplication.getRestClient();
	
		
		if(isNetworkAvailable())
		{
			populateTimeline(null);
		}else{
			Toast.makeText(getActivity(), "No network connectivity, Please Check !", Toast.LENGTH_LONG).show();
			clear();
			notifyDataSetInvalidated();
		}	
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout.
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		
		//final PullToRefreshListView lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
		
		lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
		setAdapter();
		
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
		        //customLoadMoreDataFromApi(page); 
	                // or customLoadMoreDataFromApi(totalItemsCount); 
		    	
		    	//Toast.makeText(TimelineActivity.this, "from onLoadMore", Toast.LENGTH_SHORT).show();
		    	
		    	Tweet lastTweet = (Tweet) lvTweets.getItemAtPosition(totalItemsCount-1);
		    	if(lastTweet == null){
		    		populateTimeline(null);

	        	}else{
	        		populateTimeline(String.valueOf(lastTweet.getUid()-1));
	        	}
		    }
	    });
		
		
		return v;
	}
	
	public void populateTimeline(String maxId)
	{
		if(maxId == null)
		{	
			//Toast.makeText(getActivity(), "maxId == null", Toast.LENGTH_SHORT).show();
			Log.d("debug", "maxId == null");

			client.getMentionsTimeline(new JsonHttpResponseHandler(){
			
				@Override
				public void onSuccess(JSONArray json) {
	
					//Toast.makeText(getActivity(), "onSuccess getHomeTimeline", Toast.LENGTH_SHORT).show();
					Log.d("debug", "from onSuccess");
					Log.d("debug", json.toString());
					
					addAll(Tweet.fromJSONArray(json));
					notifyDataSetChanged();
				
	
				}
				
				@Override
				public void onFailure(Throwable e, String s) {
					Toast.makeText(getActivity(), "onFailure getHomeTimeline", Toast.LENGTH_SHORT).show();

					Log.d("From TimelineActivity, getHomeTimeline failure", e.toString());
				}
				
				@Override
				protected void handleFailureMessage(Throwable arg0, String arg1) {
					Toast.makeText(getActivity(), "handleFailureMessage getHomeTimeline", Toast.LENGTH_SHORT).show();

					Log.d("From TimelineActivity, getHomeTimeline failure", arg1.toString());
				}
				
			});
		}
		else
		{
	    	//Toast.makeText(getActivity(), "from populateTimeline after onLoadMore", Toast.LENGTH_SHORT).show();
	    	
			client.getMoreMentionsTimeline(maxId, new JsonHttpResponseHandler(){
				
				@Override
				public void onSuccess(JSONArray json) {
	
					//Toast.makeText(getActivity(), "onSuccess getMoreHomeTimeline", Toast.LENGTH_SHORT).show();
					Log.d("debug", "from onSuccess");
					Log.d("debug", json.toString());
					
					addAll(Tweet.fromJSONArray(json));
	            	//aTweets.notifyDataSetChanged();
					
	
				}
				
				@Override
				public void onFailure(Throwable e, String s) {
					Log.d("From TimelineActivity, failure", e.toString());
				}
				
				@Override
				protected void handleFailureMessage(Throwable arg0, String arg1) {
					Toast.makeText(getActivity(), "handleFailureMessage getMoreHomeTimeline", Toast.LENGTH_SHORT).show();

					Log.d("From TimelineActivity, getMoreHomeTimeline failure", arg1.toString());
				}
				
				
			});	    	
	    	
		}
	}
	
}
