package com.example.remindit;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Statusbar extends Activity implements OnClickListener {

	NotificationManager nm;
	static final int uniqueid=134567;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.statusbar);
		Button stat=(Button)findViewById(R.id.bstatusbar);
		TextView status=(TextView)findViewById(R.id.tvstatus);
		stat.setOnClickListener(this);
		nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		
		Bundle gotbasket=getIntent().getExtras();
				String gotpnr=gotbasket.getString("key");
				PnrDatabase pnrdb=new PnrDatabase(this);
				pnrdb.open();
				status.setText(pnrdb.getStatus(gotpnr));
				pnrdb.close();
				nm.cancel(uniqueid);
		finish();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		
	}

}
