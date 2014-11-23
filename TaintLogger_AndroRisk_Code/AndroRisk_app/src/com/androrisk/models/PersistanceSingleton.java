package com.androrisk.models;

import java.util.List;

import android.util.Log;

import com.androrisk.controllers.Extractor;


public class PersistanceSingleton {
	
    private static PersistanceSingleton mInstance = null;
    private Extractor extractor; 

 
    private PersistanceSingleton(){
    }
 
    public static PersistanceSingleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new PersistanceSingleton();
        }
        return mInstance;
    }

	public static PersistanceSingleton getmInstance() {
		return mInstance;
	}

	public Extractor getExtractor() {
		return extractor;
	}

	public void setExtractor(Extractor extractor) {
		this.extractor = extractor;
	}

	
}
