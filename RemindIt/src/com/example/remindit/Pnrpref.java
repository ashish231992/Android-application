package com.example.remindit;


import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Pnrpref extends PreferenceActivity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pnrpref);
	
	}

	
}
