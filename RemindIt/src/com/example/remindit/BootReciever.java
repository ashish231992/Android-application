package com.example.remindit;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class BootReciever extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		try{
			
			
	         Log.d("BootReciever", "Hi, Boot reciver was catch!");
	         
	         context.startService(new Intent(context, PnrService.class));
	        
	         context.startService(new Intent(context, LocationService.class));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	       }

	
}
