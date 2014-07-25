package com.example.humbleanalytics;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Build;

import com.humbletill.humbleanalytics.R;

public class Login extends ActionBarActivity implements AsyncResponse {
	
	
	Button submit_btn;

    EditText email_edit, password_edit;
    String email_str, password_str;
    
    String address;
    NetworkThreadHandler handler;
    
    JSONObject ALL_THE_SITEGUIDS;
    Boolean task_is_running = false;
    ImageView loading_square;
    TextView login_message;
    
    AnimatorSet set;
    
    AnalyticsApplication THEAPP; 
	
    
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		final TypedArray styledAttributes = getApplicationContext().getTheme().obtainStyledAttributes(
		                new int[] { android.R.attr.actionBarSize });
		int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();
		final float scale = this.getResources().getDisplayMetrics().density;
		
		BackgroundView backgroundview1 = new BackgroundView(this, metrics.widthPixels, metrics.heightPixels-mActionBarSize);
		
		FrameLayout therooview =  (FrameLayout)findViewById(R.id.container);
		therooview.addView(backgroundview1);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		this.THEAPP = ((AnalyticsApplication) this.getApplication());
	
	}
	
	protected void onResume(){
		super.onResume();
		
		EditText e = (EditText) findViewById(R.id.email_textedit2);
		
		e.requestFocus();
	
		
		/* LOADING ANIMATION SETUP 
		
		View v = findViewById(R.id.loading_animation);
	    ObjectAnimator animation1 = ObjectAnimator.ofFloat(v, "rotationY", 0.0f, 1080f);
	    ObjectAnimator animation2 = ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 0.0f);
	    
		animation1.setRepeatCount(ObjectAnimator.INFINITE);
		animation1.setInterpolator(new AccelerateDecelerateInterpolator());
		
		animation2.setRepeatCount(ObjectAnimator.INFINITE);
		animation2.setInterpolator(new AccelerateDecelerateInterpolator());
		
		v.setVisibility(View.GONE);
		
		this.set = new AnimatorSet();
		this.set.play(animation1).with(animation2);
		this.set.setDuration(2500);*/
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	public void processFinish(Object output_message){
		
		 String message = (String) output_message;
		 this.login_message = (TextView) findViewById(R.id.output_view);
		 this.login_message.setTextColor(Color.parseColor("#ffffff"));
		  
		
		 if (message.equals("OK")){
			 
			 this.login_message.setText("Login Successful.");
			 this.THEAPP.makeNewSiteActivity("dashboard" , 0, true);
			 
		 }else{
			 
			 this.login_message.setText(message);
			 
		 };
		 
		
		 this.task_is_running = false;
		
	}
	
	/* This is the submit button onClick function */

	public void clickFunc1(View view){
		
        if (this.task_is_running == false){
        
        	/*
        	this.loading_square = (ImageView) findViewById(R.id.loading_animation);
        	this.loading_square.setVisibility(View.VISIBLE);
        	this.set.start();
			*/
        	
        	this.login_message = (TextView) findViewById(R.id.output_view);
        	this.login_message.setText("");
        	
			email_edit = (EditText)findViewById(R.id.email_textedit2);
	        password_edit = (EditText)findViewById(R.id.password_textedit);
	        
	        
	        
	        this.THEAPP.setEmailAndPassword(email_edit.getText().toString(),password_edit.getText().toString());
	        this.THEAPP.setDelegate(this);
	        this.THEAPP.makeNewAsyncTaskForLogin();
	        this.THEAPP.shootTheRequest();
	        
	        
       
	        this.task_is_running = true;
	        
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
			View rootView = inflater.inflate(R.layout.fragment_login, container,
					false);
			return rootView;
		}
	}

}
