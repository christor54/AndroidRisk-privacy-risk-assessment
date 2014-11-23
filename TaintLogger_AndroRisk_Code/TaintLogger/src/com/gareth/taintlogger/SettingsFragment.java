package com.gareth.taintlogger;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		
		Preference enabledPreference = this.findPreference(SettingsActivity.KEY_ENABLED);
		if (enabledPreference != null) {
			
			enabledPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

				@Override
				public boolean onPreferenceChange(Preference pref, Object newValue) {
					try {
						boolean value = Boolean.parseBoolean(newValue.toString());
						if (value == true) {
							startService();
						}
						else {
							// Stop service
							stopService();
						}
					}
					catch (Exception ex) {
						
					}
					
					return (true);
				}
				
			});
			
		}
	}
	
	
	public void startService() {
		Intent startServiceIntent = new Intent(this.getActivity(), LoggerService.class);
		
		this.getActivity().startService(startServiceIntent);
	}
	
	public void stopService() {
		Intent stopIntent = new Intent(LoggerService.LOGGER_SERVICE_FILTER);
		stopIntent.putExtra(LoggerService.KEY_ACTION, LoggerService.ACTION_STOP);
		
		this.getActivity().sendBroadcast(stopIntent);
		
		
	}

}
