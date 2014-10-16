package com.example.remindit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class Status {
	Context ourContext;

	public String TRAIN_NAME = "tname";
	public String TRAIN_NUMBER = "tnum";
	public String CLASS = "class";
	public String TRAVEL_DATE = "tdate";
	public String DOJ = "tdate";
	JSONArray passengerDetails;
	public String PASSENGER = "pax";
	public String SEAT_NUMBER = "seat_number";
	public String COACH = "coach";
	public String BIRTH = "berth";
	public String QUOTA = "quota";
	public String STATUS = "status";
	//public String PNR_NUMBER = null;
	public String CHART_PREPARED = "charted";
	public String result;
	

	/**************** CONSTRUCTOR ***********************/

	public Status(Context c) {
		this.ourContext = c;
		//Log.d("Status: ", "CTOR CALLED ");

	}

	PnrDatabase pnrdatabase;
	ConfDatabase cnfdb;
	int uniqueid=14567;

	/**************** getStatus FUNCTION ***********************/
	public void getStatus(JSONObject status,String pnr) {
		try {
			//Log.d("Status: ", "getStatus FUNCTION CALLED "+status);
			pnrdatabase = new PnrDatabase(ourContext);
			cnfdb=new ConfDatabase(ourContext);
			Log.d("status: ",status.toString());
			
			pnrdatabase.open();
			cnfdb.open();
			//JSONObject jObj = null;
			
			//jObj = new JSONObject(status);
			String chart_prepare = status.getString(CHART_PREPARED);
			Log.d("pnr no",pnr);
			//String pnr="1234567890";
			if (chart_prepare.equals("true")) {
				String str = getCurrentStatus(status,pnr);
               Log.d("STATUS:", "CHART PREPARED");
               
         cnfdb.updatechart(pnr, "prepared");
        
			} else {
				String str = getCurrentStatus(status,pnr);
				Log.d("check status",str);
  
				pnrdatabase.updateStatus(pnr, str);
			}
			 cnfdb.close();
			pnrdatabase.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 cnfdb.close();
			pnrdatabase.close();
		}
	}

	/**************** getCurrentStatus FUNCTION ***********************/

	public String getCurrentStatus(JSONObject currentStatus,String pnr) {
		String finalStatus = " ";
		try {

			//String pnr_number = currentStatus.getString(PNR_NUMBER);
			//String pnr_number="1234567890";
			String train_name = currentStatus.getString(TRAIN_NAME);
			String train_number = currentStatus.getString(TRAIN_NUMBER);
			String Class = currentStatus.getString(CLASS);
			String travel_date = currentStatus.getString(TRAVEL_DATE);
			//JSONObject jObj1 = new JSONObject(travel_date);
			String doj = currentStatus.getString(DOJ);
			passengerDetails = currentStatus.getJSONArray(PASSENGER);
			result = " ";
			for (int i = 0; i < passengerDetails.length(); i++) {
				JSONObject c = passengerDetails.getJSONObject(i);
				result += "\t"+ c.getString(COACH)+","+c.getString(BIRTH)+","+c.getString(QUOTA)+"\t\t\t\t"+ c.getString(STATUS) +"\n";
			}
			finalStatus = "\n PNR_Number:   " + pnr + "\n Date Of Journey:   " + doj+ "\n Train Name:   "
					+ train_name + "\n Train Number:   " + train_number
					+ "\n Class:  " + Class 
					+ "\n\t  Seats\t\t\t\t Status: \n" +result;
			
			Log.d("Status: ", "getCurrentStatus FUNCTION CALLED "+finalStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalStatus;

	}
}
