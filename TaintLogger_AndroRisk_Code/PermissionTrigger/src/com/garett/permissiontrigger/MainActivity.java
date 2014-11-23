package com.garett.permissiontrigger;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements LocationListener {
	
	public static final String TAG = "PermissionTriggerTaint";
	
	private Location mLastLocation;
	private LocationManager mLocationManager;
	
	private Button mSendButton;
	private TextView mStatusLabel;
	
	private void setLocation(Location loc) {
		if (loc != null) {
			mSendButton.setEnabled(true);
			mStatusLabel.setText(loc.toString());
		}
		mLastLocation = loc;
		
		
	}
	
	private void sendData() {

		String locationString = mLastLocation.getLatitude() + "@" + mLastLocation.getLongitude();
		String url = "http://192.168.1.116:8081/location/"+locationString;
		SendDataThread sdt = new SendDataThread(url);
		
		Thread thread = new Thread(sdt);
		thread.start();
		
	}
	
	private class SendDataThread implements Runnable {
		
		private String mUrl;
		
		public SendDataThread(String url) {
			mUrl = url;
		}

		@Override
		public void run() {
			HttpClient httpClient = new DefaultHttpClient();
			try {
				Log.i(TAG, "Sending request.");
				
				
				HttpGet request = new HttpGet(mUrl);
				
				HttpResponse repsonse = httpClient.execute(request);
				Log.i(TAG, "Got response.");
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		*/
		
		mStatusLabel = (TextView) findViewById(R.id.labelCurrentLocation);
		mStatusLabel.setText("Acquiring location...");
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 0,
				this);
		
		
		mSendButton = (Button) this.findViewById(R.id.sendButton);
		
		
		if (mSendButton != null) {
			mSendButton.setEnabled(false);
			
			mSendButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// send
					sendData();
				}
			});
			
		}
		
	
		
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		
		setLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
