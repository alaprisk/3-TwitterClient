package com.codepath.rishi.basictwitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.models.ComposeTweetDialog.ComposeTweetDialogListener;
import com.codepath.apps.basictwitter.models.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragments implements ComposeTweetDialogListener{

	private TwitterClient client;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
		
		// Set a listener to be invoked when the list should be refreshed.
        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call listView.onRefreshComplete() when
                // once the network request has completed successfully.
            	
            	
    			//aTweets.clear();
    			fetchTimelineAsync(0);
    			
            	//aTweets.clear();
    			//populateTimeline(null);
            	//lvTweets.onRefreshComplete();
            }
        });
				
		
		
		return v;
	}
	
	
    public void fetchTimelineAsync(int page) {
        /*client.getHomeTimeline(0, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                // ...the data has come back, finish populating listview...
                // Now we call onRefreshComplete to signify refresh has finished
                lvTweets.onRefreshComplete();
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });*/
    	
	    String sinceId = "1";
	    

    	Tweet latest = getItem(0);
    	if(latest == null) {
        	//aTweets.clear();
        	//aTweets.notifyDataSetChanged();
    		//Toast.makeText(getActivity(), "fetchTimelineAsync sinceId = 1", Toast.LENGTH_SHORT).show();
    	} else {
    		sinceId = String.valueOf(latest.getUid()+1);
			//Toast.makeText(getActivity(), "fetchTimelineAsync sinceId == " + sinceId, Toast.LENGTH_SHORT).show();
    	}
    	
    	//aTweets.clear();
    	notifyDataSetChanged();

    	  client.getHomeTimelineLatest(sinceId, new JsonHttpResponseHandler() {
            
		   
		   public void onSuccess(JSONArray json) {
			   
				//Toast.makeText(getActivity(), "onSuccess getHomeTimelineLatest", Toast.LENGTH_SHORT).show();
				Log.d("debug", "from onSuccess");
				Log.d("debug", json.toString());

				
            	//aTweets.addAll(Tweet.fromJSONArray(json));
            	//aTweets.notifyDataSetChanged();
				
				ArrayList<Tweet> tweets;
				tweets = Tweet.fromJSONArray(json);
				
				for (int i = 0; i < tweets.size(); i++) {
					insert(tweets.get(i), i);
				}
            	
            	onRefreshComplete();
            	
            	
            }

            @Override
			public void onFailure(Throwable e, String s) {
				Log.d("From getHomeTimelineLatest, failure", e.toString());            	
			}
			
            
			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				Toast.makeText(getActivity(), "handleFailureMessage getHomeTimelineLatest", Toast.LENGTH_SHORT).show();

				Log.d("From TimelineActivity, getHomeTimelineLatest failure", arg1.toString());
			}            
            
            @Override
			public void onFinish() {
            	//Toast.makeText(getActivity(), "onFinish getHomeTimelineLatest", Toast.LENGTH_SHORT).show();

            	onRefreshComplete();
				
				super.onFinish();
			}
        });
    	
    	
    }
    
	public void populateTimeline(String maxId)
	{
		if(maxId == null)
		{	
			//Toast.makeText(getActivity(), "maxId == null", Toast.LENGTH_SHORT).show();
			Log.d("debug", "maxId == null");

			client.getHomeTimeline(new JsonHttpResponseHandler(){
			
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
	}
	
	public void finish(String result) {
		//Toast.makeText(getActivity(), "from finish of HomeTimelineFragment", Toast.LENGTH_SHORT).show();
		
		client.postTweet(result, new JsonHttpResponseHandler() {
			        @Override
					public void onSuccess(JSONObject object) {
						Log.d("debug finish method onSuccess","finish method");
						//Toast.makeText(getActivity(), "onSuccess postTweet", Toast.LENGTH_SHORT).show();
			        	clear();
						populateTimeline(null);
						
					}
					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("debug onFailure",s.toString());
						// TODO Auto-generated method stub
					}
					
					@Override
					protected void handleFailureMessage(Throwable arg0, String arg1) {
					// TODO Auto-generated method stub
						Log.d("debug handleFailureMessage",arg1.toString());
					}
					
				});				
	 }
	
	


	
}
