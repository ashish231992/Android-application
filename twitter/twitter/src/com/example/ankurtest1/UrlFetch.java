package com.example.ankurtest1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class UrlFetch {
	String hashValue="";
	String dta="";
	public String fetchUrl(){
		StringBuilder sb = new StringBuilder("http://api.openweathermap.org/data/2.5/weather?q=London");
		Log.d("downloading url", "url");
		PlacesTask placeTask=new PlacesTask();
		placeTask.execute(sb.toString());
		return dta;
       
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
	            Log.d("downloading url", data);

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
	        	
	        	dta=data;
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        	Log.d("Helllo My Response",data );
	        }

	    }
}
