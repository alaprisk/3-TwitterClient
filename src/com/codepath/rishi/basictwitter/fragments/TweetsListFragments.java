package com.codepath.rishi.basictwitter.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetArrayAdapter;
import com.codepath.apps.basictwitter.models.Tweet;

import eu.erikw.PullToRefreshListView;

public class TweetsListFragments extends Fragment {
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	protected PullToRefreshListView lvTweets;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Non-View initialization.
		
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);
	
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the layout.
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		
		// Assign our view references.
		
	

		
		
		
		// Return the layout view.
		return v;
		
		
	}
	
	public void addAll(ArrayList<Tweet> tweets){
		aTweets.addAll(tweets);
		
	}
	
	public void notifyDataSetChanged(){
		aTweets.notifyDataSetChanged();
	}
	
	public void clear(){
		aTweets.clear();
	}

	public void notifyDataSetInvalidated(){
		aTweets.notifyDataSetInvalidated();
	}
	
	public Tweet getItem(int position){
		return aTweets.getItem(position);
	}

	public void insert(Tweet tweet_to_insert, int position){
		aTweets.insert(tweet_to_insert, position);
	}
	
	public void onRefreshComplete(){
		lvTweets.onRefreshComplete();
	}
	
	public void setAdapter(){
		lvTweets.setAdapter(aTweets);
	}
	
	public Boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
}
