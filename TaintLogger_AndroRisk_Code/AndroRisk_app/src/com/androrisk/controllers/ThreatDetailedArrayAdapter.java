package com.androrisk.controllers;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androrisk.models.Access;
import com.example.androrisk.R;

public class ThreatDetailedArrayAdapter extends ArrayAdapter<Access>  {
	  private final Context context;
	  private Access[] list;

	
	
	  public ThreatDetailedArrayAdapter(Context context, ArrayList<Access> list) {
		    super(context, R.layout.row_threat_detailed,list);
		    this.context = context;
		    if(list!=null){
		    	this.list = new Access[list.size()];
			    int i =0;
			    for (Access access : list) {
					this.list[i]=access;
					i++;
				}
		    }
	  }
	
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.row_threat_detailed, parent, false);
		    
		    Log.i("Adapter Detailed","getView");
		    //Data
		    TextView textData = (TextView) rowView.findViewById(R.id.access_data);
//		    String datasS = null;
//		    ArrayList<String> datas =list[position].getData();
//		    for (String data : datas) {
//				datasS.concat(data+ ", ");
//			}
		    textData.setText(list[position].getDataString());
		    
		   // App
		    TextView textApp = (TextView) rowView.findViewById(R.id.access_app);
		    textApp.setText(list[position].getApp());
		    
		    //Time
		    TextView textTime = (TextView) rowView.findViewById(R.id.access_time);
		    textTime.setText(list[position].getTime().toString());
		    
		    //Active
		    TextView textActive = (TextView) rowView.findViewById(R.id.access_active);
		    String activeS;
//		    if(list[position].getActive()) 
		    	activeS="Yes";
//		    else
//		    	activeS="No";
		    textActive.setText(activeS);
		    
		    return rowView;
	  }
} 