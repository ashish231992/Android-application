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

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import android.app.ProgressDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class timeline extends Activity {
	TextView textView1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timel);
		textView1 = (TextView) findViewById(R.id.textView1);
		final String token = getIntent().getStringExtra("token");
		final String scret_token = getIntent().getStringExtra("scret_token");
		new printTimeline().execute(token,scret_token);
	}
	
	class printTimeline extends AsyncTask<String,String , String> {
		private ProgressDialog pDialog ;

		@Override
		protected void onPreExecute() {
	        super.onPreExecute();
	       
	        pDialog.setMessage("Updating to twitter...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();
	    }
		
		
		protected String doInBackground(String... args) {
			String token=args[0];
			String scret_token=args[1];
			String tweet="yes";
			try {
	            ConfigurationBuilder builder = new ConfigurationBuilder();
	            builder.setDebugEnabled(true);
	            builder.setOAuthConsumerKey("uMjwOFFZCQTvBXfyNG4i7cY1H");
	            builder.setOAuthConsumerSecret("5VNIGw5woT7agF4WerWPoQxV3JWVz7vAQjEG9J0JeGocLRgpWV");
	            builder.setOAuthAccessToken(token);
	            builder.setOAuthAccessTokenSecret(scret_token);
	            Twitter twitter = TwitterFactory.getSingleton();
	            List<twitter4j.Status> statuses = twitter.getHomeTimeline();
	            for (twitter4j.Status status : statuses) {
	      
	                
	            }
			                 
	}
			catch (TwitterException e) {
	            Log.d("Twitter Update Error", e.getMessage());
	        }
	        return tweet;

}
		
		protected void onPostExecute(String tweets){
			pDialog.dismiss();
			textView1.setText(tweets+"");
			
		}
	}
}