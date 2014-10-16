package com.example.remindit;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {
	String categories[] = { "Auto PNR", "Manual PNR", "Location Reminder",
			"List of Location Reminders" };

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		switch (position) {

		case 0:
			String pos;
			pos = "AutoPnr";
			try {
				Class initializeClass = Class.forName("com.example.remindit."
						+ pos);
				Intent newIntent = new Intent(Menu.this, initializeClass);
				startActivity(newIntent);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			String p;
			p = "ManualPnr";
			try {
				Class initializeClass = Class.forName("com.example.remindit."
						+ p);
				Intent newIntent = new Intent(Menu.this, initializeClass);
				startActivity(newIntent);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			String pos2;
			pos2 = "LocationReminder";
			try {
				Class initializeClass = Class.forName("com.example.remindit."
						+ pos2);
				Intent newIntent = new Intent(Menu.this, initializeClass);
				startActivity(newIntent);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case 3:
			String pos3;
			pos3 = "LocationReminderList";
			try {
				Class initializeClass = Class.forName("com.example.remindit."
						+ pos3);
				Intent newIntent = new Intent(Menu.this, initializeClass);
				startActivity(newIntent);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Menu.this,
				android.R.layout.simple_list_item_activated_1, categories));
		getListView().setCacheColorHint(Color.rgb(36, 33, 32));
		
		int[] colors = {0, 0xFFFF0000, 0};
		getListView().setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		getListView().setDividerHeight(5);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowup=getMenuInflater();
		blowup.inflate(R.menu.coolmenu, menu);
		return true;
	}
	
}
