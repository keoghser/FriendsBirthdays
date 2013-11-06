package com.friendsbirthdaysVer6;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	//DatePickerDialog dpd;
	int day;
	int month;
	int year;
	
/*	
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
      

        // Create a new instance of DatePickerDialog and return it
        dpd =  new DatePickerDialog(getActivity(), this, year, month, day);
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Add", dpd);
        dpd.setTitle( "Add Birthday" );
        
        dpd.setCanceledOnTouchOutside(true); 
        dpd.setCancelable(true);
        
      
        
        return dpd;
    }
    
    

    public void onDateSet(DatePicker view, int year, int month, int day) {    	
        // Do something with the date chosen by the user
    	  
    	//Toast.makeText(getActivity(),"Day picked is "+day, Toast.LENGTH_SHORT).show();
    	//Toast.makeText(getActivity(),"Month picked is "+month, Toast.LENGTH_SHORT).show();
    	//Toast.makeText(getActivity(),"Year picked is "+year, Toast.LENGTH_SHORT).show();
    	
    	Intent nextIntent = new Intent();    	   
    	nextIntent.setClass(getActivity(), EditScreen.class);
        nextIntent.putExtra("day", day);
        nextIntent.putExtra("month", month+1);
        nextIntent.putExtra("year", year);        
        startActivity(nextIntent);
    }
   
}*/






	//public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    //int year, month, day;
    DatePicker picker;

    public DatePickerFragment(){
    	
    	final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        //this.year = year;
       // this.month = month;
        //this.day = day;
        
    }

    
    
    protected DialogInterface.OnClickListener btn_cancel_listener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();               
        }
    };

    protected DialogInterface.OnClickListener btn_add_listener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            picker.clearFocus();
            onDateSet(picker, picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
        }
    };

   /* protected DialogInterface.OnClickListener btn_apply_listener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            picker.clearFocus();
            onDateSet(picker, picker.getYear(), picker.getMonth(), picker.getDayOfMonth());

            //SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
            //searchAsyncTask.execute();
        }
    };*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), null, this.year, this.month, this.day);
        datePickerDialog.setCancelable(true);
        datePickerDialog.setCanceledOnTouchOutside(true);
         
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", btn_cancel_listener);

        picker = datePickerDialog.getDatePicker();

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Add", btn_add_listener);

        //datePickerDialog.setButton3("Apply now", btn_apply_listener);

        datePickerDialog.setTitle("Add Birthday");
        return datePickerDialog;
    }



    public void onDateSet(DatePicker view, int currentYear, int monthOfYear, int dayOfMonth) {
        //String sToDate = createStringFromDateElements(year, monthOfYear, dayOfMonth);
    	 day = dayOfMonth;
    	 month = monthOfYear;
    	 year = currentYear;
    	
    	
    	 int [] idate = new int [3]; // create an integer array with 3 indices to hold day, month, year
         idate[0] = day; // set index 0 = int value of day
         idate[1] = month+1;
         idate[2] = year;
         
    	String dateDetails = EditScreen.FormatDate(idate); // get a formatted string version of date
       // References.orderlist_filters.setDateto(sToDate);
        Toast.makeText(getActivity(), dateDetails, Toast.LENGTH_SHORT).show();
        
        Intent nextIntent = new Intent();    	   
    	nextIntent.setClass(getActivity(), EditScreen.class);
        nextIntent.putExtra("day", day);
        nextIntent.putExtra("month", month+1);
        nextIntent.putExtra("year", year);        
        startActivity(nextIntent);
    }
}
