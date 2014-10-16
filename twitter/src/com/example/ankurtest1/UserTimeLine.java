package com.example.ankurtest1;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;



import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
public class UserTimeLine extends Activity {
private ListView listView;
Dialog tDialog;
EditText ScreenName;
EditText tweet_text;
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.show_home_time);
final ActionBar actionBar = getActionBar();
actionBar.setDisplayShowHomeEnabled(true);
actionBar.setDisplayShowTitleEnabled(false);
actionBar.setDisplayUseLogoEnabled(true);
listView = (ListView) findViewById(R.id.lista_tweets);
tDialog = new Dialog(UserTimeLine.this);
tDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
tDialog.setContentView(R.layout.timelinedailog);
ScreenName = (EditText)tDialog.findViewById(R.id.screen_text);
ImageView Find_user = (ImageView)tDialog.findViewById(R.id.finduser);
Find_user.setOnClickListener(new View.OnClickListener() {

  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
  	String Screen_Name;
  	Screen_Name = ScreenName.getText().toString();
  	new getTimeL().execute(Screen_Name);
  	  tDialog.dismiss();
  }
});
tDialog.show();
}
@Override
public void onBackPressed() {
    // TODO Auto-generated method stub
	Intent intent = new Intent( UserTimeLine.this,MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("EXIT", true);
    startActivity(intent);
  
    
}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.activity_main_actions, menu);

    return super.onCreateOptionsMenu(menu);
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {

        case R.id.Home:
        {
        	
    		final String access_token = getIntent().getStringExtra("Access_token");
    		final String token_secret = getIntent().getStringExtra("scret_token");
    		 Intent i = new Intent(UserTimeLine.this, show_home_timeline.class);
    		 i.putExtra("Access_token", access_token);
     		i.putExtra("scret_token", token_secret);
     		startActivity(i);
            return true;
        }

        case R.id.action_search:
        	tDialog = new Dialog(UserTimeLine.this);
		      tDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		      tDialog.setContentView(R.layout.timelinedailog);
		      ScreenName = (EditText)tDialog.findViewById(R.id.screen_text);
		      ImageView Find_user = (ImageView)tDialog.findViewById(R.id.finduser);
		      Find_user.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		          // TODO Auto-generated method stub
		        	String Screen_Name;
		        	Screen_Name = ScreenName.getText().toString();
		        	new getTimeL().execute(Screen_Name);
		        	  tDialog.dismiss();
		        }
		      });
		      tDialog.show();
            return true;

        case R.id.compose:
        	final String TWITTER_CONSUMER_KEY1 = "uMjwOFFZCQTvBXfyNG4i7cY1H";
			final String TWITTER_CONSUMER_SECRET1 = "5VNIGw5woT7agF4WerWPoQxV3JWVz7vAQjEG9J0JeGocLRgpWV";
			final String twit_access_token1 = getIntent().getStringExtra("Access_token");
			final String twit_access_token_secret1 = getIntent().getStringExtra("scret_token");
        	 tDialog = new Dialog(UserTimeLine.this);
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
       	 Intent j = new Intent(UserTimeLine.this, MainActivity.class);
               j.putExtra("Access_token", "");
       		j.putExtra("scret_token", "");
       		startActivity(j);
       return true;

        default:
            return super.onOptionsItemSelected(item);
    }
}
String tweetText;
ProgressDialog progress;
private class PostTweet extends AsyncTask<String, String, String> {
      @Override
          protected void onPreExecute() {
              super.onPreExecute();
              progress = new ProgressDialog(UserTimeLine.this);
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
        twitter4j.Status response = twitter.updateStatus(tweetText +" @ankurTestApp ");
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
             Toast.makeText(UserTimeLine.this, "Tweet Sucessfully Posted", Toast.LENGTH_SHORT).show();
             tDialog.dismiss();
           }else{
             progress.dismiss();
                 Toast.makeText(UserTimeLine.this, "Error while tweeting !", Toast.LENGTH_SHORT).show();
                 tDialog.dismiss();
           }
         }
     }




private void popularListView(ArrayList<Tweet> tweets){
TweetAdapter tweetAdapter = new TweetAdapter(this, R.layout.tweet_adapter, tweets);
listView.setAdapter(tweetAdapter);
}

