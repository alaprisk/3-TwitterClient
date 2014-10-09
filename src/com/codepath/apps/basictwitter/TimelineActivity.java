package com.codepath.apps.basictwitter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.codepath.apps.basictwitter.models.ComposeTweetDialog;
import com.codepath.apps.basictwitter.models.ComposeTweetDialog.ComposeTweetDialogListener;
import com.codepath.rishi.basictwitter.fragments.HomeTimelineFragment;
import com.codepath.rishi.basictwitter.fragments.MentionsTimelineFragment;



public class TimelineActivity extends FragmentActivity implements ComposeTweetDialogListener{

	
	//private ListView lvTweets;
	FragmentManager fragmentManager = getSupportFragmentManager();
	private HomeTimelineFragment fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		
		setupTabs();

		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		
		//populateTimeline(null);
		//lvTweets = (ListView)findViewById(R.id.lvTweets);
		
		
		//aTweets = new ArrayAdapter<Tweet>(this, android.R.layout.simple_list_item_1, tweets);
		
		//After building tweetArrayAdapter instead of new ArrayAdapter
		

		//fragment = (HomeTimelineFragment) fragmentManager.findFragmentById(R.id.fragment_timeline);

		
		
		
		
		
		
	}
	

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "first",
								HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "second",
								MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
	}
	



	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		getMenuInflater().inflate(R.menu.tweets, menu);
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


	public void onProfileView(MenuItem mi){
		
		Intent i = new Intent(this, ProfileActivity.class);
		
		
		i.putExtra("screen_name", "");

		startActivity(i);
		
	}







	@Override
	public void finish(String result) {
		fragmentManager = getSupportFragmentManager();
		//fragmentManager.executePendingTransactions();
		fragment = (HomeTimelineFragment) fragmentManager.findFragmentById(R.id.flContainer);
		fragment = (HomeTimelineFragment) fragmentManager.findFragmentById(R.id.flContainer);
		if(fragment == null) 
		{	
			//Toast.makeText(this, "from finish : fragment is null", Toast.LENGTH_SHORT).show();
		}else{
			
			//Toast.makeText(this, "from finish : fragment is NOT null", Toast.LENGTH_SHORT).show();
		}
		fragment.finish(result);
	}
	
	
	
}
