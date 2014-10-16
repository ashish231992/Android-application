package com.example.remindit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.Log;

public class UrlGenerator {
	Context ourContext;
	PnrDatabase pnrdatabase;
	ConnectionControl cc;
	ArrayList<ArrayList<Object>> data;

	/**************** CONSTRUCTOR *****************************/

	public UrlGenerator(Context c) {
		Log.d("UrlGenerator : ", "CTOR CALLED ");
		this.ourContext = c;
	}

	/**************** giveURL() FUNCTION ***********************/

	public void giveURL() {
		//Log.d("UrlGenerator : ", "giveURL function CALLED ");
		Date Current_Date, Date_Of_Journey;

		String PNR_URL = "http://www.railpnrapi.com/";
		String pnr = null, pnr1 = null;
		String doj = null;
		String WEBSITE = null;

		int i;
		/****** Get Current Date ********/
		Current_Date = getMyDate();

		/****** yahan While loop lagega taaki hum saare PNR feetch kr ske ********/
		try {
			pnrdatabase = new PnrDatabase(ourContext);
			pnrdatabase.open();

			data = pnrdatabase.getAllPNR();

			/********************* comparing database *******************************/
			i = 0;
			cc = new ConnectionControl(ourContext);
			while (i < data.size()) {
				
				pnr = data.get(i).toString();
				Log.d("PNR():", pnr);
				pnr1 = pnr.substring(pnr.indexOf("[") + 1,
						pnr.indexOf("[") + 11);
				doj = pnrdatabase.getDoj(pnr1);
				Date_Of_Journey = stringToDate(doj);
				Log.d("date",Current_Date.toString()+Date_Of_Journey.toString());
				if (Date_Of_Journey.after(Current_Date)	|| Date_Of_Journey.equals(Current_Date)) {
					Log.d("date","succed");
					WEBSITE = PNR_URL + pnr1;
					cc.controlMethod(WEBSITE, pnr1);
				} else {
					pnrdatabase.deleteEntry(pnr1);
				}
				i++;

			}
			pnrdatabase.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pnrdatabase.close();
		}

	}

	/******************** getMyDate function ***************************/

	public Date getMyDate() {
		Date date = null;
		try {

			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String formattedDate = df.format(c.getTime());
			Log.d(formattedDate, "time");
			date = stringToDate(formattedDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	/******************** stringToDate function ***************************/
	public Date stringToDate(String str) {
		Date date = null;
		String Date_Format = "dd-MM-yyyy";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Date_Format);
			date = sdf.parse(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
}
