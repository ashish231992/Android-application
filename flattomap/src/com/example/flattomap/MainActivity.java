package com.example.flattomap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	private GoogleMap gmap;
	private LocationClient LocClient;
	private TextView mMessageView;
	double latitude;
	double longitude;
	Location current_location;
	MarkerOptions markerOptions;
	HashMap<String, String> gooPlace;

//Creating request For Google Map
	private static final LocationRequest LOCATION_REQ = LocationRequest.create()
			.setInterval(50000)        
			.setFastestInterval(5000)    
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			mMessageView = (TextView) findViewById(R.id.message_text);
			
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		LocClient.connect();
		
		
	}

	@Override
	public void onPause() {
		super.onPause();
		if (LocClient != null) {
			LocClient.disconnect();
		}
	}

	private void setUpMapIfNeeded() {
		if (gmap == null) {
			
			gmap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			if (gmap != null) {
				gmap.setMyLocationEnabled(true);
			}
		}
		
	}

	private void setUpLocationClientIfNeeded() {
			if (LocClient == null) {
				LocClient = new LocationClient(
						getApplicationContext(),
						this,  
						this); 
			}
	}
	
	private void build_String(){
		
		
		//if(latitude!=0.0 || longitude!=0.0){
			//LatLng latLng = new LatLng(latitude, longitude);
			//gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			//gmap.animateCamera(CameraUpdateFactory.zoomTo(15));
		//Json Fetching String
			StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
			sb.append("location="+latitude+","+longitude);
			sb.append("&radius=5000");
			sb.append("&types="+"restaurant");
			sb.append("&sensor=true");
			sb.append("&key=AIzaSyDeUuH3bKhKMV0-vfryg8XsRNi7dK6pI3A");
			//Toast.makeText(this, "I am In Strng Builder", Toast.LENGTH_LONG).show();
			//Toast.makeText(this, sb,0).show();
        
			PlacesTask placesTask = new PlacesTask();

			placesTask.execute(sb.toString());
		//}
	}
	
	
	public String controlMethod(String getUrl) throws Exception {
		Log.d("ConnectionControl : ", "controlMethod CALLED ");
			BufferedReader in = null;
			//String data = null;
			String json = null;
			JSONObject jObj = null;
			//TakeJsonObject jsonObj = new TakeJsonObject(ourContext);

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
				//Toast.makeText(this, json, Toast.LENGTH_SHORT).show();

			}catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        } 
				
			return json;
			
		}
	
	 private class PlacesTask extends AsyncTask<String, Integer, String>{
		 
	        String data = null;
	 
	        // Invoked by execute() method of this object
	        @Override
	        protected String doInBackground(String... url) {
	            try{
	                data =controlMethod(url[0]);
	            }catch(Exception e){
	                Log.d("Background Task",e.toString());
	            }
	            return data;
	        }
	 
	        // Executed after the complete execution of doInBackground() method
	        @Override
	        protected void onPostExecute(String result){
	            ParserTask parserTask = new ParserTask();
	 
	            // Start parsing the Google places in JSON format
	            // Invokes the "doInBackground()" method of the class ParseTask
	            parserTask.execute(result);
	        }
	 
	    }
		
	
	 private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
		 
	        JSONObject jObject;
	 
	        // Invoked by execute() method of this object
	        @Override
	        protected List<HashMap<String,String>> doInBackground(String... jsonData) {
	 
	            List<HashMap<String, String>> places = null;
	            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
	 
	            try{
	                jObject = new JSONObject(jsonData[0]);
	 
	                /** Getting the parsed data as a List construct */
	                places = placeJsonParser.parse(jObject);
	 
	            }catch(Exception e){
	                Log.d("Exception",e.toString());
	            }
	            return places;
	        }
	 
	        // Executed after the complete execution of doInBackground() method
	        @Override
	        protected void onPostExecute(List<HashMap<String,String>> list){
	 
	            // Clears all the existing markers
	            gmap.clear();
	 
	            for(int i=0;i<list.size();i++){
	 
	              
	            	markerOptions = new MarkerOptions();
	 
	            	gooPlace = list.get(i);
	
	                double lat = Double.parseDouble(gooPlace.get("lat"));
	
	                double lng = Double.parseDouble(gooPlace.get("lng"));
	 
	                String name = gooPlace.get("place_name");
	 
	                String vicinity = gooPlace.get("vicinity");
	 
	                LatLng latLng = new LatLng(lat, lng);
	 
	                markerOptions.position(latLng);
	 
	                markerOptions.title(name + " : " + vicinity);
	 
	                gmap.addMarker(markerOptions);
	            }
	        }
	    }
		
	
	
	
	
		@Override
	public void onLocationChanged(Location location) {
			//TextView tvLocation = (TextView) findViewById(R.id.message_text);
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			//build_String();
			//LatLng latLng = new LatLng(latitude, longitude);
		//gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
     	//gmap.animateCamera(CameraUpdateFactory.zoomTo(15));
     	//tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude ); 
			mMessageView.setText("Longitude: " + longitude+"Latitude: "+latitude);
			//LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		    //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
		    //gmap.animateCamera(cameraUpdate);
     	
	}


	@Override
	public void onConnected(Bundle connectionHint) {
		//Toast.makeText(getApplicationContext(),"7", Toast.LENGTH_SHORT).show();
	    LocClient.requestLocationUpdates(
	        LOCATION_REQ,
	        this);  // LocationListener
	    Location Los=LocClient.getLastLocation();
	    latitude=Los.getLatitude();
	    longitude=Los.getLongitude();
	    LatLng latLng = new LatLng(latitude, longitude);
	    //Toast.makeText(getApplicationContext(),"Moving camera to : "+latitude +longitude, Toast.LENGTH_SHORT).show();
	    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
	    gmap.animateCamera(cameraUpdate);
	  	build_String();
		
	}

	@Override
	public void onDisconnected() {
		// Do nothing
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

}
