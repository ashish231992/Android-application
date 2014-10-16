package com.example.ankurtest1;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


public class show_home_timeline extends Activity implements SwipeRefreshLayout.OnRefreshListener{
	private static final String TAG = MainActivity.class.getSimpleName();
private static int REFRESH_TIME_IN_SECONDS = 5;
SwipeRefreshLayout swipeRefreshLayout;
EditText ScreenName;
SharedPreferences pref;
	private ListView listView;
	int count;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		final String TWITTER_CONSUMER_KEY = "uMjwOFFZCQTvBXfyNG4i7cY1H";
		final String TWITTER_CONSUMER_SECRET = "5VNIGw5woT7agF4WerWPoQxV3JWVz7vAQjEG9J0JeGocLRgpWV";
		final String twit_access_token = getIntent().getStringExtra("Access_token");
		final String twit_access_token_secret = getIntent().getStringExtra("scret_token");
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(true);
		setContentView(R.layout.show_home_time);
		listView = (ListView) findViewById(R.id.lista_tweets);
		initUI();

		new BuscaTweetsTask().execute(TWITTER_CONSUMER_KEY,TWITTER_CONSUMER_SECRET,twit_access_token,twit_access_token_secret);
	}
	@Override
    public void onBackPressed() {
       
		Intent intent = new Intent( show_home_timeline.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
      
        
    }
	private void initUI() {
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.lySwipeRefresh);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light,
		android.R.color.white, android.R.color.holo_blue_light,
		android.R.color.white);
	
		}
	
	
	@Override
