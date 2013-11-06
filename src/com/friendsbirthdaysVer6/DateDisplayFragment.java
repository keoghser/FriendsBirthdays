package com.friendsbirthdaysVer6;

import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
//import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;


public class DateDisplayFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	int day = 0;
	int month = 0;
	int year = 0;
	OnDateTransferred nmListener;
	String dateDetails = "";
	
	
	 public void onAttach(Activity activity) {
         super.onAttach(activity);  
         
         try {
             nmListener = (OnDateTransferred) activity;
           } catch (ClassCastException e) {
               throw new ClassCastException(activity.toString() + " must implement OnDateTransferred");
           }
      
         dateDetails = nmListener.onDateTransfer();
         
     } 
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        if (dateDetails.equals("")){    	
	    	final Calendar c = Calendar.getInstance();
	        year = c.get(Calendar.YEAR);
	        month = c.get(Calendar.MONTH);
	        day = c.get(Calendar.DAY_OF_MONTH);
	      //Toast.makeText(getActivity(),"Month picked is "+month, Toast.LENGTH_SHORT).show();
        }
        else {    	
        int [] date = new int [3]; //String array with 3 entries
        date = ExtractDateValues(dateDetails);
    	
    	day = date[0];
    	month = date[1]-1;
    	year = date[2]; 
        }
		
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dpd =  new DatePickerDialog(getActivity(), this, year, month, day);
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", dpd);
        return dpd;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {    	
        // Do something with the date chosen by the user
    	  
    	//Toast.makeText(getActivity(),"Day picked is "+day, Toast.LENGTH_SHORT).show();
    	//Toast.makeText(getActivity(),"Month picked is "+month, Toast.LENGTH_SHORT).show();
    	//Toast.makeText(getActivity(),"Year picked is "+year, Toast.LENGTH_SHORT).show();
    	
    	 int [] idate = new int [3]; // create an integer array with 3 indices to hold day, month, year
         idate[0] = day; // set index 0 = int value of day
         idate[1] = month+1;
         idate[2] = year;
         
         dateDetails = EditScreen.FormatDate(idate); // get a formatted string version of date
        // Toast.makeText(getActivity(),"Date picked is "+dateDetails, Toast.LENGTH_SHORT).show();
         nmListener.onDateReturn(dateDetails);
         
    	/*Intent nextIntent = new Intent();    	   
    	nextIntent.setClass(getActivity(), EditScreen.class);
        nextIntent.putExtra("day", day);
        nextIntent.putExtra("month", month+1);
        nextIntent.putExtra("year", year);        
        startActivity(nextIntent);*/
    }
    
 // Container Activity must implement this interface
 		public interface OnDateTransferred {
 			public String onDateTransfer();
 			public void onDateReturn(String dateDetails);
 			
 		}
 		
 	// Date Method
 	public static int [] ExtractDateValues (String sdate){
 			
 			int [] date = new int [3]; //String array with 6 entries
 			int [] locationOfHyphens = new int [2]; 
 		
 			String cutString = sdate;
 			
 			for(int i=0;i<2;i++){
 				int locate = cutString.indexOf("/");
 				
 				cutString = cutString.substring(locate+1, cutString.length());
 				
 				if (i==0){
 					locationOfHyphens[i]=locate;
 					}
 				else{
 					locationOfHyphens[i]=locate+locationOfHyphens[i-1]+1;
 					}			
 				}
 					
 			String day = sdate.substring(0, locationOfHyphens[0]);
 			String month = sdate.substring(locationOfHyphens[0]+1, locationOfHyphens[1]);
 			String year = sdate.substring(locationOfHyphens[1]+1);
 			
 			date[0] = Integer.parseInt(day);
 			date[1] = Integer.parseInt(month);
 			date[2] = Integer.parseInt(year); 			
 			
 			return date;
 					
 		}
    
}
