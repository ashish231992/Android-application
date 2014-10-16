package com.example.remindit;


import java.util.ArrayList;

import com.example.remindit.LocationReminder.progresstask;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManualPnr extends Activity implements View.OnClickListener {
	Button Manual;
	EditText pnr_Manual;
	DatePicker date;
	PnrDatabase pnrdatabase;
	ConfDatabase cnfdb;
	String getPNR=null;
	ArrayList<Object> row;
	ArrayList<ArrayList<Object>> data;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.manualpnr);
			pnr_Manual = (EditText) findViewById(R.id.et_manual_pnr);
			pnr_Manual.setOnKeyListener(new OnKeyListener() {
			    @Override
			    public boolean onKey(View v, int keyCode, KeyEvent event) {
			        if (keyCode == KeyEvent.KEYCODE_ENTER) { 
			                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(pnr_Manual.getWindowToken(), 0);
			    return true;
			        }
			        return false;
			    }

				
			});
			date = (DatePicker) findViewById(R.id.datePicker1);
			getPNR=pnr_Manual.getText().toString();
			
			Manual = (Button) findViewById(R.id.bManual);
			Manual.setOnClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onClick(View v) {
		switch (v.getId()) { 
		case R.id.bManual:
			pnrdatabase=new PnrDatabase(this);
			pnrdatabase.open();
			data = pnrdatabase.getAllRowsAsArrays();
			pnrdatabase.close();
			int flag=0;
			for (int position = 0; position < data.size(); position++) {
				ArrayList<Object> row = data.get(position);
				String PNR=row.get(0).toString();
			if(pnr_Manual.getText().toString().compareTo(PNR)==0){
					flag=1;
					break;		
				}
			}
			
			if(pnr_Manual.getText().toString().length()!=10){
				String error = "Please Enter a 10 Digit valid PNR";
				Dialog d = new Dialog(this);
				d.setTitle("Invalid PNR!");
				TextView tv = new TextView(this);
				tv.setText(error);
				d.setContentView(tv);
				d.show();
			}
			else if(flag==1){
				Dialog d = new Dialog(this);
				d.setTitle("PNR Already Exist");
				TextView tv = new TextView(this);
				tv.setText("");
				d.setContentView(tv);
				d.show();
				pnr_Manual.setText("");
			}
			else{
				try{
					pnrdatabase = new PnrDatabase(this);
					cnfdb=new ConfDatabase(this);
					cnfdb.open();
					pnrdatabase.open();
				pnrdatabase.createEntry(
								pnr_Manual.getText().toString(),
								date.getDayOfMonth() + "-" + (date.getMonth()+1) + "-"
								+ date.getYear(),0,0,"abcd"," ",0);
					
					cnfdb.createEntry(pnr_Manual.getText().toString()," ","false");
					pnrdatabase.close();
					cnfdb.close();
					Toast.makeText(this, "PNR Inserted manually!!", 0).show();
				}
				catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "Error!", 0).show();
				}
			}
			
			/* ProgressDialog dialog;
			 dialog = new ProgressDialog(ManualPnr.this);
	            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            
	           dialog.setTitle("Message");
	           dialog.setMessage("Loading Data........");
	           dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	            dialog.show();
	           
				dialog.dismiss();*/
				
				Thread menuThread=new Thread(){
					public void run(){
						try{
							 UrlGenerator ug;
								ug=new UrlGenerator(ManualPnr.this);
								ug.giveURL();
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							
						}
					}
				};
				menuThread.start();
				
				
				
				
				
	            
			
			pnr_Manual.setText(" ");
			break;

		default:
			break;
		}
				
	}
	
	
}
