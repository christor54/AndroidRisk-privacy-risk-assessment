package com.androrisk.controllers;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androrisk.models.Access;
import com.example.androrisk.R;

public class ThreatListArrayAdapter extends ArrayAdapter<String> {
  private final Context context; 
  private final String[] values;
  private final int[] risks;
  
  public ThreatListArrayAdapter(Context context, String[] values, int[] risks) {
	    super(context, R.layout.row_threat, values);
	    this.context = context;
	    this.values = values;
	    this.risks=risks; 
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.row_threat, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.threat_name);
	    textView.setText(values[position]);
	    TextView textView_risk = (TextView) rowView.findViewById(R.id.threat_risk_assessment);
	    int risk = risks[position];
	    StringBuilder sb = new StringBuilder();
	    sb.append("");
	    sb.append(risk);
	    String riskS = sb.toString();
	    textView_risk.setText(riskS);
	    return rowView;
  }
} 