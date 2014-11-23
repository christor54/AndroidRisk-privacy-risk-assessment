package com.androrisk.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Access {
	private Date time;
	//private String time;
	private String app;
	private ArrayList <String> data ;
	private String ip; 
	private Boolean active;
	private String extra;
	
	
	public Access(Date time, String app, ArrayList <String> data, String ip,
			Boolean active, String extra) {
		super();
		this.time = time; 
		this.app = app;
		this.data = data;
		this.ip = ip;
		this.active = active;
		this.extra = extra;
	}
	
	public Access(String string){
	//Input : string = log extracted from log file
	//Output : Access	
		super();
		char [] sChar = string.toCharArray();
		String attribute; 
		int i=0, la=0,cu=0;
		for (char c : sChar){
			if(c=='|'){
				i++;
				attribute=string.substring(la,cu);
				affectAttribute(i,attribute);
				attribute=null;
				la=cu+1;
			}
			cu++;
		}
		display();
	}
	
	public void affectAttribute(int i, String attribute){
		this.active=true;
		switch (i){
		case 1://time

			Date date=null;
			try {
//				date = new SimpleDateFormat("MM:dd:yyyy:HH:mm:ss", Locale.ENGLISH).parse(attribute);
				date = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.ENGLISH).parse(attribute); 
				date.setYear(114); 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.time=date;
			break;
		case 2://app
			this.app= attribute;
			break;
		case 3://data
			this.data= makeListData(attribute);
			break;
		case 4://ip
			this.ip= attribute;
			break;
			
//		case 5://active
//			//Boolean a = Boolean.getBoolean(attribute);
//			int a = Integer.parseInt(attribute);
//			if(a==0)
//				this.active= false;
//			else
//				this.active=true;
//			break;
		case 5://extra
			this.extra= attribute;
			break;
		default: 
			System.out.println("Access->affectAttribute->defaut");
			break;
		}
	}
	
	public ArrayList<String> makeListData(String attribute) {
		//Location, GPS Location, SMS
		ArrayList<String> datas = new ArrayList<String>();
		char [] sChar = attribute.toCharArray();
		String data; 
		int num=0, la=0,cu=0, lenght=sChar.length;
		for (char c : sChar){
			if((c==',')||(cu==sChar.length)){
				data=attribute.substring(la,cu);
				datas.add(num,data);
				num++;
				data=null;
				la=cu+2;//to skip the space after the comma
			}
			cu++;
		}
		return datas;
	}

	public void display(){
		System.out.println("Display Access -> Time:" + this.getTime()+"| App:"+this.getApp()+"| data:"+this.getDataString()+"| ip:"+this.getIp()+"| active:"+this.getActive()+"| extra:"+this.getExtra()); 
	}
	
	public Date getTime() {
		return time;
	}
//	public String getTime() {
//		return time;
//	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public ArrayList <String> getData() {
		return data;
	}
	public String getDataString() {
		String datasS = " ",temp;
		for (String  s : data) {
			temp=s + " "; 
			datasS= datasS.concat(temp);
		}		
		return datasS;
	}
	public void setData(ArrayList <String> data) {
		this.data = data;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	} 
	
}
