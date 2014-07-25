package com.example.humbleanalytics;

import com.humbletill.humbleanalytics.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public  class SitesDialogFragment extends DialogFragment {
    
	CharSequence[] items = {"India", "US", "UK", "Australia"};
	
	int answer;
	DashBoard con;
	
	
	
	DialogInterface.OnClickListener theanswer_handler = new DialogInterface.OnClickListener() {
		   
		   public void onClick(DialogInterface dialog, int item) {
			   con.setSiteChoice(item);
		   }
		   
	};
 
	public void setContext(Context con){
		
		this.con = (DashBoard) con;
		
	}
	   
	public void setItems(CharSequence[] newitems) {
		
		
	};
	
	
	@Override 
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setTitle("Choose a site")
        
        	   .setCancelable(true)	
        	   .setIcon(R.drawable.ic_menu_home)
			   .setItems(((AnalyticsApplication) con.getApplication()).getSiteNames(), theanswer_handler);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}