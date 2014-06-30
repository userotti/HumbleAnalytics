package com.example.humbleanalytics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;


public class NetworkThreadHandler extends AsyncTask {
	
	
	
    HttpClient client;
    HttpResponse response;
    
    HttpGet get_req;  
    HttpPost post_req;

    String request_type;
   
    String the_email;
    String the_pass;
    String the_site_guid;
    
    String finalURL;
    
    String baseURL = "http://api.humble.co.za/1.2/analytics/";
    String APPDATA;
    JSONObject APPDATA_JSON;
    
    
    AsyncResponse delegate=null;
    
    
    
	public NetworkThreadHandler()
    {
		this.client = new DefaultHttpClient();	
		
		// this is stil hardcoded
		this.setSiteGuid("0C8213F4-E2CC-4396-9F70E74E2A6B978B");
		
    }
	
	
	//STEP 1 very important
	
	
	public void setRequestType(String type){
		
		
		if (type == "get"){
			
			
			this.request_type = new String("get"); 
		
			
		}
		
		if (type == "post"){
			
			this.request_type = new String("post"); 
			
			
		}
		
		
	}
	
	//STEP 2 for the GET request 
	
	public void setTheAuthHeaderValues(String email, String pass)
    {
		this.the_email = email;
		this.the_pass = pass;
		
    }
	
	//Step 3 setup the whole thing
	
	public void prepForExecution(){
		
		if (this.request_type.equals("get")){
			if (this.the_site_guid != null){
					this.buildFinalSiteGuidUrl();
					this.get_req = new HttpGet(this.finalURL);
					
					
					
				}else{
					Log.d("SITEGUID", "site guid is null");
				}	
			}
		
		if (this.request_type.equals("post")){
				this.post_req = new HttpPost(this.finalURL);
		}
	}
	
	
	
	private void setSiteGuid(String site_guid){
		
		this.the_site_guid = site_guid;
		
	}
	
	private void buildFinalSiteGuidUrl(){
		
		this.setFinalUrl(this.baseURL + this.the_site_guid);
		
	}
	
	private void setFinalUrl(String address){
		
		this.finalURL = address;
		
	}
	
	
	
	private void doTheAuthRequest(){
		
		final String emailpass = this.the_email + ":" + this.the_pass; 
	    final String basicAuth = "Basic " + Base64.encodeToString(emailpass.getBytes(), Base64.NO_WRAP);
	        
	    this.get_req.setHeader("Authorization", basicAuth);
	        
	    try{
	       
	       this.response = this.client.execute(this.get_req);
	      
	       
	       	    
		}
		catch(Exception ex)
		{
			
		   Log.e("MYAPP_check2_execute", "exception" + ex);
		}
	    
	    
	    try{
	       
	       BufferedReader rd = new BufferedReader(new InputStreamReader(this.response.getEntity().getContent()));
	       
	       
	       String line = "";
	            while ((line = rd.readLine()) != null) {
	              
	              this.APPDATA = line; 
	            }
	            
	            this.APPDATA_JSON = new JSONObject(this.APPDATA);
	            //Log.d("this.APPDATA message",  this.APPDATA_JSON.getString("message"));
	            
		}
		catch(Exception ex)
		{
			Log.e("MYAPP_BufferReader", "exception", ex);
		}
		
	}
	
	
	@Override
	protected Object doInBackground(Object... params) {
		
		this.doTheAuthRequest();
		
       
				
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		
	    delegate.processFinish(this.APPDATA_JSON);
	}
	
}


