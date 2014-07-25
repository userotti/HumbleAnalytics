package com.example.humbleanalytics;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

import com.humbletill.humbleanalytics.R;

public class DashBoard extends Activity implements AsyncResponse {
	
	JSONObject ALLAPPDATA;
	JSONObject APPDATA;
	
	AnalyticsApplication THEAPP;
	
	SitesDialogFragment thedialog;
	
	int siteNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		this.THEAPP = ((AnalyticsApplication) this.getApplication());
		
		
		
		thedialog = new SitesDialogFragment();
		
		thedialog.setContext(this);
		
		
		
		
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dash_board, menu);
		return true;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		//this.setFonts();
		
		try {
			this.ALLAPPDATA = new JSONObject(getIntent().getStringExtra("DATA_STRING"));
			this.APPDATA = new JSONObject(this.ALLAPPDATA.getString("data"));
			setTitle(this.APPDATA.getString("sitename") + " - Dashboard");
			//setTitle("Humble Test Store - Dashboard");
			
			this.setAllValues();
			
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.refresh_settings) {
			
			setTitle("Loading new data...");
			
			this.THEAPP.setDelegate(this);
			this.THEAPP.makeNewAsyncTaskForLogin();
			this.THEAPP.shootTheRequest();
			
		}
		
		
		
		if (id == R.id.stores) {
			
			
			this.thedialog.show(getFragmentManager(), "missiles");
			
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void setSiteChoice(int sitechoice){
		
		this.siteNumber = sitechoice;
		
		setTitle("Loading new data...");
		
		this.THEAPP.setDelegate(this);
		this.THEAPP.makeNewAsyncTaskForLogin();
		this.THEAPP.shootTheRequest();
		
	}
	
	public void processFinish(Object message){
		
		if (message.equals("OK")){
			 
			 this.THEAPP.makeNewSiteActivity("dashboard", this.siteNumber, true);
			 finish();
		 
		 }else{
			 
			 Log.d("fop", "groot fop");
			 
		 };
	}
	
	public void setAllValues() throws JSONException{
		
		int comma_pos = (this.APPDATA.getString("mtd_gpperc").indexOf('.'));
		((TextView)findViewById(R.id.gp_perc_view)).setText((this.APPDATA.getString("mtd_gpperc")).substring(0,comma_pos+2) + "%");
		((TextView)findViewById(R.id.gp_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("mtd_gpvalue")));
		((TextView)findViewById(R.id.turnover_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("mtd_turnovervalue")));
		
		((TextView)findViewById(R.id.todays_sales_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_sales")));
		((TextView)findViewById(R.id.todays_tax_view)).setText("R 0.00");
		comma_pos = (this.APPDATA.getString("today_tranaverage").indexOf('.'));
		if (comma_pos != -1 & comma_pos + 3 <= this.APPDATA.getString("today_tranaverage").length() )
		{
			((TextView)findViewById(R.id.todays_sales_ave_trans_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_tranaverage").substring(0,comma_pos+3)));
		}	
		else
		{
			((TextView)findViewById(R.id.todays_sales_ave_trans_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_tranaverage")));
		}
		((TextView)findViewById(R.id.todays_sales_trans_comp_view)).setText(this.makeStingIntoPriceValue(this.APPDATA.getString("today_transactions")));
		
	
		
		((TextView)findViewById(R.id.todays_trans_cash_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_cash")));
		((TextView)findViewById(R.id.todays_trans_card_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_card")));
		((TextView)findViewById(R.id.todays_trans_snapscan_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_snapscan")));
		((TextView)findViewById(R.id.todays_trans_change_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("today_change")));
		
		
		
		((TextView)findViewById(R.id.stock_on_hand_cost_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_cost")));
		((TextView)findViewById(R.id.stock_on_hand_retail_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_retail")));
		
		
		JSONArray top_staff = this.APPDATA.getJSONArray("top_staff");
		JSONArray top_products = this.APPDATA.getJSONArray("top_products");
		JSONArray top_categories = this.APPDATA.getJSONArray("top_categories");
		
		
		int i,j;
		
		
		for(i = 1; i <= top_staff.length(); i++ ){
			int resId = getResources().getIdentifier("top_sales_person" + i, "id", getPackageName());
			((TextView)findViewById(resId)).setText(this.makeStingIntoPriceValue((top_staff.getJSONObject(i-1).getString("agent"))));
			resId = getResources().getIdentifier("top_sales_person_amount" + i, "id", getPackageName());
			((TextView)findViewById(resId)).setText("R " + this.makeStingIntoPriceValue((top_staff.getJSONObject(i-1).getString("gp"))) + " GP MTD"   );
		}
	
	
		for(j = 1; j <= top_products.length(); j++ ){
			int resId = getResources().getIdentifier("top_selling_product" + j, "id", getPackageName());
			((TextView)findViewById(resId)).setText(this.makeStingIntoPriceValue((top_products.getJSONObject(j-1).getString("descr"))));
			resId = getResources().getIdentifier("top_selling_product_amount" + j, "id", getPackageName());
			((TextView)findViewById(resId)).setText(this.makeStingIntoPriceValue((top_products.getJSONObject(j-1).getString("qty"))) + " Sold MTD"   );
		}
		
		
		((TextView)findViewById(R.id.sales_persqrmtr_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("tradingdensity_sale")));
		((TextView)findViewById(R.id.gp_persqrmtr_view)).setText("R " + this.makeStingIntoPriceValue(this.APPDATA.getString("tradingdensity_gp")));
		
		
		for(j = 1; j <= top_categories.length(); j++ ){
			
			int resId = getResources().getIdentifier("top_selling_cat" + j, "id", getPackageName());
			((TextView)findViewById(resId)).setText(this.makeStingIntoPriceValue((top_categories.getJSONObject(j-1).getString("category"))));
			resId = getResources().getIdentifier("top_selling_cat_amount" + j, "id", getPackageName());
			((TextView)findViewById(resId)).setText("R " + this.makeStingIntoPriceValue((top_categories.getJSONObject(j-1).getString("gp"))) + " GP MTD"   );
		}
		
		new DownloadImageTask((ImageView) findViewById(R.id.weather_image)).execute(this.APPDATA.getString("weather_url"));
		
		comma_pos = (this.APPDATA.getString("weather_tempc").indexOf('.'));
		((TextView)findViewById(R.id.weather_tempc_view)).setText(this.APPDATA.getString("weather_tempc").substring(0,comma_pos));
		
		
		((TextView)findViewById(R.id.weather_descr_view)).setText(this.makeStingIntoPriceValue(this.APPDATA.getString("weather_descr")));
		
		
			
		  
		/*
		((TextView)findViewById(R.id.top_sales_person2)).setText("2. " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_retail")));
		((TextView)findViewById(R.id.top_sales_person3)).setText("3. " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_cost")));
		((TextView)findViewById(R.id.top_sales_person4)).setText("4. " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_retail")));
		((TextView)findViewById(R.id.top_sales_person5)).setText("5. " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_cost")));
		
			
		((TextView)findViewById(R.id.top_selling_product1)).setText("1. " + this.makeStingIntoPriceValue((top_staff.getJSONObject(0).getString("agent")));
		((TextView)findViewById(R.id.top_selling_product2)).setText("2. " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_retail")));
		((TextView)findViewById(R.id.top_selling_product3)).setText("3. " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_cost")));
		((TextView)findViewById(R.id.top_selling_product4)).setText("4. " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_retail")));
		((TextView)findViewById(R.id.top_selling_product5)).setText("5. " + this.makeStingIntoPriceValue(this.APPDATA.getString("stock_cost")));
	*/	
		
		
	
		/*
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
		*/
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

	public String makeStingIntoPriceValue(String thestring){
		
		return thestring;
		
	}
	
	public void setFonts(){
		
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Helvetica-Bold.ttf");
	 	
		((TextView)findViewById(R.id.gross_profit_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.turnover_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.today_sales_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.trans_cash_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.stock_on_hand_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.top_selling_prod_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.top_sales_person_heading)).setTypeface(myTypeface);
	   // ((TextView)findViewById(R.id.my_currencies_heading)).setTypeface(myTypeface);
		   
	    //((TextView)findViewById(R.id.gross_profit_view)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.turnover_view)).setTypeface(myTypeface);
	    
	   
	    
	    ((TextView)findViewById(R.id.todays_sales_view)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.todays_sales_ave_trans_view)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.todays_sales_trans_comp_view)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.todays_sales_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.todays_sales_ave_trans_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.todays_sales_trans_comp_heading)).setTypeface(myTypeface);
		
	    ((TextView)findViewById(R.id.todays_transactions_cash_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.todays_trans_cash_view)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.todays_transactions_card_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.todays_trans_card_view)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.todays_transactions_snapscan_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.todays_trans_snapscan_view)).setTypeface(myTypeface);
		
	    ((TextView)findViewById(R.id.stock_on_hand_retail_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.stock_on_hand_retail_view)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.stock_on_hand_cost_heading)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.stock_on_hand_cost_view)).setTypeface(myTypeface);
	    
	    
	    /*((TextView)findViewById(R.id.bottomheading1)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.bottomheading2)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.bottomheading3)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.bottomheading4)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.bottomheading5)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.bottomheading6)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading1)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading2)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading3)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading4)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading5)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading6)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading7)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading8)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading9)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading10)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading11)).setTypeface(myTypeface);
	    ((TextView)findViewById(R.id.subheading12)).setTypeface(myTypeface);
	 	*/
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
			View rootView = inflater.inflate(R.layout.fragment_dash_board_scrollview,
					container, false);
			return rootView;
		}
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		  ImageView bmImage;

		  public DownloadImageTask(ImageView bmImage) {
		      this.bmImage = bmImage;
		  }

		  protected Bitmap doInBackground(String... urls) {
		      String urldisplay = urls[0];
		      Bitmap mIcon11 = null;
		      try {
		        InputStream in = new java.net.URL(urldisplay).openStream();
		        mIcon11 = BitmapFactory.decodeStream(in);
		      } catch (Exception e) {
		          Log.e("Error", e.getMessage());
		          e.printStackTrace();
		      }
		      return mIcon11;
		  }

		  protected void onPostExecute(Bitmap result) {
		      bmImage.setImageBitmap(result);
		  }
		}
	
	
	

}



