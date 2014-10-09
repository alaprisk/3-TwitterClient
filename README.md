<big><b>4-Twitter with Fragments
======================
<b></big>

Week 4 Project: Twitter with Fragments

---Rishi Kanth Alapati.<br>
Time spent for New Upload: 20 hours spent in total<br>

<b><big>Completed User stories from Project 3:<b></big><br>
REQUIRED User Stories:
- User can sign in to Twitter using OAuth login
- User can view the tweets from their home timeline
  - User should be displayed the username, name, and body for each tweet
  - User should be displayed the relative timestamp for each tweet "8m", "7h"
  - User can view more tweets as they scroll with infinite pagination

- User can compose a new tweet
  - User can click a “Compose” icon in the Action Bar on the top right
  - User can then enter a new tweet and post this to twitter
  - User is taken back to home timeline with new tweet visible in timeline

OPTIONAL User Stories: 
  - User can see a counter with total number of characters left in case of composing tweet
  - User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)

<b><big>User stories in Project 4:<b></big><br>

- User can switch between Timeline and Mention views using tabs.
  - User can view their home timeline tweets.
  - User can view the recent mentions of their username.
  - User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll")

- User can navigate to view their own profile
  - User can see picture, tagline, # of followers, # of following, and tweets on their profile.

- User can click on the profile image in any tweet to see another user's profile.
  - User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
  - Profile view should include that user's timeline

Notes:<br>
-Used 3rd party Library Picasso for loading images<br>
-Still working on opening the twitter app offline and see last loaded tweets <br>

Issues:<br>
  - Seeing an issue that onLoadMore is not getting called when I click & drag on the ListView, though I have called setOnScrollListener in UserTimelineFragment.
  
Walkthrough of all the userstories:
<br> https://github.com/alaprisk/3-TwitterClient/blob/TwitterWithFragments/TwitterWithFragments.gif<br>

