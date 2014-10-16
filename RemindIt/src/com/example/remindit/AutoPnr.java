package com.example.remindit;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class AutoPnr extends Activity {

	TabHost th;
	PnrDatabase pnrdatabase;
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
			setContentView(R.layout.autopnr);
			//Tpnr = (TextView) findViewById(R.id.hello);
			// THE DATA TABLE
			dataTable = (TableLayout) findViewById(R.id.data_table);

			Log.d("AutoPNR ", "onCreate: ");
		

			//th = (TabHost) findViewById(R.id.tabhost);
			//th.setup();
			//TabSpec specs = th.newTabSpec("tag1");
			//specs.setContent(R.id.tab1);
			//specs.setIndicator("Show PNR");
		//th.addTab(specs);
			

			//ug = new UrlGenerator(this);
			//ug.giveURL();
			

			pnrdatabase = new PnrDatabase(this);
			pnrdatabase.open();
		//Tpnr.setText(pnrdatabase.getData());
			/*************** yha pr hme saari arrays milengi database se */

			data = pnrdatabase.getAllRowsAsArrays();

			for (int position = 0; position < data.size(); position++) {
				TableRow tableRow = new TableRow(this);

				ArrayList<Object> row = data.get(position);

				TextView statusText = new TextView(this);

				statusText.setText(row.get(4).toString());
				tableRow.addView(statusText);

				/*TextView textTwo = new TextView(this);

				textTwo.setText(row.get(2).toString());
				tableRow.addView(textTwo);

				TextView textThree = new TextView(this);

				textThree.setText(row.get(3).toString());

				tableRow.addView(textThree);*/

				dataTable.addView(tableRow);
				String PNR=row.get(0).toString();
				

			}
			


		} catch (Exception e) {
			Log.e("ERROR", e.toString());
			e.printStackTrace();
		} finally {
			pnrdatabase.close();
		}

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
			Intent p=new Intent("com.example.remindit.PNRPREF");
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
