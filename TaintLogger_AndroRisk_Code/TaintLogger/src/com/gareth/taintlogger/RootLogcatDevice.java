package com.gareth.taintlogger;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.appanalysis.LogEntry;

import android.util.Log;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.Command;

public class RootLogcatDevice {
	
	private BlockingQueue<LogEntry> mEntries;
	
	private static RootLogcatDevice mInstance;
	
	public static RootLogcatDevice getInstance() {
		if (mInstance == null) {
			mInstance = new RootLogcatDevice();
		}
		return (mInstance);
	}
	private Command mLogcatCommand;
	
	private RootLogcatDevice() {
		mEntries = new LinkedBlockingQueue<LogEntry>();
		
		mLogcatCommand = new Command(0, Integer.MAX_VALUE, "logcat -v time *:S TaintLog:*")
		{

			@Override
			public void commandCompleted(int arg0, int arg1) {
				Log.i("RLD", "Command completed. ***********************");
				
			}

			@Override
			public void commandOutput(int arg0, String line) {
				
				LogEntry entry = LogEntry.fromLine(line);
				if (entry != null) {
					mEntries.add(entry);
				}
				
			}

			@Override
			public void commandTerminated(int arg0, String arg1) {
				Log.i("RLD", "Command terminated. ***********************");
				
			}
			
		
		};
		
	}
	
	
	public void open() {
		if (!mLogcatCommand.isExecuting()) {
			try {
				RootTools.getShell(true).add(mLogcatCommand);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RootDeniedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void close() {
		if (mLogcatCommand.isExecuting()) {
			mLogcatCommand.terminate("because");
		}
	}
	
	// 5-second timeout
	public LogEntry readEntry() {
		try {
			return (mEntries.poll(5, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}
