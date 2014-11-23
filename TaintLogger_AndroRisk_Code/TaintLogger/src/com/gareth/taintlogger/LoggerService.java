package com.gareth.taintlogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.appanalysis.LogEntry;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LoggerService extends Service {
	
	private static final String TAG = LoggerService.class.getSimpleName();
	
	public static final String LOGGER_SERVICE_FILTER = "com.gareth.taintlogger.INTENT";
	
	public static final String KEY_ACTION = "key_action";
	
	public static final String ACTION_STOP = "action_stop";
	public static final String ACTION_START = "action_start";
	
	private boolean mRunning;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private String mFilePath;
	
	private File mLogFile;
	
	private BroadcastReceiver mReciever;
	
	private Thread mLoggerThread;
	
	private void stopService() {
		mRunning = false;
		if (mLoggerThread != null) {
			try {
				mLoggerThread.join(10000);
				
				this.unregisterReceiver(mReciever);
				this.stopSelf();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		mReciever = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent intent) {
				Log.i("RECIEVER", "Recieved intent.");
				if (intent.hasExtra(KEY_ACTION)) {
					String action = intent.getStringExtra(KEY_ACTION);
					
					if (action.equals(ACTION_STOP)) {
						// Stop the service
						stopService();
					}
				}
				
			}
			
		};
		
		this.registerReceiver(mReciever, new IntentFilter(LOGGER_SERVICE_FILTER));
		
		Toast.makeText(this, "Logger service started.", Toast.LENGTH_SHORT).show();
		
		File baseDir = this.getApplicationContext().getExternalFilesDir("");
		
		Log.i("BASEDIR", baseDir.getAbsolutePath());
		
		mLogFile = new File(baseDir, "log.txt");
		Log.i("TEST", mLogFile.getAbsolutePath());
		
		
		// Start processing logs
		mRunning = true;
		mLoggerThread = new Thread(new LogcatListenerThread());
		mLoggerThread.start();
		
		
		return Service.START_STICKY;
	}
	
	class LogcatListenerThread implements Runnable {
		FileOutputStream out;
		
		public void run() {
			
			try {
				out = new FileOutputStream(mLogFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			handleLogcatMessages();
		}
		
		private void logMessageToFile(String message) {
			if (out != null) {
				try {
					byte[] messageBytes = message.getBytes();
					out.write(messageBytes);
					out.flush();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		
		private void handleLogcatMessages() {
			String thTAG = "logcatThread";
			
			
			RootLogcatDevice dev = RootLogcatDevice.getInstance();
			
			dev.open();
			
			while (mRunning) {
				LogEntry entry = dev.readEntry();
				
				if (entry != null) {
					// Get additional info
					if (LogUtils.isSend(entry)) {
						String ipAddress = LogUtils.getIPAddress(entry);
						String appName = LogUtils.getProcessName(LoggerService.this.getApplicationContext(), entry);
						String taint = LogUtils.getTaint(entry);
						String data = LogUtils.getData(entry);
						
						String[] outData = {
								entry.getTimestamp(),
								appName,
								taint,
								ipAddress,
								data
						};
						
						StringBuilder builder = new StringBuilder();
						
						for (int i = 0; i < outData.length; i++) {
							if ((i > 0) && (i < outData.length)) {
								builder.append("|");
							}
							builder.append(outData[i]);
						}
						builder.append("\n");
						
						logMessageToFile(builder.toString());
						
					}
				}
			}
			
			Log.d(thTAG, "Closing root logcat device.");
			dev.close();

		}
		
	}
	
	
}
