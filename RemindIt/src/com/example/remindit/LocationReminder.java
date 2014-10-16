package com.example.remindit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class LocationReminder extends Activity implements LocationListener ,OnClickListener,OnItemClickListener{
	Button bt,call;
	LatLng l=null;
	LatLng loc=null;
	TextView tvloc,tvresult,tvnote;
	EditText etloc,etnote;
	AutoCompleteTextView locationet;
String res;
int refNumber;
ArrayList<String> referenceList=null;

	private GoogleMap googlemap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try{
		String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager nMgr = (NotificationManager) this.getSystemService(ns);
	    nMgr.cancel(134567);
		
		
		if(isgoogleplay())
		{
			//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

			//StrictMode.setThreadPolicy(policy); 
		setContentView(R.layout.locationreminder);
	
		setupmap();
	  locationet=(AutoCompleteTextView)findViewById(R.id.autoCompleteloc);
	  locationet.setOnKeyListener(new OnKeyListener() {
		
		  @Override
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        if (keyCode == KeyEvent.KEYCODE_ENTER) { 
		                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(locationet.getWindowToken(), 0);
		    return true;
		        }
		        return false;
		    }

			
	});
	  locationet.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
	  locationet.setOnItemClickListener(this);
	  bt=(Button)findViewById(R.id.btsearch);
	  call=(Button)findViewById(R.id.clearall);
	//	etloc=(EditText)findViewById(R.id.etlocation);
		////etloc.setOnKeyListener(new OnKeyListener() {
		//    @Override
		//    public boolean onKey(View v, int keyCode, KeyEvent event) {
		 //       if (keyCode == KeyEvent.KEYCODE_ENTER) { 
		 //               InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		 //   imm.hideSoftInputFromWindow(etloc.getWindowToken(), 0);
		 ////   return true;
		  //      }
		  //      return false;
		  //  }

			
		//});
		
		tvresult=(TextView)findViewById(R.id.tvresult);
		etnote=(EditText)findViewById(R.id.etnote);
		etnote.setOnKeyListener(new OnKeyListener() {
		    @Override
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        if (keyCode == KeyEvent.KEYCODE_ENTER) { 
		                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(etnote.getWindowToken(), 0);
		    return true;
		        }
		        return false;
		    }

			
		});
		tvloc=(TextView)findViewById(R.id.tvloc);
		tvnote=(TextView)findViewById(R.id.tvnote);
		
		
		bt.setOnClickListener(this);
		call.setOnClickListener(this);
		}
		}catch(Exception e){
			
			Toast.makeText(this, "Enter Valid Address", Toast.LENGTH_SHORT).show();
		}
	}
	private boolean isgoogleplay()
	{
		int status=GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(status==ConnectionResult.SUCCESS)
			return true;
		else
		{
			Toast.makeText(this, "GOOGLE PLAY NOT AVAILABLE", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	private void setupmap(){
		if(googlemap==null)
		{
			googlemap=((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
			
			if(googlemap!=null){
				googlemap.setMyLocationEnabled(true);
				LocationManager ls=(LocationManager)getSystemService(LOCATION_SERVICE);
				String provider=ls.getBestProvider(new Criteria() , true);
				if(provider==null)
				{
					onProviderDisabled(provider);
				}
				Location loc=ls.getLastKnownLocation(provider);
				if(loc!=null)
				{
					onLocationChanged(loc);
				}
				googlemap.setOnMapLongClickListener(onlongclickmapsetting());
			}
		}
	}
	private OnMapLongClickListener onlongclickmapsetting() {
		// TODO Auto-generated method stub
		
		
		
		return new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				// TODO Auto-generated method stub
				Log.d("long click",arg0.toString());
			}
		};
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btsearch:
			try{//String t="no text";
			//LatLng l=autocomplete(etloc.getText().toString());
				String par=locationet.getText().toString();
				par=par.replaceAll("\\s", "");
				Log.d("string",par.trim());
		   AsyncTask<String, Integer, LatLng> obj=new progresstask().execute(par.trim());
		   
		   
		   Toast.makeText(this, "Inserted in database", Toast.LENGTH_LONG).show();
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Toast.makeText(this, "Error found", Toast.LENGTH_LONG).show();
			}
			
				break;

		case R.id.clearall:
			locationet.setText("");
			etnote.setText("");
			break;
		default:
			break;
		}
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		LatLng latlng=new LatLng(location.getLatitude(), location.getLongitude());
		
		googlemap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		googlemap.animateCamera(CameraUpdateFactory.zoomTo(15));
	}
	private static final String LOG_TAG = "ExampleApp";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/details";

	private static final String OUT_JSON = "/json";
     
	
	
	
	
	
	
	public class progresstask extends AsyncTask<String,Integer,LatLng>
	{
		AutoCompleteTextView at;
		ArrayAdapter<String> adapter;
		EditText etnote2;
        TextView txtView;
        ProgressDialog dialog;
        private GoogleMap googlemap;
		 @Override
	        protected void onPreExecute() {
	            
	            // Set the variable txtView here, after setContentView on the dialog
	            // has been called! use dialog.findViewById().
	            txtView = (TextView)findViewById(R.id.tvresult);
	            googlemap=((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
	            at=(AutoCompleteTextView)findViewById(R.id.autoCompleteloc);
	            etnote2=(EditText)findViewById(R.id.etnote);
	            dialog = new ProgressDialog(LocationReminder.this);
	            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            
	           dialog.setTitle("Message");
	           dialog.setMessage("Loading Data........");
	           dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	            dialog.show();  
	            
	        }
		 protected void onPostExecute(LatLng result) {

			 googlemap.moveCamera(CameraUpdateFactory.newLatLng(result));
				googlemap.animateCamera(CameraUpdateFactory.zoomTo(10));
				  txtView.setText("Done loading data");
				  dialog.dismiss();
				  
				 
	         
	        }

		@Override
		protected LatLng doInBackground(String... input) {
			// TODO Auto-generated method stub
			
			String address = null;
			
			JSONObject result = null;
		    ArrayList<String> resultList = null;
		    BufferedReader in = null;
			String data = null;
			String json = null;
			JSONObject jObj = null;
			JSONObject location = null;
			JSONObject geometry = null;
		    HttpURLConnection conn = null;
		    StringBuilder jsonResults = new StringBuilder();
		    try {
				// calling http client
				HttpClient client = new DefaultHttpClient();

				// Set up URI
			    StringBuilder sb = new StringBuilder(PLACES_API_BASE  + OUT_JSON);
		       
		        sb.append("?reference="+reference(locationet.getText().toString())+"&sensor=true&key=AIzaSyCLH4qY3UlqC0cKIZed6YPGShvLVLMH_vM");
		      Log.d(reference(locationet.getText().toString()),"reference");
		      

		       
				URI website = new URI(sb.toString());
				// get Http Request
				HttpGet request = new HttpGet();
				request.setURI(website);
				// response from The given website
				HttpResponse response = client.execute(request);
				/*************** Read Data ********/
				 
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb1 = new StringBuffer("");
				String l = "";
				String nl = System.getProperty("line.separator");
				while ((l = in.readLine()) != null) {
					sb1.append(l + nl);
				}
				in.close();
				data = sb1.toString();
				Log.d("url",sb1.toString());
				jObj = new JSONObject(data);
		 result = jObj.getJSONObject("result");
		
		 JSONObject item=result.getJSONObject("geometry");
		 
		 location=item.getJSONObject("location"); 
		 
		 
		 
		 
		
				loc=new LatLng(Double.parseDouble(location.getString("lat")), Double.parseDouble(location.getString("lng")));
				String lat=  Double.toString(loc.latitude);
				String lng=Double.toString(loc.longitude);
				LocationDatabase locdb=new LocationDatabase(LocationReminder.this);
				locdb.open();
				Log.d("entry",at.getText().toString()+etnote2.getText().toString()+lat+lng);
				locdb.createEntry(at.getText().toString(),etnote2.getText().toString(),lat, lng);
				
				 
				   locdb.close();
				   googlemap.moveCamera(CameraUpdateFactory.newLatLng(loc));
				googlemap.animateCamera(CameraUpdateFactory.zoomTo(10));
				
				
				
				//Log.d("data"+loc.latitude,data);
	             
				//address=address+location.getString("lat")+location.getString("lng");
		    

		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    }
		    
		    
				
			return loc;
		}
		 
	}
	
	



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
     private static final String LOG_TAG2 = "ExampleApp";
    
	private static final String PLACES_API_BASE2 = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON2 = "/json";

	private static final String API_KEY = "AIzaSyCLH4qY3UlqC0cKIZed6YPGShvLVLMH_vM";

	private ArrayList<String> autocomplete(String input) {
	    ArrayList<String> resultList = null;
	    
	   
	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE2 + TYPE_AUTOCOMPLETE + OUT_JSON2);
	        sb.append("?sensor=false&key=" + API_KEY);
	        sb.append("&components=country:in");
	        sb.append("&input=" + URLEncoder.encode(input, "utf8"));
	        
	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());
	        
	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e(LOG_TAG, "Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
	        
	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	          
	           
	        }
	    } catch (JSONException e) {
	        Log.e(LOG_TAG, "Cannot process JSON results", e);
	    }
	    Log.d(LOG_TAG,""+ resultList.size());
	    return resultList;
	}
	private String reference(String input) {
	   String resultList = null;
	    
	   
	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE2 + TYPE_AUTOCOMPLETE + OUT_JSON2);
	        sb.append("?sensor=false&key=" + API_KEY);
	        sb.append("&components=country:in");
	        sb.append("&input=" + URLEncoder.encode(input, "utf8"));
	        
	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());
	        
	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (Exception e) {
	        Log.e(LOG_TAG, "Error processing Places API URL", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
	        
	        // Extract the Place descriptions from the results
	       
	     
	            resultList=predsJsonArray.getJSONObject(0).getString("reference");
	       
	    } catch (Exception e) {
	        Log.e(LOG_TAG, "Cannot process JSON results", e);
	    }
	  
	    return resultList;
	}
	
	
	
	
	//class
	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	    private ArrayList<String> resultList;
	    
	    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
	        super(context, textViewResourceId);
	    }
	    
	    @Override
	    public int getCount() {
	        return resultList.size();
	    }

	    @Override
	    public String getItem(int index) {
	        return resultList.get(index);
	    }

	    @Override
	    public Filter getFilter() {
	        Filter filter = new Filter() {
	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	                FilterResults filterResults = new FilterResults();
	                if (constraint != null) {
	                    // Retrieve the autocomplete results.
	                    resultList = autocomplete(constraint.toString());
	                    
	                    // Assign the data to the FilterResults
	                    filterResults.values = resultList;
	                    filterResults.count = resultList.size();
	                }
	                return filterResults;
	            }

	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	                if (results != null && results.count > 0) {
	                    notifyDataSetChanged();
	                }
	                else {
	                    notifyDataSetInvalidated();
	                }
	            }};
	        return filter;
	    }
	}


	

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		// TODO Auto-generated method stub
		 String str = (String) adapterView.getItemAtPosition(position);
		
		 
	      
		 Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowup=getMenuInflater();
		blowup.inflate(R.menu.coolmenu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.aboutus:
			Intent i=new Intent("com.example.remindit.ABOUT");
			startActivity(i);
			
			break;
		case R.id.preferences:
			Intent p=new Intent("com.example.remindit.PREFS");
			startActivity(p);
			break;
		case R.id.exit:
			finish();
			break;

		default:
			break;
		}
		return false;
	}
	
	
	
	
	

}
