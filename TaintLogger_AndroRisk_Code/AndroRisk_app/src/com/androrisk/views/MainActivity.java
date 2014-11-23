package com.androrisk.views;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.androrisk.controllers.Extractor;
import com.androrisk.controllers.ThreatListArrayAdapter;
import com.androrisk.models.PersistanceSingleton;
import com.example.androrisk.R;


public class MainActivity extends ListActivity  {
    private String[] arrayThreats;
    private int[] risks; 
    
    private PersistanceSingleton persistanceSingleton = PersistanceSingleton.getInstance();
       
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Extractor extractor = new Extractor();
		persistanceSingleton.setExtractor(extractor);
		
        // For the cursor adapter, specify which columns go into which views
	    arrayThreats = new String[] { "Tracking (T1)", "Eavesdropping (T2)", "Profiling (T3)", "Phishing (T4)", "Perso. Info. Disclosure (T5)"};
        risks = extractor.getRisk();
	    
	    ThreatListArrayAdapter adapter = new ThreatListArrayAdapter (this,
	    		arrayThreats, risks);
		setListAdapter(adapter);
    }


    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
//    	List<Access> list = extractor.getThreatList(position+1); 

    	if(position==0){
		    Intent intent = new Intent(this, ThreatDetailedActivity.class);
		    StringBuilder sb = new StringBuilder();
		    sb.append("");
		    sb.append(position+1);
		    String numberS = sb.toString();
		    intent.putExtra("THREAT_NUMBER", numberS );
		    startActivity(intent);
    	}
    	else{
        	Toast.makeText(getApplicationContext(),
        	  	      "Sorry, Threat " + position + "is in development", Toast.LENGTH_SHORT)
        	  	      .show();
    	}
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}

