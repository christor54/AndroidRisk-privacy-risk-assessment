package com.androrisk.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.Period;

import android.util.Log;

import com.androrisk.models.Access;
import com.example.androrisk.dummy.ThreatList.ThreatItem;

public class Extractor {
	private final String filePath = "dev/log.txt";
//	private final String filePath = "sdcard/Android/data/com.gareth.taintlogger/files/log.txt";
	private ArrayList<Access> listAccess=null;
	private int[] risk= new int[]{0,0,0,0,0};
	private ArrayList<ArrayList<Access>> Threat_ListAccess = new ArrayList<ArrayList<Access>>();
	
	//An ArrayList is used in the mapping so that the Id
	public static Map<String, ArrayList<Integer>> PERMISSION_IMPACT = new HashMap<String, ArrayList<Integer>>();
    static {
        addData("GPS Location",new ArrayList<Integer>(Arrays.asList(
        		1)));
        addData("Location",new ArrayList<Integer>(Arrays.asList(
        		1)));
    }
    
    public static Map<Integer, ArrayList<String>> THREAT_PERMISSIONS = new HashMap<Integer, ArrayList<String>>();
    static {
    	addDataToThreat(1, new ArrayList<String>(Arrays.asList(
    			"GPS location", "Location")));
    	addDataToThreat(2, new ArrayList<String>(Arrays.asList(
    			"lol", "Toto", "La Plata")));
    	addDataToThreat(3, new ArrayList<String>(Arrays.asList(
    			"lol", "Toto", "La Plata")));
    	addDataToThreat(4, new ArrayList<String>(Arrays.asList(
    			"lol", "Toto", "La Plata")));
    	addDataToThreat(5, new ArrayList<String>(Arrays.asList(
    			"lol", "Toto", "La Plata")));
    }
    public final double BASE_LOG_LIKELIHOOD= 1.7;
    private int period_of_use; 
    
    public Extractor ()  {
    	File file = new File(filePath);
    	initializeThreatAccess();
    	if(file!=null){
    		ArrayList<String> listStrings  = makeLineIntoStrings(file);
    	//	displayStringList(listStrings);
    		listAccess=makeListAccess(listStrings);
    		if(listAccess!=null&&!listAccess.isEmpty()){
    			period_of_use=periodInDays(listAccess.get(0).getTime()); 
	    		exctractLists();
	    		assessAllRisks();
    		}
    	}
    }
    
    public int periodInDays (Date start){
		Long startL = start.getTime(),
			    end = listAccess.get(listAccess.size()-1).getTime().getTime();
		Interval interval = new Interval(start.getTime(), end);
		Period p = interval.toPeriod();
		int days= p.getDays();
		if(days>0)
			return days;
		else 
			return 1; 
    }
    //RISK ASSESSMENT 

	private void assessAllRisks() {
		for(int i=0; i<5; i++)
			risk[i] = riskAssessement(Threat_ListAccess.get(i));
	}


	public int riskAssessement(ArrayList<Access> list) {
		// TODO Auto-generated  method stub
		int likelihood,impact;
		if(list!=null&&!list.isEmpty()){
			likelihood= likelihood(list);
			impact=impact(list);
			return impact*likelihood;
		}
		else
			return 0; 
	}
	
	public int likelihood (ArrayList<Access> list) {
		int numberOfAccess = numberOfAccess(list);
		int numberByDay = numberOfAccess/period_of_use;
		double likelihoodD = Math.log(numberOfAccess+1)/Math.log(BASE_LOG_LIKELIHOOD);
		int likelihood = (int) likelihoodD;
		if(likelihood<1)
			return 1;
		if(likelihood>10)
			return 10;
		return likelihood;
	}
	
	private int numberOfAccess(ArrayList<Access> list) {
		int cmpt=0;
		for (Access access : list) {
			cmpt= cmpt+ access.getData().size(); 
		}
		return cmpt;
	}

	public int impact (ArrayList<Access> list) {
		int size = list.size();
		int totalImpact=0;
		int average; 
		for (Access access : list) {
			totalImpact=totalImpact+ impactAccess(access); 
		}
		average= totalImpact/size;
		return average; 

	}

	private int impactAccess(Access access) {
		int impactData=1, impact=1;
		ArrayList<String> datas= access.getData(); 
		for (String data : datas) {
			if(PERMISSION_IMPACT.containsKey(data)){
				if(PERMISSION_IMPACT.get(data).get(0)!=null){
					impactData= PERMISSION_IMPACT.get(data).get(0)*2;
				}
			}	
			if(access.getActive()==true)
				impactData=impactData+2;
			impact= impactData + impact;
		}
		impact= impact/datas.size();
		return impact;// average of the impact of the access 
	}

	// LISTS' EXCTRATION 
	
	@SuppressWarnings("resource")
	public ArrayList<String> makeLineIntoStrings(File file) {
		Scanner sc;
		ArrayList<String> lines = new ArrayList<String>();
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				  lines.add(sc.nextLine());
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return lines; 
	}
	
	
	
	public ArrayList<Access> makeListAccess(List<String> list){
		ArrayList<Access> accessList = new ArrayList<Access>();
		Access access; 
		for(String s : list){
			access= new Access (s);
			displayAccess("Access listAccess",access);
			accessList.add(access);
		}
		return accessList; 
	}
	
	private void exctractLists() {
		if(listAccess!=null){
			for(Access a: listAccess){//for each access
				for (int i=1; i<=5 ; i++) {// for each threat
					for (String  data : a.getData()) {//for each data of an access 
						if(THREAT_PERMISSIONS.get(i).contains(data)){
							ArrayList<String> tab = new ArrayList<String>();
							tab.add(data);
							a.setData(tab);
							Threat_ListAccess.get(i-1).add(a);
						}
					}
				}
			}
		}
	}
	
	
	//INITIALIZATION METHODS
	
    public void initializeThreatAccess (){
		for(int i=1; i<=5;i++){
			Threat_ListAccess.add(new ArrayList<Access>());  
		}
    }
	
    private static void addData(String data, ArrayList<Integer> arrayList) {
        PERMISSION_IMPACT.put(data, arrayList);
    }
    
	private static void addDataToThreat(Integer threatNumber, ArrayList arrayList) {
		THREAT_PERMISSIONS.put(threatNumber, arrayList);
	}
	
	
	//TESTING METHODS
	
	public void displayStringList(List<String> listStrings ){
		for(String string :listStrings){
			Log.i("String ", string+"/n");
		}
	}
	
	public void displayAccess(String tag, Access a){
		Log.i(tag," Time:" + a.getTime()+"| App:"+a.getApp()+"| data:"+a.getDataString()+"| ip:"+a.getIp()+"| active:"+a.getActive()+"| extra:"+a.getExtra()); 
	}

	//SETTER AND GETTER
	
	public ArrayList<Access> getThreatList(int number){
		ArrayList<Access> list; 		
		return Threat_ListAccess.get(number-1);		
	}
	
	public ArrayList<Access> getListAccess() {
		return listAccess;
	}

	public void setListAccess(ArrayList<Access> listAccess) {
		this.listAccess = listAccess;
	}

	public int[] getRisk() {
		return risk;
	}

	public void setRisk(int[] risk) {
		this.risk = risk;
	}
}
