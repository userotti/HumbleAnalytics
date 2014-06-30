package com.example.humbleanalytics;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;



import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity implements AsyncResponse {
	
	
	Button submit_btn;

    EditText email_edit, password_edit;
    String email_str, password_str;
    
    String address;
    NetworkThreadHandler handler;
    
    JSONObject RECIEVED_APPDATA_JSON;
    Boolean task_is_running = false;
    ProgressBar loading_circle;
    TextView login_message;
    
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
		this.makeNewAsyncTask();
		
		
		
	}
	
	protected void onResume(){
		super.onResume();
		
		EditText e = (EditText) findViewById(R.id.email_textedit);
		
		e.requestFocus();
		
		
	}
	
	private void makeNewAsyncTask(){
		
		handler = null;
		handler = new NetworkThreadHandler();
		handler.delegate = this;
		
		this.task_is_running = false; 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void processFinish(Object output){
		
		 this.RECIEVED_APPDATA_JSON = (JSONObject) output;
		 
		 this.login_message = (TextView) findViewById(R.id.output_view);
		 this.login_message.setTextColor(Color.parseColor("#ffffff"));
		  
		 try {
			 if (this.RECIEVED_APPDATA_JSON.getString("message").equals("OK")){
				 
				 this.login_message.setText("Login Successful.");
				 
			 }else{
				 
				 this.login_message.setText(this.RECIEVED_APPDATA_JSON.getString("message"));
				 
			 }
				 
     	 } catch (JSONException e) { 
			e.printStackTrace();
		 }
	     
	     try {
			 
	    	 //Make the new Activity
	    	 if (this.RECIEVED_APPDATA_JSON.getString("message").equals("OK")){
				  	
				Intent intent = new Intent(this, DisplayAppData.class);
				intent.putExtra("DATA_STRING", this.RECIEVED_APPDATA_JSON.toString());
				startActivity(intent);
			
			 }
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	     
	     
	     this.makeNewAsyncTask();
	     this.loading_circle.setVisibility(View.GONE);
		
	}
	
	/* This is the submit button onClick function */

	public void clickFunc1(View view){
		
        if (this.task_is_running == false){
        
        	this.loading_circle = (ProgressBar) findViewById(R.id.loading_animation);
        	this.loading_circle.setVisibility(View.VISIBLE);
        	
        	this.login_message = (TextView) findViewById(R.id.output_view);
        	this.login_message.setText("");
        	
			email_edit = (EditText)findViewById(R.id.email_textedit);
	        password_edit = (EditText)findViewById(R.id.password_textedit);
	                
	        email_str = email_edit.getText().toString();
	        password_str = password_edit.getText().toString();
	    
	        
	        try{
	        	handler.setRequestType("get");
	        	handler.setTheAuthHeaderValues(email_str, password_str);
	        	handler.prepForExecution();
	        	handler.execute( this );   
	        	this.task_is_running = true;
	        
	        }catch(Exception e){
	        	
	        	Log.d("GET REQUEST", "issue with the network");
	        	e.printStackTrace();
	        }
        
        }
	}

	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
