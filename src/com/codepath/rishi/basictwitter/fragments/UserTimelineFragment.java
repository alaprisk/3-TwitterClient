package com.codepath.rishi.basictwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.models.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

public class UserTimelineFragment extends TweetsListFragments {

	private TwitterClient client;
	String globalScreenName = null;

	
	public void setScreeName(String screenName)
	{
		//Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
		globalScreenName = screenName;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		client = TwitterApplication.getRestClient();
		//Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
		
		
		if(isNetworkAvailable())
		{
			if(globalScreenName == null)
			{	
				//Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();

						client.getUserTimeline(new JsonHttpResponseHandler(){
							
							@Override
							public void onSuccess(JSONArray json) {
				
								//Toast.makeText(getActivity(), "4. onSuccess getUserTimeline", Toast.LENGTH_SHORT).show();
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
		//Toast.makeText(getActivity(), "5", Toast.LENGTH_SHORT).show();

		
		lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
		setAdapter();
		
		//clear();
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {

			@Override
		    public void onLoadMore(int page, int totalItemsCount) {
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
		        //customLoadMoreDataFromApi(page); 
	                // or customLoadMoreDataFromApi(totalItemsCount); 
		    	
		    	//Toast.makeText(getActivity(), "from onLoadMore", Toast.LENGTH_SHORT).show();
				//Toast.makeText(getActivity(), "6", Toast.LENGTH_SHORT).show();

		    	Tweet lastTweet = (Tweet) lvTweets.getItemAtPosition(totalItemsCount-1);
		    	if(lastTweet == null){
		    		populateTimeline(globalScreenName,null);

	        	}else{
	        		populateTimeline(globalScreenName,String.valueOf(lastTweet.getUid()-1));
	        	}
		    }
	    });
		
		
		
		return v;
	}
	
	
	public void populateTimeline(String screenName, String maxId)
	{
		globalScreenName = screenName;
		//Toast.makeText(getActivity(), "7", Toast.LENGTH_SHORT).show();

		/*
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
	    	
			client.getMoreHomeTimeline(maxId, new JsonHttpResponseHandler(){
				
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
		
		*/
		client.getOtherUserTimeline(screenName, maxId, new JsonHttpResponseHandler() {			
			@Override
			public void onSuccess(JSONArray json) {
				//Toast.makeText(getActivity(), "onSuccess getMoreHomeTimeline", Toast.LENGTH_SHORT).show();
				//Toast.makeText(getActivity(), "8", Toast.LENGTH_SHORT).show();

				Log.d("debug", "from onSuccess");
				Log.d("debug", json.toString());
				//if(maxId==null)
				clear();
				addAll(Tweet.fromJSONArray(json));
            	notifyDataSetChanged();

			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug",s.toString());
				Log.d("From TimelineActivity, failure", e.toString());
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
	}
}
