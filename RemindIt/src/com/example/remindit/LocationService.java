package com.example.remindit;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service implements LocationListener{
	private static final String TAG = "MyLocationService";
	NotificationManager nm;
	static final int uniqueid=134567;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		
		
		
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//Toast.makeText(this, "Congrats! MyLocationService Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Toast.makeText(this, "MyLocationService Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "MyLOcationService Started", Toast.LENGTH_LONG).show();
		try{Log.d(TAG, "onStart");
		boolean mobileNwInfo =false;
				ConnectivityManager conxMgr =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				try { mobileNwInfo =conxMgr.getActiveNetworkInfo().isConnected(); }
				catch (NullPointerException e) 
				{
					mobileNwInfo = false; }
				if ( mobileNwInfo == false )
				{
					//Toast.makeText(this, "Please Check your internet connection", Toast.LENGTH_LONG).show();
				}
		Intent i = new Intent(this, this.getClass());
	    PendingIntent pendingIntent =
	        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    long currentTimeMillis = System.currentTimeMillis();
	    
	    
	    long nextUpdateTimeMillis = currentTimeMillis + 1* DateUtils.MINUTE_IN_MILLIS;
	    Time nextUpdateTime = new Time();
	    nextUpdateTime.set(nextUpdateTimeMillis);

	   
	    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC, nextUpdateTimeMillis, pendingIntent);
		
	    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);;
	    Location loc = null;
	    Boolean isGPSEnabled,isNetworkEnabled;
	    isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
        	//Toast.makeText(this, "Please Turn On your GPS sensor ", Toast.LENGTH_LONG).show();
        } else {
            //this.canGetLocation = true;
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                       35000,
                        0, this);
                Log.d("Network", "Network Enabled");
                if (locationManager != null) {
                    loc= locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (loc != null) {
                       // latitude = location.getLatitude();
                       // longitude = location.getLongitude();
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
            //	Toast.makeText(this, "GPS enabled", Toast.LENGTH_LONG).show();
                if (loc == null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            3500,
                            0, this);
                    Log.d("GPS", "GPS Enabled");
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                          //  latitude = location.getLatitude();
                          //  longitude = location.getLongitude();
                        }
                    }
                }
            }
        }
		ArrayList<ArrayList<Object>> res=null;
		ArrayList<Object> row;
	    LocationDatabase locdb=new LocationDatabase(this);
		locdb.open();
		try{
			res=locdb.getAllRowsAsArrays();
		
		}
		catch(Exception e)
		{
			//Toast.makeText(this, "Error in database", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			
		}
		try{int j=0;
		//Toast.makeText(this, "database size"+res.size(), Toast.LENGTH_LONG).show();
		SharedPreferences getprefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		  int remindme=Integer.parseInt(getprefs.getString("list", "1"));
		  String remindermessage,locationname;	
		  //Toast.makeText(this, "distance="+remindme, Toast.LENGTH_LONG).show();
		while(j<res.size())
		{
			row=res.get(j);
			locationname=row.get(0).toString();
			String lat=row.get(1).toString();
			String lng=row.get(2).toString();
			remindermessage=row.get(3).toString();
			int id=Integer.parseInt(row.get(4).toString());
			Location dest=new Location("dest");
			dest.setLatitude(Double.parseDouble(lat));
			dest.setLongitude(Double.parseDouble(lng));
			//Toast.makeText(this, lat+"  "+lng, Toast.LENGTH_LONG).show();
			float distance=dest.distanceTo(loc);
			//pref
			
			
			

			if(distance<=remindme*1000)
			{
				//Toast.makeText(this, "Location reached"+distance+" meters apart", Toast.LENGTH_LONG).show();
				Intent intent1=new Intent(this,LocationReminderList.class);
				PendingIntent pi=PendingIntent.getActivity(this, 0, intent1, 0);
				
				String body="Location reached";
				String title="Remind it";
				nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
				
				//Notification n=new Notification(R.drawable.notify,body,System.currentTimeMillis());
				//n.setLatestEventInfo(this, title, body, pi);
				//n.defaults=Notification.DEFAULT_ALL;
			     
				String msg="\n"+locationname+" "+distance+" meters apart \n\n"+"REMINDER MESSAGE: \n"+remindermessage;
				
				NotificationCompat.Builder builder =
				        new NotificationCompat.Builder(this)
				        .setSmallIcon(R.drawable.notify)
				        .setContentTitle(title)
				        .setContentText(body)
				        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
				        /*
				         * Sets the big view "big text" style and supplies the
				         * text (the user's reminder message) that will be displayed
				         * in the detail area of the expanded notification.
				         * These calls are ignored by the support library for
				         * pre-4.1 devices.
				         */
				       .setStyle(new NotificationCompat.BigTextStyle()
				            .bigText(msg));
				            	
			
				
				 builder.setContentIntent(pi);  
				
				
				nm.notify(uniqueid,builder.build());
				
				
				
			
			}
			else
			{
				//Toast.makeText(this, "Location not reached "+distance+" meters apart", Toast.LENGTH_LONG).show();
			}
			
			j++;
		}
		
		
		locdb.close();
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			e.getCause();
			Log.d("error","found");
		}
		}
		catch(Exception e)
		{
			//Toast.makeText(this, "Something Went Wrong, Restart the applicaton :-(", Toast.LENGTH_LONG).show();
		}
		
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "Please check if your network provider and gps is enabled  ", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
