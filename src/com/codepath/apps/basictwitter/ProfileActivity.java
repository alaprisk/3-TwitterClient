package com.codepath.apps.basictwitter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.models.User;
import com.codepath.rishi.basictwitter.fragments.UserTimelineFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	String screenName = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		screenName = getIntent().getStringExtra("screen_name");
		
		loadProfileInfo();
		
        UserTimelineFragment fragmentDemo = (UserTimelineFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentUserTimeline);
        fragmentDemo.setScreeName(screenName);
        
        fragmentDemo.populateTimeline(screenName,null);
		
	}

	public void populateProfileHeader(User u) {

		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvfollowers = (TextView) findViewById(R.id.tvfollowers);
		TextView tvfollowing = (TextView) findViewById(R.id.tvfollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		
		tvName.setText(u.getName());
		tvTagline.setText(u.getTagline());
		tvfollowers.setText(u.getFollowersCount() + " Followers");
		tvfollowing.setText(u.getFollowingCount() + " Following");
		
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
		
	}
	
	private void loadProfileInfo(){
		
			if(screenName.isEmpty() || screenName == ""){
				//Toast.makeText(this, "screeName is null", Toast.LENGTH_SHORT).show();
				Log.d("from ProfileActivity", "from screenName is null");

				TwitterApplication.getRestClient().getMyInfo(new JsonHttpResponseHandler(){
				
					public void onSuccess(JSONObject json) {
						
						User u = User.fromJSON(json);
						getActionBar().setTitle("@" + u.getScreenName());
						populateProfileHeader(u);
						super.onSuccess(json);

						} 
					
					public void onFailure(Throwable arg0, JSONArray arg1) {
						
						
					}
				} );	
			
			}else {
				Log.d("from ProfileActivity", "from screenName is not null");
				TwitterApplication.getRestClient().getOtherUserInfo(screenName,new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						// TODO Auto-generated method stub
						User u = User.fromJSON(jsonObject);
						getActionBar().setTitle("@"+u.getScreenName());
						populateProfileHeader(u);
						super.onSuccess(jsonObject);
					}
				});
			}			
			
	}
			

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
