package com.example.remindit;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LocationReminderList extends Activity {

	TabHost th;
	LocationDatabase locdb;
	ArrayList<Object> row;
	ArrayList<ArrayList<Object>> data;
	TextView Tpnr;

	// the table that displays the data
	TableLayout dataTable;
	String url;
	UrlGenerator ug;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.locationreminderlist);
			//Tpnr = (TextView) findViewById(R.id.hello);
			// THE DATA TABLE
			dataTable = (TableLayout) findViewById(R.id.data_table);

			Log.d("AutoPNR ", "onCreate: ");
			

			locdb = new LocationDatabase(this);
			locdb.open();
		//Tpnr.setText(pnrdatabase.getData());
			/*************** yha pr hme saari arrays milengi database se */

			data = locdb.getAllRowsAsArrays();

			for (int position = 0; position < data.size(); position++) {
				TableRow tableRow = new TableRow(this);

				ArrayList<Object> row = data.get(position);

				TextView statusText = new TextView(this);
				
				String address=row.get(0).toString();
				String note=row.get(3).toString();
				statusText.setWidth(LayoutParams.MATCH_PARENT);

				statusText.setText("ADDRESS: "+address+" \n"+"Reminder Note: \n"+note+"\n\n\n");
				tableRow.addView(statusText);
                
				dataTable.addView(tableRow);
				String PNR=row.get(0).toString();
				

			}
			


		} catch (Exception e) {
			Log.e("ERROR", e.toString());
			e.printStackTrace();
		} finally {
			locdb.close();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowup=getMenuInflater();
		blowup.inflate(R.menu.locreminderlist, menu);
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
			locdb = new LocationDatabase(this);
			locdb.open();
				
				locdb.deleteall();
				finish();
	
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
