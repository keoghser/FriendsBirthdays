package com.friendsbirthdaysVer6.services;

import java.util.ArrayList;

import com.friendsbirthdaysVer6.DatabaseAdapter;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

/**This method is started when
 * the StartService() class is called from another class
 * The method checks to see if the App's SharedPrefernces variable (xml file) is set to true, i.e. it has been
 * activated previously.
 * If the variable is true, this class scans the APP database to get information on alarms
 * and notifications to be set in the future and sets them.
 * */
public class StartService extends IntentService {
	private AlarmManagerBroadcastReceiver alarm;
	DatabaseAdapter db;	
	ArrayList<Integer> friendIDs = new ArrayList<Integer>();
	ArrayList<String> dates = new ArrayList<String>();
	ArrayList<Integer> notifications = new ArrayList<Integer>();
	
	
	/**
	 *  Constructor for the class, calls its superclass
	 *  setting it with the name "ServiceStarted"
	 */	
	public StartService(){ 
		super("ServiceStarted");
		}
	
	
	public void onCreate() { // may not be needed?
	        super.onCreate();
	        Log.d("Server", ">>>onCreate()");
	    }

	/**
	 * Called when the service is started explicitly using startServices()
	*/ 
	public int onStartCommand(Intent intent, int flags, int startId) {
			super.onStartCommand(intent, startId, startId);
	        Log.d("LocalService", "Received start id " + startId + ": " + intent);// for testing
	        return START_STICKY; // Code continues to run until stopped
	    }

	/**
	 * This includes code that needs to be executed on a separate thread
	 */
	protected void onHandleIntent(Intent intent) {
			Log.d("onHandleIntentService","onHandleIntent service starting");// for testing
			SharedPreferences appActivity = getSharedPreferences
					( "com.friendsbirthdaysver6_preferences", MODE_PRIVATE);  // Obtains an instance of the SharedPreference
																			// class by specifying the name of the xml file
																			// sharedpreferences object is only accessible to this package
			
			boolean active = appActivity.getBoolean("activated", false); // Retrieve a boolean value from sharedPreferences
																		 // false is default if no value present
			
			if(active) // If Sharedpreference variable was set up previously
				{
				Log.d("onHandleIntentService","SharedPreference Active");// for testing				
				alarm = new AlarmManagerBroadcastReceiver(); // create AlarmManagerBroadcastReceiver object
				Context context = this.getApplicationContext();   
				
				/*
				 * Call a class to interrogate the database, get information on alarms
				 * and notifications to be set in the future and set them.
				*/				
				
				db = new DatabaseAdapter(this);	        	
	        	db.open();        	
		        Cursor c = db.getAllFriends(); // get all information for all friends and put into cursor object
		        GetFriendsId(c);
		        GetFriendsDate(c);
		        GetFriendsNotification(c);
		        
		        /*
		         * Loop through friendIDs arraylist, use ConvertToAlarmString () method from AlarmManagerBroadcastReceiver
		         * Class with date and notification arguments to obtain String alarmDetails,
		         * Set an alarm using each String alarmDetails
		         */
		        for(int i=0; i<friendIDs.size(); i++){
		        	String alarmDetails = alarm.ConvertToAlarmString(dates.get(i), notifications.get(i));
		        	alarm.SetAlarm(context, friendIDs.get(i), alarmDetails);
		        	}
				}
			
			else{
				Log.d("onHandleIntentService","SharedPreference NOT Active"); // for testing
				}
		}
	
	/**
	 * Adds String items from cursor c at index 0 to friendIDs arraylist
	 */
	public void GetFriendsId (Cursor c){ 
			c.moveToFirst();	// Start at first item in cursor
			while(!c.isAfterLast()) { //While loop through cursor object
			friendIDs.add(c.getInt(0)); //add the String item at index 0 to friendIDs arraylist 
			c.moveToNext();}
			}	
	
	/**
	 * Adds Double items from cursor c at index 2 to dates arraylist 
	 */
	public void GetFriendsDate (Cursor c){
			int [] dateOfBirth	= new int [3];
			c.moveToFirst();	// Start at first item in cursor
			while(!c.isAfterLast()) { //While loop through cursor object
			double date = c.getDouble(2); //gets the Double item at index 2 
			dateOfBirth = DatabaseAdapter.FromJulian(date); // converts double value from Julian date to standard date
			dates.add(DatabaseAdapter.StringDate(dateOfBirth));  // converts the int [] to a string and adds to dates arraylist
    		c.moveToNext();}
    		}
	

	/**
	 * Adds Int items from cursor c at index 3 to notifications arraylist
	 */public void GetFriendsNotification (Cursor c){
			c.moveToFirst();	// Start at first item in cursor
			while(!c.isAfterLast()) { //While loop through cursor object
			notifications.add(c.getInt(3)); //add the Int item at index 3 to notifications arraylist 
	    	c.moveToNext();}
			}		
	
}	

