package com.androrisk.views;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.androrisk.controllers.Extractor;
import com.androrisk.controllers.ThreatDetailedArrayAdapter;
import com.androrisk.controllers.ThreatListArrayAdapter;
import com.androrisk.models.Access;
import com.androrisk.models.PersistanceSingleton;
import com.example.androrisk.R;


public class ThreatDetailedActivity extends ListActivity{
	 private ArrayList<Access> list; 
	 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	Extractor extractor = new Extractor();
	//	PersistanceSingleton.getInstance().setExtractor(extractor);
        Bundle extra = getIntent().getExtras();
        String numberS = extra.getString("THREAT_NUMBER");
        Log.i("numberS", numberS);
        int number = Integer.parseInt(numberS); 
       
		list = PersistanceSingleton.getInstance().getExtractor().getThreatList(number);
//        String[] tab = new String [] {"toto","tata"};
	    
	    ThreatDetailedArrayAdapter adapter = new ThreatDetailedArrayAdapter (this,list);
		setListAdapter(adapter);
    }
	
    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
//    	List<Access> list = extractor.getThreatList(position+1); 
    	Toast.makeText(getApplicationContext(),
  	      "IP : " + list.get(position).getIp()+ " Extra : " + list.get(position).getExtra(), Toast.LENGTH_SHORT)
  	      .show();

    }

}
