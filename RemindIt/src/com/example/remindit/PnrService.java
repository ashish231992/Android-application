package com.example.remindit;

import java.util.ArrayList;



import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.widget.TableRow;
import android.widget.Toast;

public class PnrService extends Service{
	private static final String TAG = "MyService";
	PnrDatabase pnrdatabase;
	NotificationManager nm;
	 int uniqueid=134568;
	ArrayList<ArrayList<Object>> data;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		//Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		try{
			boolean mobileNwInfo =
					false;
					ConnectivityManager conxMgr =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
					try { mobileNwInfo=conxMgr.getActiveNetworkInfo().isConnected(); }
					catch (NullPointerException e) { 
						mobileNwInfo =false; }
					if ( mobileNwInfo == false )
					{
						//Toast.makeText(this, "Please Check your internet connection and restart the application", Toast.LENGTH_LONG).show();
					}
		//Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		Intent i = new Intent(this, this.getClass());
	    PendingIntent pendingIntent =
	        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    long currentTimeMillis = System.currentTimeMillis();
	    long nextUpdateTimeMillis = currentTimeMillis + 1* DateUtils.MINUTE_IN_MILLIS;
	    Time nextUpdateTime = new Time();
	    nextUpdateTime.set(nextUpdateTimeMillis);

	   
	    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC, nextUpdateTimeMillis, pendingIntent);
	    
	   
		PnrDatabase pnrdb=new PnrDatabase(this);
		ConfDatabase cnfdb=new ConfDatabase(this);
		cnfdb.open();
		
		pnrdb.open();
		Log.d("pnrdtaa"," reached open");
		data = pnrdb.getAllPNR();
		Log.d("pnrdtaa"," reached 2 open");
		String pnr,pnr1;
		int i1=0;
		Log.d("notification","reached");
		try{
			Thread menuThread=new Thread(){
				public void run(){
					try{
						 UrlGenerator ug;
							ug=new UrlGenerator(PnrService.this);
							ug.giveURL();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						
					}
				}
			};
			menuThread.start();

		    
		
			}
			catch(Exception e)
			{
				
				//Toast.makeText(this, "Something Went Wrong, Restart the applicaton :-(", Toast.LENGTH_LONG).show();
			}

		
		while (i1 < data.size()) {
			
			pnr = data.get(i1).toString();
			pnr1 = pnr.substring(pnr.indexOf("[") + 1,
					pnr.indexOf("[") + 11);
			Log.d("notification",pnr1);
			i1++;
			
			String flag=cnfdb.getflag(pnr1);
			
		String str =pnrdb.getStatus(pnr1);
		//Toast.makeText(this, "Sda"+str+"sda", Toast.LENGTH_LONG).show();
		

		
		    
               		if(cnfdb.getchart(pnr1).compareTo("prepared")==0)
               		{
               			nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        				nm.cancel(uniqueid);
        				String title="Remind It!";
        				//Notification n=new Notification(R.drawable.notify,body,System.currentTimeMillis());
        				//n.setLatestEventInfo(this, title, body, pi);
        				//n.defaults=Notification.DEFAULT_ALL;
        				String body="Your pnr no : "+pnr1+" has been confirmed";
        				String msg=pnrdb.getStatus(pnr1);
        				Log.d("status", msg);
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
        				//Toast.makeText(this, "Sda"+str+"sda", Toast.LENGTH_LONG).show();
        				//Bundle basket=new Bundle();
        				//basket.putString("key", pnr1);
        				//String uid=""+uniqueid;
        				//basket.putInt("key1",uniqueid);
        				
        				
        				Intent intent1=new Intent(this,AutoPnr.class);
        				//intent1.putExtras(basket);
        				PendingIntent pi=PendingIntent.getActivity(this, 0, intent1, 0);
        				builder.setContentIntent(pi);  
        				nm.notify(uniqueid,builder.build());
        				uniqueid++;
        				pnrdb.deleteEntry(pnr1);
        				cnfdb.deleteEntry(pnr1);
        				
               		}
		
			if(pnrdb.getWait(pnr1)==0 && str.compareTo("abcd")!=0 && flag.compareTo("true")!=0)
			{
				nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
				nm.cancel(uniqueid);
								
				String body="Your pnr no : "+pnr1+" has been confirmed";
				String title="Remind It!";
				//Notification n=new Notification(R.drawable.notify,body,System.currentTimeMillis());
				//n.setLatestEventInfo(this, title, body, pi);
				//n.defaults=Notification.DEFAULT_ALL;
				
				String msg=pnrdb.getStatus(pnr1);
				Log.d("status", msg);
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
				//Bundle basket=new Bundle();
				//basket.putString("key", pnr1);
				//String uid=""+uniqueid;
				//basket.putInt("key1",uniqueid);
				
				
				Intent intent1=new Intent(this,AutoPnr.class);
				//intent1.putExtras(basket);
				PendingIntent pi=PendingIntent.getActivity(this, 0, intent1, 0);
				 
				builder.setContentIntent(pi);  
				
				nm.notify(uniqueid,builder.build());
				uniqueid++;
				cnfdb.updateRow(pnr1, "true");
				
			}

			 
			
			
		}
		 pnrdb.close();
		 cnfdb.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
			    
	
	}

	@Override
	public void onDestroy() {
		//Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
	}

}
