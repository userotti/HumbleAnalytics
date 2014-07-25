package com.example.humbleanalytics;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class AnalyticsApplication extends Application implements AsyncResponse {
	
	public static final String PREFS_NAME = "UserDetailsPrefsFile";
	
	SharedPreferences settings;
	
	String email_str, password_str;
    String address;
    NetworkThreadHandler handler;
    //JSONObject RECIEVED_APPDATA_JSON;
    
    JSONObject LOGIN_DATA_BOL;
    

    ArrayList<String> THE_SITE_GUIDS = new ArrayList<String>();
    ArrayList<String> THE_SITE_NAMES = new ArrayList<String>();
    
    ArrayList<JSONObject> THE_LASTEST_SITE_STATS = new ArrayList<JSONObject>();
    
    String current_site_name;
    String current_activity;
    int current_activity_number;
    
    AsyncResponse delegate;
    Boolean logged_in_user = false;
    String request_uri_kind = new String("login");
    int number_of_update_return_calls = 0;
    int number_of_autherized_sites = 0;
    
    public void onCreate()
	{
		super.onCreate();
		this.settings = getSharedPreferences(PREFS_NAME, 0);
		
		//this.clearPrefDetails();
		
	}
     
    public boolean hasCurrentUser(){
    	
    	this.email_str = this.settings.getString("email", "niks");
		this.password_str = this.settings.getString("password", "niks");
		
		if (this.email_str.equals("niks") & this.password_str.equals("niks")){
			return false;		
		}else{
			return true;
		}
		
    }
    
    public void makeNewActivity(String type){
    	
    	if (type.equals("login")){
    		
    		Intent intent = new Intent(this, Login.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		intent.putExtra("DATA_STRING", "niks vir nou");
    		startActivity(intent);
    	
    	}
    	
    	if (type.equals("main")){
    	
    		Intent intent = new Intent(this, MainActivity.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		intent.putExtra("DATA_STRING", "niks vir nou");
    		startActivity(intent);
    	
    	}
    	
    
    }
    
   
    
    public void makeNewSiteActivity(String type, int site_num, boolean update_data){
    	
    	
    	if (type.equals("dashboard")){
        	
    		
    		this.current_activity_number = site_num; 
    		String data_string = this.THE_LASTEST_SITE_STATS.get(site_num).toString();
    		
    		Log.d("DATASTRING", data_string);
    		
    		Intent intent = new Intent(this, DashBoard.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		intent.putExtra( "DATA_STRING" , data_string);
    		startActivity(intent);
    	
    	}
    	
    	
    }
	
    //This function handles the async request calls comming back ASYNC!!
    
	public void processFinish(Object output){
		
		
		if (this.request_uri_kind.equals("login")){
			String return_string = "Login Error";
			try {
				this.LOGIN_DATA_BOL = (JSONObject) output;
				
				if (this.LOGIN_DATA_BOL.getString("data") == null){
					return_string = this.LOGIN_DATA_BOL.getString("message");
					throw new Exception();
				}
				
				JSONArray thesitesarray = new JSONArray(this.LOGIN_DATA_BOL.getString("data"));
				
				if (thesitesarray.length() == 0){
					return_string = "No sites found for this user: " + this.email_str;
					throw new Exception();
					
				}else{
				
					int i;
					this.THE_SITE_GUIDS.clear();
					this.THE_SITE_NAMES.clear();
					
					for (i = 0; i < thesitesarray.length(); i++){
						JSONObject theelement = (JSONObject)thesitesarray.get(i);
						this.THE_SITE_GUIDS.add(theelement.get("siteguid").toString());
						this.THE_SITE_NAMES.add(theelement.get("sitename").toString());
						
					}
					
					Log.d("LOGINBOL", this.LOGIN_DATA_BOL.toString());
					
					this.updateAllSites();
					return;
				}
				
				 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				this.delegate.processFinish(return_string);
				e.printStackTrace();
			}
			
		}
		
		//UPDATE HANDLE
		if (this.request_uri_kind.equals("login_update")){
			
			this.number_of_update_return_calls += 1;
			JSONObject site_data = (JSONObject) output;
			
			try {
				if (site_data.getString("message").equals("OK")){
					this.THE_LASTEST_SITE_STATS.add(site_data);
					Log.d("YARON", "IS ORAAIT");
					this.number_of_autherized_sites += 1;
					
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Log.d("SITE_DATA", site_data.toString());
			
			if (this.number_of_update_return_calls == this.THE_SITE_GUIDS.size()){

				try {
					
					if (this.number_of_autherized_sites > 0){
					
						this.setPrefDetails();
						this.delegate.processFinish(this.LOGIN_DATA_BOL.getString("message"));
						
						
					}else{
						
						this.delegate.processFinish("User not aurtherized for any of the sites.");
						
					}
					
					Log.d("NOU", "KANT JY MAR DIE DELEGATE HANDLE");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.number_of_update_return_calls = 0;
				
			}
		
		}
		
	}
	
	public CharSequence[] getSiteNames(){
		
		return this.THE_SITE_NAMES.toArray(new CharSequence[this.THE_SITE_NAMES.size()]);

	}
	
	public int getSitesArrayLength(){
		
		Log.d("in AnalyticsClass", ""+ this.THE_LASTEST_SITE_STATS.size());
		return this.THE_LASTEST_SITE_STATS.size();
		
	}
	
	public int getCurrentSiteNum(){
		
		return this.current_activity_number;
		
	}
	
	private void setPrefDetails(){
		
		this.settings.edit().putString("email", this.email_str).commit();
		this.settings.edit().putString("password", this.password_str).commit();
		
	}
	
	public void clearPrefDetails(){
		
		this.settings.edit().putString("email", "niks").commit();
		this.settings.edit().putString("password", "niks").commit();
		
		Log.d("CLEAR HIERIE KAK", "ASB");
		
		
	}
	public void updateAllSites(){
		
		this.THE_LASTEST_SITE_STATS.clear();
		this.number_of_autherized_sites = 0;
		
		
		int i;
		
		for (i = 0; i < this.THE_SITE_GUIDS.size(); i++){
			
			this.makeNewAsyncTaskForUpdate(this.THE_SITE_GUIDS.get(i));
			this.shootTheRequest();
			
		}
		
	}
	
	public String getUserEmail(){
		
		return(this.email_str);
		
	}
		
	public void setDelegate(AsyncResponse activity){
		
		this.delegate = activity;
		
	}
	
	public void setEmailAndPassword(String email, String pass){
		
		this.email_str = email;
		this.password_str = pass;
		
	}
	
	
	public void printGuids(){
		
		int i;
		
		for (i = 0; i < this.THE_SITE_GUIDS.size(); i++){
			
			Log.d("wen", this.THE_SITE_GUIDS.get(i));
		
		}
		
	}
	
	public void shootTheRequest(){
		
		try{
			
			this.handler.setRequestType("get");
			this.handler.setTheAuthHeaderValues(this.email_str, this.password_str);
			this.handler.prepForExecution();
			this.handler.execute( this );   
        
        }catch(Exception e){
        	
        	Log.d("GET REQUES IN APP", "issue with the network");
        	
        	if (this.delegate != null){
        		
        		JSONObject	error =	new JSONObject();
        		try {
					error.put("message", "There seems to be a problem with our server.");
					this.delegate.processFinish(error);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
        		
        	}
        	e.printStackTrace();
        }
		
	}
	
	public void makeNewAsyncTaskForLogin(){
			
			//this.handler = null;
			this.handler = new NetworkThreadHandler();
			this.handler.delegate = this;
			this.handler.setUri("checkuser");
			this.request_uri_kind = "login";
			
			Log.d("new please", "newplease");
		
	}
	
	public void makeNewAsyncTaskForUpdate(String guid_uri){
		
		//this.handler = null;
		this.handler = new NetworkThreadHandler();
		this.handler.delegate = this;
		this.handler.setUri(guid_uri);
		this.request_uri_kind = "login_update";
		
		Log.d("NEW ASYNC", "login_update");
	
	}
	
}





/*
OOONCE I WAAAS A VERY YOUNG MAN, and very young men are non to clever.
SAAAILED ACROSS TO FAR AWAY LANDS, and far away towns of tin and terror.
...

https://www.youtube.com/watch?v=Di5AT4MI6BY
*/