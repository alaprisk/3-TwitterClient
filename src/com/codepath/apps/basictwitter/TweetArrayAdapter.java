package com.codepath.apps.basictwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Get the data item for position
		Tweet tweet = getItem(position);
		
		// Find or inflate the template
		View v;
		
		if(convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.tweet_item, parent, false);
		} else {
			v = convertView;
		}
		
		// Find the views within template
		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
		TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
		TextView tvTime = (TextView) v.findViewById(R.id.tvTime);
		
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		// Populate views with tweet data.
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		
		tvUserName.setText(Html.fromHtml("<b>" + tweet.getUser().getName() + "  </b><font size='1' color='#373737'>"
				+ "@"+tweet.getUser().getScreenName() + "</font>"));
		tvBody.setText(tweet.getBody());
		
		tvTime.setText(getRelativeTimestamp(tweet.getCreatedAt()));
		
		setUpImageClickListener(ivProfileImage, tweet.getUser().getScreenName());
		
		
        /*v.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        	}
		
        });*/
		
		return v;	
	}
	
	private void setUpImageClickListener(ImageView ivProfileImage,
			final String screenName) {
		ivProfileImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(),ProfileActivity.class);
				i.putExtra("screen_name", screenName.substring(0));
				getContext().startActivity(i);
				
			}
		});		
	}

	private CharSequence getRelativeTimestamp(String createdAt) {

		String format = "EEEEE MMM dd HH:mm:s zzz yyyy";
		SimpleDateFormat date = new SimpleDateFormat(format, Locale.ENGLISH);
		date.setLenient(true);
		String relativeDate = "";
		try {
			long dateMillis = date.parse(createdAt).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			//Toast.makeText(, "TweetArrayAdapter failure", Toast.LENGTH_SHORT).show();
			
			e.printStackTrace();
		}
	 
		return relativeDate;
		
		
	}

}