public void onRefresh() {
	
		final String TWITTER_CONSUMER_KEY2 = "uMjwOFFZCQTvBXfyNG4i7cY1H";
		final String TWITTER_CONSUMER_SECRET2 = "5VNIGw5woT7agF4WerWPoQxV3JWVz7vAQjEG9J0JeGocLRgpWV";
		final String twit_access_token2 = getIntent().getStringExtra("Access_token");
		final String twit_access_token_secret2 = getIntent().getStringExtra("scret_token");
		new BuscaTweetsTask().execute(TWITTER_CONSUMER_KEY2,TWITTER_CONSUMER_SECRET2,twit_access_token2,twit_access_token_secret2);
Log.d(TAG, "onRefresh SwipeRefreshLayout");
new Handler().postDelayed(new Runnable() {
@Override
public void run() {
stopSwipeRefresh();
}
}, REFRESH_TIME_IN_SECONDS * 1000);
}
private void stopSwipeRefresh() {
swipeRefreshLayout.setRefreshing(false);
}
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.activity_main_actions, menu);
	 
	        return super.onCreateOptionsMenu(menu);
	    }
	 Dialog tDialog2;
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        switch (item.getItemId()) {
	 
	            case R.id.Home:
	            {
	            	final String termoDeBusca = getIntent().getStringExtra("TERMO_DE_BUSCA");
	        		final String TWITTER_CONSUMER_KEY = "uMjwOFFZCQTvBXfyNG4i7cY1H";
	        		final String TWITTER_CONSUMER_SECRET = "5VNIGw5woT7agF4WerWPoQxV3JWVz7vAQjEG9J0JeGocLRgpWV";
	        		final String twit_access_token = getIntent().getStringExtra("Access_token");
	        		final String twit_access_token_secret = getIntent().getStringExtra("scret_token");
	            	new BuscaTweetsTask().execute(TWITTER_CONSUMER_KEY,TWITTER_CONSUMER_SECRET,twit_access_token,twit_access_token_secret);
	                return true;
	            }
	 
	            case R.id.action_search:
	            	final String access_token = getIntent().getStringExtra("Access_token");
	            	final String access_token_secret = getIntent().getStringExtra("scret_token");
	                     Intent i = new Intent(show_home_timeline.this, UserTimeLine.class);
	                     i.putExtra("Access_token", access_token);
		             		i.putExtra("scret_token", access_token_secret);
	             		startActivity(i);
	       
	                return true;
	 
	            case R.id.compose:
	            	final String TWITTER_CONSUMER_KEY1 = "uMjwOFFZCQTvBXfyNG4i7cY1H";
	    			final String TWITTER_CONSUMER_SECRET1 = "5VNIGw5woT7agF4WerWPoQxV3JWVz7vAQjEG9J0JeGocLRgpWV";
	    			final String twit_access_token1 = getIntent().getStringExtra("Access_token");
	    			final String twit_access_token_secret1 = getIntent().getStringExtra("scret_token");
	            	 tDialog = new Dialog(show_home_timeline.this);
	   		      tDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	   		      tDialog.setContentView(R.layout.tweet_dialog);
	   		       tweet_text = (EditText)tDialog.findViewById(R.id.tweet_text);
	   		      ImageView post_tweet = (ImageView)tDialog.findViewById(R.id.post_tweet);
	   		      post_tweet.setOnClickListener(new View.OnClickListener() {
	   		        @Override
	   		        public void onClick(View v) {
	   		          // TODO Auto-generated method stub
	   		        new PostTweet().execute(TWITTER_CONSUMER_KEY1,TWITTER_CONSUMER_SECRET1,twit_access_token1,twit_access_token_secret1);
	   		        }
	   		      });
	   		      tDialog.show();
	                return true;
	 
	            case R.id.signout:
	            	 Intent j = new Intent(show_home_timeline.this, MainActivity.class);
	                    j.putExtra("Access_token", "");
	            		j.putExtra("scret_token", "");
	            		startActivity(j);
	            return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
	 EditText tweet_text ;
	 Dialog tDialog;
	 private class MakeTweet implements OnClickListener {
			final String TWITTER_CONSUMER_KEY1 = "uMjwOFFZCQTvBXfyNG4i7cY1H";
			final String TWITTER_CONSUMER_SECRET1 = "5VNIGw5woT7agF4WerWPoQxV3JWVz7vAQjEG9J0JeGocLRgpWV";
			final String twit_access_token1 = getIntent().getStringExtra("Access_token");
			final String twit_access_token_secret1 = getIntent().getStringExtra("scret_token");
		    @Override
		    public void onClick(View v) {
		      // TODO Auto-generated method stub
		    	
		      tDialog = new Dialog(show_home_timeline.this);
		      tDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		      tDialog.setContentView(R.layout.tweet_dialog);
		       tweet_text = (EditText)tDialog.findViewById(R.id.tweet_text);
		      ImageView post_tweet = (ImageView)tDialog.findViewById(R.id.post_tweet);
		      post_tweet.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		          // TODO Auto-generated method stub
		        new PostTweet().execute(TWITTER_CONSUMER_KEY1,TWITTER_CONSUMER_SECRET1,twit_access_token1,twit_access_token_secret1);
		        }
		      });
		      tDialog.show();
		    }}
	 
	 
	 String tweetText;
	 ProgressDialog progress;
	  private class PostTweet extends AsyncTask<String, String, String> {
	        @Override
	            protected void onPreExecute() {
	                super.onPreExecute();
	                progress = new ProgressDialog(show_home_timeline.this);
	                progress.setMessage("Posting tweet ...");
	                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	                progress.setIndeterminate(true);
	                tweetText = tweet_text.getText().toString();
	                progress.show();
	        }
	           protected String doInBackground(String... param) {
	        	String TWITTER_CONSUMER_KEY = param[0];
	   		    String TWITTER_CONSUMER_SECRET = param[1];
	   		    String twit_access_token = param[2];
	   		    String twit_access_token_secret = param[3];

  				ConfigurationBuilder builder = new ConfigurationBuilder();
	   				 builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
	   				    builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
	   				    builder.setOAuthAccessToken(twit_access_token);
	   				    builder.setOAuthAccessTokenSecret(twit_access_token_secret);
	            
	              AccessToken accessToken = new AccessToken(twit_access_token, twit_access_token_secret);
	              Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
	              try {
	          twitter4j.Status response = twitter.updateStatus(tweetText +" @AnkurTestApp ");
	          return response.toString();
	        } catch (TwitterException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	        }
	           return null;
	           }
	           protected void onPostExecute(String res) {
	             if(res != null){
	               progress.dismiss();
	               Toast.makeText(show_home_timeline.this, "Tweet Sucessfully Posted", Toast.LENGTH_SHORT).show();
	               tDialog.dismiss();
	             }else{
	               progress.dismiss();
	                   Toast.makeText(show_home_timeline.this, "Error while tweeting !", Toast.LENGTH_SHORT).show();
	                   tDialog.dismiss();
	             }
	           }
	       }
	 
	 public void showToast(String msg){
	        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	    }
	
	private void popularListView(ArrayList<Tweet> tweets){
		TweetAdapter  tweetAdapter = new TweetAdapter(this, R.layout.tweet_adapter, tweets);
		listView.setAdapter(tweetAdapter);
	}

	
	private class BuscaTweetsTask extends AsyncTask<String, Void, ArrayList<Tweet>>{

		private ProgressDialog progressDialog;
		private static final String URL_BASE = "https://api.twitter.com";
		private static final String URL_BUSCA = "https://api.twitter.com/1.1/statuses/home_timeline.json?count=10";
		private static final String URL_AUTH = URL_BASE + "/oauth2/token";

		private static final String CONSUMER_KEY = "uMjwOFFZCQTvBXfyNG4i7cY1H";
		private static final String CONSUMER_SECRET = "5VNIGw5woT7agF4WerWPoQxV3JWVz7vAQjEG9J0JeGocLRgpWV";

		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			swipeRefreshLayout.setRefreshing(true);
			
		}

		
		@Override
		protected ArrayList<Tweet> doInBackground(String... param) {

			ArrayList<Tweet> tweets = new ArrayList<Tweet>();
			HttpURLConnection conexao = null;
			BufferedReader br = null;
			String TWITTER_CONSUMER_KEY = param[0];
		    String TWITTER_CONSUMER_SECRET = param[1];
		    String twit_access_token = param[2];
		    String twit_access_token_secret = param[3];

			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				 builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
				    builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
				    builder.setOAuthAccessToken(twit_access_token);
				    builder.setOAuthAccessTokenSecret(twit_access_token_secret);
				    builder.setJSONStoreEnabled(true);
				    builder.setIncludeEntitiesEnabled(true);
				    builder.setIncludeMyRetweetEnabled(true);
				    AccessToken accessToken = new AccessToken(twit_access_token, twit_access_token_secret);
				    Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
				    

				
				
				String strInitialDataSet = DataObjectFactory.getRawJSON(twitter.getHomeTimeline());
				JSONArray jsonArray = new JSONArray(strInitialDataSet);
				JSONObject jsonObject;
				Log.d("Response", jsonArray+"");
				
				for (int i = 0; i < jsonArray.length(); i++) {
					
					jsonObject = (JSONObject) jsonArray.get(i);
					Tweet tweet = new Tweet();
					tweet.setNome(jsonObject.getJSONObject("user").getString("name"));
					tweet.setUsuario(jsonObject.getJSONObject("user").getString("screen_name"));
					tweet.setUrlImagemPerfil(jsonObject.getJSONObject("user").getString("profile_image_url"));
					tweet.setMensagem(jsonObject.getString("text"));
					tweet.setData(jsonObject.getString("created_at"));
					tweet.setFav(jsonObject.getString("favorite_count"));
					tweet.setRt(jsonObject.getString("retweet_count"));
					tweets.add(i, tweet);
				}
				
			} catch (Exception e) {
				Log.e("Erro GET: ", Log.getStackTraceString(e));        

			}finally {
				if(conexao != null){
					conexao.disconnect();
				}
			}
			return tweets;
		}

		
		@Override
		protected void onPostExecute(ArrayList<Tweet> tweets){
		
			swipeRefreshLayout.setRefreshing(false);
			if (tweets.isEmpty()) {
				Toast.makeText(show_home_timeline.this, "Unable to retrieve tweets! ",
						Toast.LENGTH_SHORT).show();
			} else {
				popularListView(tweets);
				Toast.makeText(show_home_timeline.this, "Loaded tweets!",
						Toast.LENGTH_SHORT).show();
			}
		}


	}

	


}
