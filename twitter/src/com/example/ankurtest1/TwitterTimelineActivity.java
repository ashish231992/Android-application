package com.example.ankurtest1;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
public class TwitterTimelineActivity extends Fragment {
    TextView prof_name;
    
    SharedPreferences pref;
   ProgressDialog progress;
   ListView mainlist;
   String hashValue="";

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.timeline, container, false);
        prof_name = (TextView)view.findViewById(R.id.prof_name);
        pref = getActivity().getPreferences(0);
        StringBuilder sb = new StringBuilder("http://gadighar.in/usersubmit.php?clat=2&clong=3");
        mainlist = (ListView)view.findViewById(R.id.mainlist);
        mainlist.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,new ArrayList<String>()));
        PlacesTask placeTask=new PlacesTask();
        placeTask.execute(sb.toString());
        return view;
    }
    private class SignOut implements OnClickListener {
    @Override
    public void onClick(View arg0) {
      // TODO Auto-generated method stub
      SharedPreferences.Editor edit = pref.edit();
            edit.putString("ACCESS_TOKEN", "");
            edit.putString("ACCESS_TOKEN_SECRET", "");
            edit.commit();
            Fragment login = new LoginFragment();
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, login);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
    }
    }
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

         
            urlConnection.connect();

         
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    /** A class, to download Google Places */
    private class PlacesTask extends AsyncTask<String, Integer, String>{

        String data = null;

        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result){
        	String data=result;
        	try {
    			JSONObject jObj=new JSONObject(data);
    			hashValue=jObj.getString("hash");
    			Log.d("Helllo my hashvalue",hashValue );
    			
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
        	Log.d("Helllo My Response",data );
         
        }

    }
       
}


	







