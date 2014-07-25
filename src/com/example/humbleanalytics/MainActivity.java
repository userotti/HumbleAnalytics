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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Build;

import com.humbletill.humbleanalytics.R;


public class MainActivity extends ActionBarActivity implements AsyncResponse {
	
	AnalyticsApplication THEAPP;
	Boolean task_is_running;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		 this.task_is_running = false;  
		
		
		
	}
	
	public void processFinish(Object output_message){
		
		 String message = (String) output_message;
		 
		 TextView login_message = (TextView) findViewById(R.id.main_login_message);
		
		 if (message.equals("OK")){
			 
			 login_message.setText("Login Successful.");
			 this.THEAPP.makeNewSiteActivity("dashboard", 0, true);
		 
		 }else{
			 
			 login_message.setText(message);
			 
		 };
		 
		 
		 //leave it off
		 
		 this.task_is_running = false;
		 
		 
		
	}
	
	protected void onResume(){
		super.onResume();
		
		
		/*RelativeLayout layout =(RelativeLayout)findViewById(R.id.main_layout);
		layout.setBackgroundResource(R.drawable.ready);
		
		*/
		
		((TextView) findViewById(R.id.main_login_message)).setText("");
		
        if (this.THEAPP.hasCurrentUser() == true){
        	
        	String str = this.THEAPP.getUserEmail();
        	
        
        	Button newbtn = (Button) findViewById(R.id.continueBtn);
        	newbtn.setVisibility(View.VISIBLE);
        	newbtn.setText(str);
        	
        	
        }else{
        	
        	Button newbtn = (Button) findViewById(R.id.continueBtn);
        	
        	newbtn.setVisibility(View.GONE);
        	
    		
        }
        
	}
	
	public void continueButton(View view){
		
		if (this.task_is_running == false){
			
		/*this.set.start();
		ImageView img = (ImageView) findViewById(R.id.loadingsquareID);
		img.setVisibility(View.VISIBLE); */
		
		//this.THEAPP.BootStrapStart();
		
		this.THEAPP.setDelegate(this);
		this.THEAPP.makeNewAsyncTaskForLogin();
		this.THEAPP.shootTheRequest();
		
		this.task_is_running = true;
		
		}
	}
	
	public void loginButton(View view){
		
		this.THEAPP.makeNewActivity("login");
	
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
