package com.example.remindit;


import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class Ourreceiver extends  BroadcastReceiver{
	String text,address;
	PnrDatabase pnrdatabase;
	ArrayList<Object> row;
	ArrayList<ArrayList<Object>> data;
	
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		//Toast.makeText(arg0, "ourreciver",0).show();
		try{
			SmsMessage[] message=null;
		
		Bundle bundle=arg1.getExtras();
		
		
		if(bundle!=null)
		{
			Object[]pdus=(Object[])bundle.get("pdus");
			message=new SmsMessage[pdus.length];
			for(int i=0;i<message.length;i++)
			{
				message[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
				
			}
		}
		
		 
			
			String text=message[0].getMessageBody().toString();
			String address=message[0].getOriginatingAddress();
			String []temp=text.split("PNR:",2);
			String [] temp2=temp[1].split(",TRAIN:",2);
			String pnr=temp2[0];
			temp=temp2[1].split(",DOJ:",2);
			String trainno=temp[0];
			temp2=temp[1].split(",TIME:");
			String doj=temp2[0];
			temp=temp2[1].split(",");
			String time=temp[0];
			//String seattype=temp[1];
			//String journey=temp[2];
			//String passengername=temp[3];
			//String seatno=temp[4];
			 
			//yahan se database main pnr insert ho rha hai via irctc msg
			
			pnrdatabase = new PnrDatabase(arg0);
			ConfDatabase cnfdb=new ConfDatabase(arg0);
			cnfdb.open();
			pnrdatabase.open();
			
			data = pnrdatabase.getAllRowsAsArrays();
			int flag=0;
			for (int position = 0; position < data.size(); position++) {
				ArrayList<Object> row = data.get(position);
				String PNR=row.get(0).toString();
			if(pnr.compareTo(PNR)==0){
					flag=1;
					break;		
				}
			}
			if(flag==0){
				pnrdatabase.createEntry (pnr,doj, 0, 0, "abcd"," ",0);
				cnfdb.createEntry(pnr," ","false");
				
			}
			else{
				
				Toast.makeText(arg0, trainno+":    "+pnr+":  "+doj+":  "+address,0).show();
			}
			
			pnrdatabase.close();
			cnfdb.close();
			}
		catch(Exception e)
		{
			//Toast.makeText(arg0, "hello ",0).show();
		}
	}
}
	


