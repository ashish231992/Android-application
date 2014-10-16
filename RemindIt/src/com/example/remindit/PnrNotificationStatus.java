package com.example.remindit;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PnrNotificationStatus extends Activity{

	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		try{
		
			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pnrnotificationstatus);
	
		Bundle b = this.getIntent().getExtras();
		String i = b.getString("key");
		//int uid=Integer.parseInt(b.getString("key1"));
		Log.d("PNR", i);
		tv=(TextView)findViewById(R.id.tvpnr);
		//String ns = Context.NOTIFICATION_SERVICE;
	     //NotificationManager nMgr = (NotificationManager) this.getSystemService(ns);
	    //nMgr.cancel(uid);
		PnrDatabase pnrdatabase;
		pnrdatabase = new PnrDatabase(this);
		pnrdatabase.open();
		String status =pnrdatabase.getStatus(i);
		tv.setText(i+ "\n"+status);		
		
		pnrdatabase.close();
		}
		catch(Exception e){
			
		}
		
		
		
	}
	

}