private class getTimeL extends AsyncTask<String, Void, ArrayList<Tweet>>{
private ProgressDialog progressDialog;
private static final String URL_BASE = "https://api.twitter.com";
private static final String URL_BUSCA = URL_BASE + "/1.1/statuses/user_timeline.json?screen_name=";
private static final String URL_AUTH = URL_BASE + "/oauth2/token";

private static final String CONSUMER_KEY = "uMjwOFFZCQTvBXfyNG4i7cY1H";
private static final String CONSUMER_SECRET = "5VNIGw5woT7agF4WerWPoQxV3JWVz7vAQjEG9J0JeGocLRgpWV";



private String autenticarApp(){
HttpURLConnection conexao = null;
OutputStream os = null;
BufferedReader br = null;
StringBuilder resposta = null;
try {
URL url = new URL(URL_AUTH);
conexao = (HttpURLConnection) url.openConnection();
conexao.setRequestMethod("POST");
conexao.setDoOutput(true);
conexao.setDoInput(true);
String credenciaisAcesso = CONSUMER_KEY + ":" + CONSUMER_SECRET;
String autorizacao = "Basic " + Base64.encodeToString(credenciaisAcesso.getBytes(), Base64.NO_WRAP);
String parametro = "grant_type=client_credentials";
conexao.addRequestProperty("Authorization", autorizacao);
conexao.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
conexao.connect();
os = conexao.getOutputStream();
os.write(parametro.getBytes());
os.flush();
os.close();
br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
String linha;
resposta = new StringBuilder();
while ((linha = br.readLine()) != null){
resposta.append(linha);	
}
Log.d("Código resposta POST", String.valueOf(conexao.getResponseCode()));
Log.d("Resposta JSON - token de acesso", resposta.toString());
} catch (Exception e) {
Log.e("Erro POST", Log.getStackTraceString(e));
}finally{
if (conexao != null) {
conexao.disconnect();
}
}
return resposta.toString();
}

@Override
protected void onPreExecute(){
super.onPreExecute();
progressDialog = new ProgressDialog(UserTimeLine.this);
progressDialog.setMessage("Loading User Timeline...");
progressDialog.show();
}

@Override
protected ArrayList<Tweet> doInBackground(String... param) {
String screenName = param[0];
ArrayList<Tweet> tweets = new ArrayList<Tweet>();
HttpURLConnection conexao = null;
BufferedReader br = null;
try {
URL url = new URL(URL_BUSCA + screenName);
conexao = (HttpURLConnection) url.openConnection();
conexao.setRequestMethod("GET");
String jsonString = autenticarApp();
JSONObject jsonAcesso = new JSONObject(jsonString);
String tokenPortador = jsonAcesso.getString("token_type") + " " +
jsonAcesso.getString("access_token");
conexao.setRequestProperty("Authorization", tokenPortador);
conexao.setRequestProperty("Content-Type", "application/json");
conexao.connect();
br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
String linha;
StringBuilder resposta = new StringBuilder();
while ((linha = br.readLine()) != null){
resposta.append(linha);	
}
Log.d("Código resposta GET", String.valueOf(conexao.getResponseCode()));
Log.d("Resposta JSON", resposta.toString());
JSONArray jsonArray = new JSONArray(resposta.toString());
JSONObject jsonObject;
for (int i = 0; i < jsonArray.length(); i++) {
jsonObject = (JSONObject) jsonArray.get(i);
Tweet tweet = new Tweet();
tweet.setNome(jsonObject.getJSONObject("user").getString("name"));
tweet.setUsuario(jsonObject.getJSONObject("user").getString("screen_name"));
tweet.setUrlImagemPerfil(jsonObject.getJSONObject("user").getString("profile_image_url"));
tweet.setMensagem(jsonObject.getString("text"));
tweet.setData(jsonObject.getString("created_at"));
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
progressDialog.dismiss();
if (tweets.isEmpty()) {
	AlertDialog.Builder builder = new AlertDialog.Builder(UserTimeLine.this);
    builder.setTitle("ScreenName Error").setMessage("    ScreenName not exist").setCancelable(false).setNegativeButton("Close",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    });
    AlertDialog alert = builder.create();
    alert.show();



} else {
popularListView(tweets);
Toast.makeText(UserTimeLine.this, "Loaded tweets!",
Toast.LENGTH_SHORT).show();
}
}
}
}