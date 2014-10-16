package com.example.remindit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

public class ConnectionControl {

	Context ourContext;

	/**************** CONSTRUCTOR *****************************/

	public ConnectionControl(Context c) {
	
		this.ourContext = c;
		StrictMode.ThreadPolicy policy = 
		        new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	//	Log.d("ConnectionControl : ", "CTOR CALLED ");
	}
	/**************** controlMethod FUNCTION *****************************/
	
	public void controlMethod(String getUrl, String pnr) throws Exception {
	Log.d("ConnectionControl : ", "controlMethod CALLED ");
		BufferedReader in = null;
		//String data = null;
		String json = null;
		JSONObject jObj = null;
		TakeJsonObject jsonObj = new TakeJsonObject(ourContext);

		try {
			// calling http client
			HttpClient client = new DefaultHttpClient();

			// Set up URI
			URI website = new URI(getUrl);
			// get Http Request
			HttpGet request = new HttpGet();
			request.setURI(website);
			// response from The given website
			HttpResponse response = client.execute(request);
			/*************** Read Data ********/
			
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String l = "";
			String nl = System.getProperty("line.separator");
			while ((l = in.readLine()) != null) {
				sb.append(l + nl);
			}
			in.close();
			json = sb.toString();
			//jObj = new JSONObject(json);
			
			//jsonObj.getJsonObject(jObj, pnr);

		}catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        } 
			try {
				jObj = new JSONObject(json);
					

					jsonObj.getJsonObject(jObj, pnr);

				} catch (Exception e) {
					e.printStackTrace();
				}
		
		
	}
}
