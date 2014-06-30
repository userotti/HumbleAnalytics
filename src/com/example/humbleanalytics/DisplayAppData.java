package com.example.humbleanalytics;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.humbleanalytics.MainActivity.PlaceholderFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class DisplayAppData extends ActionBarActivity {
	
	JSONObject ALLAPPDATA;
	JSONObject APPDATA;
	
	TextView gross_profit_textview;
	
	TextView turnover_textview;
	
	
	TextView today_sales_textview;
	TextView transaction_ave_textview;
	TextView transactions_comp_textview;
	
	TextView transactions_cash_textview;
	TextView transactions_card_textview;
	TextView transactions_snapscan_textview;
	
	TextView cost_value_stock_onhand;
	TextView retail_value_stock_onhand;
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_app_data);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		Log.d("en", "nou");
		
	}
	
	@Override 
	public void onResume(){
		super.onResume();
		
		this.setFonts();
		
		
	    
		try {
			this.ALLAPPDATA = new JSONObject(getIntent().getStringExtra("DATA_STRING"));
			this.APPDATA = new JSONObject(this.ALLAPPDATA.getString("data"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    try {
			textView.setText(this.ALLAPPDATA.getString("message"));
			
			Log.d("OUTPUT JSON", this.ALLAPPDATA.toString());
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	    
	    
	    
	    
	    try {
			this.setTopRowValues();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_app_data, menu);
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
	
	public void setTopRowValues() throws JSONException{
		
		this.gross_profit_textview =  (TextView)findViewById(R.id.gross_profit_view);
		
		this.turnover_textview = (TextView)findViewById(R.id.turnover_view);
		
		
		this.today_sales_textview = (TextView)findViewById(R.id.today_sales_view);
		this.transaction_ave_textview = (TextView)findViewById(R.id.transaction_ave_view);
		this.transactions_comp_textview = (TextView)findViewById(R.id.transactions_comp_view);
		
		this.transactions_cash_textview = (TextView)findViewById(R.id.transactions_cash_view);
		this.transactions_card_textview = (TextView)findViewById(R.id.transactions_card_view);
		this.transactions_snapscan_textview = (TextView)findViewById(R.id.transactions_snapscan_view);
		
		this.cost_value_stock_onhand = (TextView)findViewById(R.id.cost_value_stock_onhand_view);
		this.retail_value_stock_onhand = (TextView)findViewById(R.id.retail_value_stock_onhand_view);
		
		
		
		
		//set them
		
		this.gross_profit_textview.setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("mtd_gpvalue")));
		
		this.turnover_textview.setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("mtd_turnovervalue")));
		
		this.today_sales_textview.setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_sales")));
		this.transaction_ave_textview.setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_tranaverage")));
		this.transactions_comp_textview.setText(""+(this.APPDATA.getString("today_transactions")));
		
		this.transactions_cash_textview.setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_cash")));
		this.transactions_card_textview.setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_card")));
		this.transactions_snapscan_textview.setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_snapscan")));
		
		this.cost_value_stock_onhand.setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_cost")));	
		this.retail_value_stock_onhand.setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_retail")));
		
		
		
		/*siteguid'            => $siteguid,
			'message'             => 'Not OK',
			'updated'             => date("Y-m-d"),
			'mtd_gpvalue'         => 0,
			'mtd_gpperc'          => 0,
			'mtd_turnovervalue'   => 0,
			'today_sales'         => 0,
			'today_tranaverage'   => 0,
			'today_transactions'  => 0,
			'today_cash'          => 0,
			'today_card'         => 0,
			'today_snapscan'      => 0,
			'today_change'        => 0,
			'stock_cost'          => 0,
			'stock_retail'        => 0,
			'tradingdensity_sale' => 0,
			'tradingdensity_gp'   => 0,
			'top_products'        => array(),
			'top_staff'           => array(),
		);*/

		
	}
	
	public void setFonts(){
		
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Helvetica-Bold.ttf");
	   
		TextView myTextView = (TextView)findViewById(R.id.gross_profit_heading);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.turnover_heading);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.today_sales_heading);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.transactions_cash_heading);
	    myTextView.setTypeface(myTypeface);
	    
	    
	    
	    myTextView = (TextView)findViewById(R.id.bottomheading1);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.bottomheading2);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.bottomheading3);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.bottomheading4);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.bottomheading5);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.bottomheading6);
	    myTextView.setTypeface(myTypeface);
	    
	    
	    
	    
	    
	    myTextView = (TextView)findViewById(R.id.subheading1);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading2);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading3);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading4);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading5);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading6);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading7);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading8);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading9);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading10);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading11);
	    myTextView.setTypeface(myTypeface);
	    
	    myTextView = (TextView)findViewById(R.id.subheading12);
	    myTextView.setTypeface(myTypeface);
	    
	    
	    
		
	}
	
	public String makeStingIntoPriceValue(String thestring){
		
		return thestring;
		
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
			View rootView = inflater.inflate(
					R.layout.fragment_display_app_data, container, false);
			return rootView;
		}
	}

}
