package com.friendsbirthdaysVer6;



import com.friendsbirthdaysVer6.R;
import com.friendsbirthdaysVer6.DateDisplayFragment.OnDateTransferred;
import com.friendsbirthdaysVer6.services.AlarmManagerBroadcastReceiver;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditScreen extends Activity implements OnDateTransferred{

	// Set up variables
	private AlarmManagerBroadcastReceiver alarm;
	int idFriend = -1;
	String dateChosen = "";
	DatabaseAdapter db;
	
	String [] notifications1;
	String [] actions1;
	String [] notifications2;
	String [] actions2;
	
	EditText nameFriend;
	Button dateFriend;
	EditText notificnFriend;
	EditText actnFriend;
	EditText phoneFriend;
	EditText textFriend;
	
	Spinner notificationFriend;
	Spinner actionFriend;
	
	int notFriend = 0;
	int actFriend = 0;
	String date="";
	int notif = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);
        
        
        
        //Create new DatabaseAdapter Object
        db = new DatabaseAdapter(this);	// Create a new DatabaseAdapter from the DatabaseAdapter Class
        db.open();
        
        // Get idFriend
        idFriend = getIntent().getIntExtra("idFriend", -1);	// get information saved in Intent object used to get here, if nothing saved, use -1
        Log.d("EditScreen","idFriend send in is "+idFriend);// for testing
      //******************************
        //Toast.makeText(this, "Name id in editScreen is "+idFriend, Toast.LENGTH_LONG).show();
        
        //Get date chosen
        int day = getIntent().getIntExtra("day", 0); // get information saved in Intent object used to get here, if nothing saved, use 0
        int month = getIntent().getIntExtra("month", 0);
        int year = getIntent().getIntExtra("year", 0);
        int alarmReset = getIntent().getIntExtra("alarmReset", 0);
        
       
        
        // Create editText boxes    	
    	nameFriend = (EditText) findViewById (R.id.editNameView); // Create editText box and call it nameFriend
    	dateFriend = (Button) findViewById (R.id.editDateView); // Create editText box and call it dateFriend    
        phoneFriend = (EditText) findViewById (R.id.editPhoneView); // Create editText box and call it phoneFriend
        textFriend = (EditText) findViewById (R.id.editTextView); // Create editText box and call it textFriend
        
        // Create Spinners with no blank space
        notificationFriend = (Spinner) findViewById (R.id.editNotificationView); // Create Spinner Object from editNotificationView
        actionFriend = (Spinner) findViewById (R.id.editActionView);
        
        // Get Spinners resources from Strings.xml with no blank space
        notifications1 = getResources().getStringArray(R.array.notification);
        actions1 = getResources().getStringArray(R.array.action);
       // Toast.makeText(this, "Name id in editScreen is "+idFriend, Toast.LENGTH_LONG).show();
        
        // Add a blank space to Spinners resources
        notifications2 = new String [notifications1.length+1];
        actions2 = new String [actions1.length+1];
        
        notifications2[0]="";						// Copy resources from notifications1 and add a blank space at start
        for(int i=0;i<notifications1.length;i++){
	        notifications2[i+1]=notifications1[i];
	        }
        
        actions2[0]="";
        for(int i=0;i<actions1.length;i++){
	        actions2[i+1]=actions1[i];
	        }
        
       /*
        // Format day variable
        String sday = "";
        
        if (day<10){
        	sday = "0"+Integer.toString(day); // Add 0 to front of day variable if blank and convert to String
        	}
        else {
        	sday = Integer.toString(day);
        	}
        
        // Format month variable
        String smonth = "";
        
        if (month<10){
        	smonth = "0"+Integer.toString(month); // Add 0 to front of month variable if blank and convert to String
        	}
        else{
        	smonth = Integer.toString(month);
        	}
        
        // Complete dateChosen String
        dateChosen = sday+"/"+smonth+"/"+year;*/
        
        int [] idate = new int [3]; // create an integer array with 3 indices to hold day, month, year
        idate[0] = day; // set index 0 = int value of day
        idate[1] = month;
        idate[2] = year;
        
        dateChosen = FormatDate(idate); // get a formatted string version of date
        
        
        
    
                
       
   // Enter this code if idFriend is not = -1, i.e. only if came from EditScreen to edit existing entry 
        if (idFriend>-1){
        	        	
        	// Create cursor
        	Cursor c = db.getFriendAtId(idFriend);	// Create a cursor object, get details from DB to match idFriend
        	
        	// pre-populate editText boxes    //*******************************************************************************    	
        	nameFriend.setText(c.getString(1).replace("€", "'")); // Set value of nameFriend editText box to String found at index 1 of cursor object
        	
        	
        	String sdate = DatabaseAdapter.StringDate(DatabaseAdapter.FromJulian(c.getDouble(2)));
        	//Toast.makeText(this, "sdate is "+sdate, Toast.LENGTH_SHORT).show();
        	int [] iadate = new int [3];
        	iadate = DateDisplayFragment.ExtractDateValues(sdate);
            
            date = FormatDate(iadate);
        	
        	dateFriend.setText(date); 
        	
        	
        	
           	// Set notific variable
        	notif = c.getInt(3); // Create new int notif with int value found at index 3 of cursor
        	//Toast.makeText(this, "notif is "+notif, Toast.LENGTH_SHORT).show();
    		int notific=0; // Initialise notific variable
    		
    		switch (notif) {	// Switch - Set notific variable based on notif variable
    			case 0:
    				notific=0;
    				break;
    			case 1:
    				notific=1;
    				break;
    			case 2:
    				notific=2;
    				break;
    			case 3:
    				notific=3;
    				break;
    			case 4:
    				notific=4;
    				break;
    			default:
    				break;	}
    		
    		// Populate Spinners        	
        	ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner,notifications1); 
           	notificationFriend.setAdapter(adapter1);    // Attach resources from notifications1 to notificationFriend Spinner      	
           	
           	ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner,actions1);
           	actionFriend.setAdapter(adapter2);
        	
           	// Set act variable
    		int act = c.getInt(4);  // Create new int act with int value found at index 4 of cursor       	
        	
    		// Pre-populate editText boxes
        	notificationFriend.setSelection(notific);    // Set value of notificationFriend editText box to notific value    	
        	
        	actionFriend.setSelection(act); 
        	
        	
        	// pre-populate editText boxes        	
        	phoneFriend.setText(c.getString(6)); // Set value of phoneFriend editText box to String found at index 6 of cursor object
        	
        	// Create and pre-populate textFriend
        	String text = c.getString(5);	// Create string text with String found at index 5 of cursor object         	
    		if (text==null){	// If no String found, add 'empty' to textFriend EditText box 		
    			text = "Empty";    			
    			textFriend.setText(text); 
    			}
    		else {    			
    			textFriend.setText(c.getString(5)); // If String found, add text to textFriend EditText box 
    			}  
    		
    		// Create setOnItemSelectedListener for actionFriend Spinner to obtain value selected
    		actionFriend.setOnItemSelectedListener(new OnItemSelectedListener() {
    	            
    	      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    	       			{
    	       			actFriend = arg0.getSelectedItemPosition();
    	       			//Toast.makeText(getBaseContext(), "index action is "+actFriend, Toast.LENGTH_LONG).show();
    	       			}
    	       		
    	      public void onNothingSelected (AdapterView<?> arg0)
    	       			{
    					Toast.makeText(getBaseContext(), "Please Select an Action Type", Toast.LENGTH_LONG).show();
    	       			}
    				});
    	     
    		// Create setOnItemSelectedListener for notificationFriend Spinner to obtain value selected
    	     notificationFriend.setOnItemSelectedListener(new OnItemSelectedListener() {
    	            
    	        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    	       			{
    	       			notFriend = arg0.getSelectedItemPosition();
    	       			//Toast.makeText(getBaseContext(), "index notification is "+notFriend, Toast.LENGTH_LONG).show();
    	       			}
    	       		
    			public void onNothingSelected (AdapterView<?> arg0)
    	       			{
    					Toast.makeText(getBaseContext(), "Please Select a Notification Type", Toast.LENGTH_LONG).show();
    	       			}
    				});
    	     
    	     if (alarmReset==1){
    	    	 alarm = new AlarmManagerBroadcastReceiver(); // create AlarmManagerBroadcastReceiver object
    	    	 Log.d("EditScreen","date is "+date+" notif is "+notif);// for testing
    	    	 String alarmTimeSet = alarm.ConvertToAlarmStringNextYear(date,notif);
    	    	alarm.SetAlarm(getBaseContext(), idFriend, alarmTimeSet);
    	    	 Log.d("EditScreen","Alarm "+idFriend+" reset");// for testing
    	    	 Log.d("EditScreen","alarmTimeSet "+alarmTimeSet);// for testing
    	        }
    	 	}// end original if
        
     
    // Enter this code if idFriend = -1, i.e. only if came from HomeScreen to create new entry  
        else {
        		// Create and pre-populate editText boxes
        		dateFriend = (Button) findViewById (R.id.editDateView); // Create editText box and call it dateFriend
        		
        		if (!dateChosen.equalsIgnoreCase("00/00/0")){ // If variable dateChosen does not equal to 00/00/0
        			dateFriend.setText(dateChosen); // Set value of editText box equal to value of dateChosen        			
        		}									// If variable is equal to 00/00/0, then this editText box will not be pre-populated
        		
        		// Populate Spinners
        		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner,notifications2);
        		notificationFriend.setAdapter(adapter1); // Attach resources from notifications2 to notificationFriend Spinner
               	       
        		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner,actions2);
        		actionFriend.setAdapter(adapter2);
        		
        		// Create setOnItemSelectedListener for actionFriend Spinner to obtain value selected
        		actionFriend.setOnItemSelectedListener(new OnItemSelectedListener() {
        	            
        	        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        	       			{
        	       			actFriend = arg0.getSelectedItemPosition()-1;
        	       			//Toast.makeText(getBaseContext(), "index action is "+actFriend, Toast.LENGTH_LONG).show();
        	       			}
        	       		
        				public void onNothingSelected (AdapterView<?> arg0)
        	       			{
        					Toast.makeText(getBaseContext(), "Please Select an Action Type", Toast.LENGTH_LONG).show();
        	       			}
        				});
        	     
        		// Create setOnItemSelectedListener for notificationFriend Spinner to obtain value selected
        	     notificationFriend.setOnItemSelectedListener(new OnItemSelectedListener() {
        	            
        	        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        	       			{
        	       			notFriend = arg0.getSelectedItemPosition()-1;
        	       			//Toast.makeText(getBaseContext(), "index notification is "+notFriend, Toast.LENGTH_LONG).show();
        	       			}
        	       		
        				public void onNothingSelected (AdapterView<?> arg0)
        	       			{
        					Toast.makeText(getBaseContext(), "Please Select a Notification Type", Toast.LENGTH_LONG).show();
        	       			}
        				});
        	} // end else
    	
       
      
      
        
     // Set up Save button code   
        Button btn6 = (Button) findViewById(R.id.button6); // Create btn6 from UI view button6
        btn6.setOnClickListener(new View.OnClickListener() { // Set btn6 onClickListener
        	
         public void onClick(View view) { // Create onClick Method, what is done when button is clicked
            
        	// Create variables to be transfered
        	long newRowId = idFriend; // get value of idFriend from original intent or -1 if not sent from intent        	
         	String newName = nameFriend.getText().toString();// create new variable and assign value in nameFriend editText box to it
         	newName = EnsureLastCharacterIsNotSpace(newName);
         	String newSdateOfBirth = dateFriend.getText().toString();
        	int newNotification = notFriend;
        	int newAction = actFriend;        	
        	String newPhoneNumber = phoneFriend.getText().toString();        	
        	String newPhoneText = textFriend.getText().toString();
        	boolean validated = Validate (newName,newSdateOfBirth,newNotification,newAction,newPhoneNumber,newPhoneText);
        		
        	        	
        	if (validated){
        	  
		        if (idFriend>-1){     // update friend       	 
		            	db.updateFriend(newRowId, newName, newSdateOfBirth, newNotification, newAction, newPhoneText, newPhoneNumber);
		            	}	             
		        else {          // insert friend  	 
		            	db.insertFriend(newName, newSdateOfBirth, newNotification, newAction, newPhoneText, newPhoneNumber);
		            	}
	        	 
        		db.close();
        		
        		//Toast.makeText(getBaseContext(), "NewRowId is "+newRowId, Toast.LENGTH_LONG).show();
        		SetTimer(view,newRowId,newNotification,newSdateOfBirth);        		
        		//***********************************
        		Toast.makeText(getBaseContext(), "Entry Saved", Toast.LENGTH_LONG).show();
        		// Start new intent
        		Intent newIntent = new Intent(view.getContext(), ReviewScreen.class); // Go to ReviewScreen
                startActivity(newIntent);
        	} // end if
        	
        	else {
        		return;      
        		}
        	
        		} // end onClick
        	});   // end setOnClickListener
        
    
        // Set up Home button code 
        Button btn7 = (Button) findViewById(R.id.button7);
        btn7.setOnClickListener(new View.OnClickListener() {
        	
         public void onClick(View view) {
             
        	 Intent newIntent = new Intent(view.getContext(), HomeScreen.class); // go to HomeScreen
                startActivity(newIntent);
            	}
        	});   // end Button 7 code
        
     } // end onCreate
    
    
    
    public boolean Validate (String name, String date, int notification, int action, String phoneNumber, String text){    	
    		boolean validated;				//******************************************************************************
    	
     	if (name.equals("") || !name.matches("^([a-zA-Z-' ]+$)?") || name.length()>25){ 
    		Toast.makeText(getBaseContext(), "Name is not valid, please re-enter", Toast.LENGTH_LONG).show();         		
    		return validated=false;
    		}  
     	else if (date==""){
     		Toast.makeText(getBaseContext(), "A birthday has not been chosen, please choose one", Toast.LENGTH_LONG).show();         		
    		return validated=false;
     	}
    	
    	else if (!phoneNumber.matches("^([0-9 ]+$)?") || phoneNumber.length()>23){
    		Toast.makeText(getBaseContext(), "Phone number is not valid, please re-enter", Toast.LENGTH_LONG).show();         		
    		return validated=false;
    		}         	
    	
    	else if ((notification<0) || (notification>4)){
    		Toast.makeText(getBaseContext(), "No notification type chosen, please choose", Toast.LENGTH_LONG).show(); 
    		return validated=false;
    		}        	
    	
    	else if ((action<0) || (action>2)){
    		Toast.makeText(getBaseContext(), "No action type chosen, please choose", Toast.LENGTH_LONG).show(); 
    		return validated=false;
    		}
     	
    	else if (text.length()>159){
    		Toast.makeText(getBaseContext(), "Text exceeds maximum length, please re-enter", Toast.LENGTH_LONG).show(); 
    		return validated=false;
    		}
    	else {
    		validated=true;
    		}
     	return validated;
		}
    
    
    public void showDatePickerDialog(View v) { 
        DialogFragment newFragment = new DateDisplayFragment ();
        newFragment.show(getFragmentManager(), "dateDisplay");
        
       /* Bundle currentValues = new Bundle ();
        currentValues.putString("name", nameFriend.getText().toString());
        //currentValues.putInt("notification", nameFriend.getText().toString());
        //currentValues.putString("action", action.getText().toString());
        currentValues.putString("phone", phoneFriend.getText().toString());
        currentValues.putString("text", textFriend.getText().toString());
       */
          }
    
    public String onDateTransfer() {
		//Toast.makeText(this,"Back in activity here with name ",Toast.LENGTH_SHORT).show();
		return dateFriend.getText().toString();		
		}
    
    public void onDateReturn(String date) {
		//Toast.makeText(this,"Back in activity here with date "+date,Toast.LENGTH_SHORT).show();
		dateChosen = date;
		dateFriend.setText(dateChosen);
		}
   
    
    //Method FormatDate
    public static String FormatDate (int [] sdate){
    	// Format day variable
        String sday = "";
        
        if (sdate[0]<10){
        	sday = "0"+Integer.toString(sdate[0]); // Add 0 to front of day variable if blank and convert to String
        	}
        else {
        	sday = Integer.toString(sdate[0]);
        	}
        
        // Format month variable
        String smonth = "";
        
        if (sdate[1]<10){
        	smonth = "0"+Integer.toString(sdate[1]); // Add 0 to front of month variable if blank and convert to String
        	}
        else{
        	smonth = Integer.toString(sdate[1]);
        	}
        
        // Complete dateChosen String
        String sdateChosen = sday+"/"+smonth+"/"+Integer.toString(sdate[2]);
        
        return sdateChosen;
    }
    
    
    
    public String EnsureLastCharacterIsNotSpace(String oldName){
    	String newname = oldName.trim();
    	return newname;
     }
    
    public void SetTimer(View view, long alarmID, int notification, String sDate){ 
    	/*
    	 * This method creates an AlarmManagerBroadcastReceiver object, sets an alarm 
    	 * and set's the SharedPreference Variable to TRUE
    	 */
    	Context context = this.getApplicationContext(); 
    	alarm = new AlarmManagerBroadcastReceiver();
    	//Toast.makeText(getBaseContext(), "In SetTimer, alarmID is "+alarmID, Toast.LENGTH_LONG).show();
    	
    	String alarmTimeSet = alarm.ConvertToAlarmString(sDate, notification);
	
		if(alarm != null){      
			alarm.SetAlarm(context, alarmID, alarmTimeSet);
			//Log.d("EditScreen","Alarm Set");// for testing
			}else
			{      
			Log.d("EditScreen","No alarm Set");// for testing
			//Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();     
			}  
		
		SharedPreferences appActivity = getSharedPreferences(
				  "com.friendsbirthdaysver6_preferences", MODE_PRIVATE); 
		SharedPreferences.Editor appActivityUpdater = appActivity.edit();
	  	appActivityUpdater.putBoolean("activated", true);	  
	    appActivityUpdater.commit();
	} 
    
    
} // end class
