package com.gareth.taintlogger;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
	
	public static final String KEY_ENABLED = "pref_key_enabled";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.getFragmentManager().beginTransaction()
								.replace(android.R.id.content, new SettingsFragment())
								.commit();
	}

}
