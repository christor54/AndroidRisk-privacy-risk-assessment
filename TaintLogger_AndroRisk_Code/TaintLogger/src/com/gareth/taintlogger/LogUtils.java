package com.gareth.taintlogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.appanalysis.LogEntry;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

public class LogUtils {
	
    private static Hashtable<Integer, String> ttable = new Hashtable<Integer, String>();
    static {
        // ttable.put(new Integer(0x00000000), "No taint");
        ttable.put(Integer.valueOf(0x00000001), "Location");
        ttable.put(Integer.valueOf(0x00000002), "Address Book (ContactsProvider)");
        ttable.put(Integer.valueOf(0x00000004), "Microphone Input");
        ttable.put(Integer.valueOf(0x00000008), "Phone Number");
        ttable.put(Integer.valueOf(0x00000010), "GPS Location");
        ttable.put(Integer.valueOf(0x00000020), "NET-based Location");
        ttable.put(Integer.valueOf(0x00000040), "Last known Location");
        ttable.put(Integer.valueOf(0x00000080), "camera");
        ttable.put(Integer.valueOf(0x00000100), "accelerometer");
        ttable.put(Integer.valueOf(0x00000200), "SMS");
        ttable.put(Integer.valueOf(0x00000400), "IMEI");
        ttable.put(Integer.valueOf(0x00000800), "IMSI");
        ttable.put(Integer.valueOf(0x00001000), "ICCID (SIM card identifier)");
        ttable.put(Integer.valueOf(0x00002000), "Device serial number");
        ttable.put(Integer.valueOf(0x00004000), "User account information");
        ttable.put(Integer.valueOf(0x00008000), "browser history");
    }
	
    public static String getTaint(LogEntry entry) {
    	String msg = entry.getMessage();
    	
    	// match hex digits
    	Pattern p = Pattern.compile("with tag 0x(\\p{XDigit}+) ");
        Matcher m = p.matcher(msg);

        if(m.find() && m.groupCount() > 0) {

            String match = m.group(1);

            // get back int
            int taint;
            try {
                taint = Integer.parseInt(match, 16);
            }
            catch(NumberFormatException e) {
                return "Unknown Taint: " + match;
            }

            if(taint == 0x0) {
                return "No taint";
            }

            // for each taint
            ArrayList<String> list = new ArrayList<String>();
            int t;
            String tag;
            
            // check each bit
            for (int i=0; i<32; i++) {
            	t = (taint>>i) & 0x1;
            	tag = ttable.get(Integer.valueOf(t<<i));
                if(tag != null) {
                    list.add(tag);
                }
            }

            // build output
            StringBuilder sb = new StringBuilder("");
            if(list.size() > 1) {
                for(int i = 0; i < list.size() - 1; i++) {
                    sb.append(list.get(i) + ", ");
                }
                sb.append(list.get(list.size() - 1));
            }
            else {
                if(!list.isEmpty()) {
                    sb.append(list.get(0));
                }
            }

            return sb.toString();
        }
        else {
            return "No Taint Found";
        }
    }

    public static String getProcessName(Context ctx, LogEntry entry) {
        ActivityManager mgr = (ActivityManager) ctx.getSystemService(
            Context.ACTIVITY_SERVICE);

        int pid = entry.getPid();
        
        String pname = "";
        List<RunningAppProcessInfo> apps = mgr.getRunningAppProcesses();
        for(RunningAppProcessInfo pinfo : apps) {
            if(pinfo.pid == pid) {
                pname = pinfo.processName;
                break;
            }
        }

        return pname;
    }
    
    public static boolean isSend(LogEntry entry) {
    	return entry.getMessage().contains("libcore.os.send");
    }

    public static String getIPAddress(LogEntry entry) {
    	String msg = entry.getMessage();
    	
    	Pattern p = Pattern.compile("\\((.+)\\) ");
        Matcher m = p.matcher(msg);

        if(m.find() && m.groupCount() > 0) {
        	String result = m.group(1);
        	// remove trailing junk
        	if (result.contains(")"))
        		result = result.substring(0,result.indexOf(")")-1);
            return result;
        }
        else {
            return null;
        }
    }
    
    
    public static String getData(LogEntry entry) {
    	String msg = entry.getMessage();
    	
        int start = msg.indexOf("data=[") + 6;
        return msg.substring(start);
    }
    
    public static void writeEntryToLogFile(Context ctx, String fileName, LogEntry entry) {
		String ipAddress = LogUtils.getIPAddress(entry);
		String appName = LogUtils.getProcessName(ctx.getApplicationContext(), entry);
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
		
		String echoCommand = "cat \"" + data + "\" >> " + fileName;
		Log.i("WTF", echoCommand);
		
		try {
			RootTools.getShell(true).add(new CommandCapture(1, echoCommand));
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
