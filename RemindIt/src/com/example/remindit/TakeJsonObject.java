package com.example.remindit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class TakeJsonObject {

	int count;
	Context ourContext;
	JSONArray passenger = null;
	// public String CHART_PREPARED = "chart_prepared";
	public String PASSENGER = "pax";
	public String STATUS = "status";

	PnrDatabase pnrdatabase;
	int counter;

	/**************** CONSTRUCTOR ***********************/

	public TakeJsonObject(Context c) {
		this.ourContext = c;
	//	Log.d("TakeJsonObject: ", "CTOR CALLED ");
	}

	Status compstatus;

	/**************** getJsonObject function ***********************/

	public void getJsonObject(JSONObject comStatus, String pnr) {
		try {
			Log.d("TakeJsonObject : ", "getJsonObject function CALLED ");
			pnrdatabase = new PnrDatabase(ourContext);
			pnrdatabase.open();
			Log.d("json: ",comStatus.toString());
			counter = 0;
			compstatus = new Status(ourContext);
			if (comStatus.toString().compareTo("{}")==0) {
				Log.d("TakeJsonObject : ", "string is null ");
				counter = pnrdatabase.getCounter(pnr);
				if (counter <= 5) {
					counter++;
					pnrdatabase.updateCounter(pnr, counter);
				}
				else {
					pnrdatabase.deleteEntry(pnr);
				}

			} 
			else {

				compstatus.getStatus(comStatus,pnr);
				int wait = 0, conf = 0;

				//JSONObject jObj = new JSONObject(comStatus);
				// String chart_prepare = jObj.getString(CHART_PREPARED);
				passenger = comStatus.getJSONArray(PASSENGER);
				Log.d("passenger: ",passenger.toString());
				for (int i = 0; i < passenger.length(); i++) {
					JSONObject c = passenger.getJSONObject(i);
					String status = c.getString(STATUS);
					if (status.equals("CNF")) {
						conf++;
					} else {
						wait++;
					}
				}
				pnrdatabase.updateCnf(pnr, conf);
				pnrdatabase.updateWait(pnr, wait);
				pnrdatabase.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pnrdatabase.close();
		}

	}
}
