package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.models.ComposeTweetDialog;
import com.codepath.apps.basictwitter.models.ComposeTweetDialog.ComposeTweetDialogListener;
import com.codepath.apps.basictwitter.models.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;



public class TimelineActivity extends FragmentActivity implements ComposeTweetDialogListener{

	private TwitterClient client;
	
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private PullToRefreshListView lvTweets;
	//private ListView lvTweets;
	FragmentManager fragmentManager = getSupportFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		client = TwitterApplication.getRestClient();
		
		//populateTimeline(null);
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		//lvTweets = (ListView)findViewById(R.id.lvTweets);
		
		
		tweets = new ArrayList<Tweet>();
		//aTweets = new ArrayAdapter<Tweet>(this, android.R.layout.simple_list_item_1, tweets);
		
		//After building tweetArrayAdapter instead of new ArrayAdapter
		aTweets = new TweetArrayAdapter(this, tweets);
		
		lvTweets.setAdapter(aTweets);	
		
		if(isNetworkAvailable())
		{
			populateTimeline(null);
		}else{
			Toast.makeText(this, "No network connectivity, Please Check !", Toast.LENGTH_LONG).show();
			aTweets.clear();
			aTweets.notifyDataSetInvalidated();
		}	
		
		
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
    			//fetchTimelineAsync(0);
    			
            	aTweets.clear();
    			populateTimeline(null);
            	lvTweets.onRefreshComplete();
            }
        });
		
		
		
	}
	
	@Override
	public void finish(String result) {
		client.postTweet(result, new JsonHttpResponseHandler() {
			        @Override
					public void onSuccess(JSONObject object) {
						aTweets.clear();
						populateTimeline(null);
						
					}
					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("debug",s.toString());
						// TODO Auto-generated method stub
					}
				});				
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
	    
    	Tweet latest = (Tweet) aTweets.getItem(0);
    	if(latest == null) {
        	//aTweets.clear();
        	//aTweets.notifyDataSetChanged();
    		//Toast.makeText(TimelineActivity.this, "fetchTimelineAsync sinceId = 1", Toast.LENGTH_SHORT).show();
    	} else {
    		sinceId = String.valueOf(latest.getUid()+1);
			//Toast.makeText(TimelineActivity.this, "fetchTimelineAsync sinceId == " + sinceId, Toast.LENGTH_SHORT).show();
    	}
    	
    	//aTweets.clear();
    	//aTweets.notifyDataSetChanged();

    	  client.getHomeTimelineLatest(sinceId, new JsonHttpResponseHandler() {
            
		   
		   public void onSuccess(JSONArray json) {
			   
				//Toast.makeText(TimelineActivity.this, "onSuccess getHomeTimelineLatest", Toast.LENGTH_SHORT).show();
				Log.d("debug", "from onSuccess");
				Log.d("debug", json.toString());

				
            	aTweets.addAll(Tweet.fromJSONArray(json));
            	aTweets.notifyDataSetChanged();

            	lvTweets.onRefreshComplete();
            	
            	
            }

            @Override
			public void onFailure(Throwable e, String s) {
				Log.d("From getHomeTimelineLatest, failure", e.toString());            	
			}
			
            @Override
			public void onFinish() {
            	//Toast.makeText(TimelineActivity.this, "onFinish getHomeTimelineLatest", Toast.LENGTH_SHORT).show();

				lvTweets.onRefreshComplete();
				
				super.onFinish();
			}
        });
    	
    	
    }

	private Boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	public void populateTimeline(String maxId)
	{
		if(maxId == null)
		{	
			//Toast.makeText(TimelineActivity.this, "maxId == null", Toast.LENGTH_SHORT).show();
			Log.d("debug", "maxId == null");

			client.getHomeTimeline(new JsonHttpResponseHandler(){
			
				@Override
				public void onSuccess(JSONArray json) {
	
					//Toast.makeText(TimelineActivity.this, "onSuccess getHomeTimeline", Toast.LENGTH_SHORT).show();
					Log.d("debug", "from onSuccess");
					Log.d("debug", json.toString());
					
					aTweets.addAll(Tweet.fromJSONArray(json));
	            	aTweets.notifyDataSetChanged();
				
	
				}
				
				@Override
				public void onFailure(Throwable e, String s) {
					Toast.makeText(TimelineActivity.this, "onFailure getHomeTimeline", Toast.LENGTH_SHORT).show();

					Log.d("From TimelineActivity, failure", e.toString());
				}
			});
		}
		else
		{
	    	//Toast.makeText(TimelineActivity.this, "from populateTimeline after onLoadMore", Toast.LENGTH_SHORT).show();
	    	
			client.getMoreHomeTimeline(maxId, new JsonHttpResponseHandler(){
				
				@Override
				public void onSuccess(JSONArray json) {
	
					//Toast.makeText(TimelineActivity.this, "onSuccess getMoreHomeTimeline", Toast.LENGTH_SHORT).show();
					Log.d("debug", "from onSuccess");
					Log.d("debug", json.toString());
					
					aTweets.addAll(Tweet.fromJSONArray(json));
	            	//aTweets.notifyDataSetChanged();
					
	
				}
				
				@Override
				public void onFailure(Throwable e, String s) {
					Log.d("From TimelineActivity, failure", e.toString());
				}
			});	    	
	    	
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if(id == R.id.miCompose) {
			
			//Toast.makeText(this, "Compose tweet clicked", Toast.LENGTH_SHORT).show();

			ComposeTweetDialog composeTweet = new ComposeTweetDialog();
			composeTweet.show(fragmentManager, "New Tweet");
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
