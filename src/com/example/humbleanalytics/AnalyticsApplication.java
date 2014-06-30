package com.example.humbleanalytics;

import org.json.JSONObject;

import android.app.Application;

public class AnalyticsApplication extends Application implements AsyncResponse {
	
	String email_str, password_str;
    String address;
    NetworkThreadHandler handler;
    JSONObject RECIEVED_APPDATA_JSON;
    
    
    public void onCreate()
	{
		super.onCreate();
		
		// Initialize the singletons so their instances
		// are bound to the application process.
		//initSingletons();
	}
	
	public void processFinish(Object output){
		
	}
	
	private void makeNewAsyncTask(){
		
			handler = null;
			handler = new NetworkThreadHandler();
			handler.delegate = this;
		
	}
	
}





/*
OOONCE I WAAAS A VERY YOUNG MAN, and very young men are non to clever.
SAAAILED ACROSS TO FAR AWAY LANDS, and far away towns of tin and terror.
...

https://www.youtube.com/watch?v=Di5AT4MI6BY
*/