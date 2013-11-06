package com.friendsbirthdaysVer6.services;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date; 
import com.friendsbirthdaysVer6.R;
import com.friendsbirthdaysVer6.DatabaseAdapter;
import com.friendsbirthdaysVer6.DateDisplayFragment;
import com.friendsbirthdaysVer6.EditScreen;

//import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.util.Log;


/**
 * This class takes information from the StartService class and sets alarms with that information
 * When an alarm time is reached, the onReceive method is fired and and the NotificationView class is called
 * */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {  
	
	//final public static String ONE_TIME = "onetime";  // This could be deleted, maybe done;t need to set one time?
	//int notificationID = -1; // Need to set this notification ID to match Database primary key
	DatabaseAdapter db;	
	int action = 0;
	String name = "";
	String text = "";
	String phone = "";
	
	
	@Override 
	/**
	 * This method is called when the BroadcastReceiver is receiving an Intent broadcast. 
	 * It wakes up the phone
	 */
	public void onReceive(Context context, Intent intent) {   
		// wake up phone
		PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);         
		PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Wake lock");   
		//PARTIAL_WAKE_LOCK	or SCREEN_BRIGHT_WAKE_LOCK 
		
		//Acquire the lock         
		wakeLock.acquire();  
		int alarmID = (int) intent.getExtras().getLong("idAlarm");
		
		//Process alarm here.         
		db = new DatabaseAdapter(context);	        	
    	db.open();        	
        Cursor c = db.getFriendAtId(alarmID); // get all information for all friends and put into cursor object
        action = c.getInt(4);
        Log.d("AlarmManagerBroadcastReceiver","action is "+action);// for testing
        name = c.getString(1).replace("€", "'");
       
        
        text = c.getString(5);		
		if (text==null){			
			text = "No Text Entered";
			}
		
		phone = c.getString(6);		
		if (phone==null){			
			phone = "";
			}
		
		if (action==0){
			Intent i = new Intent(context, EditScreen.class);
		    i.putExtra("notificationID", alarmID);
		    i.putExtra("idFriend", alarmID);
		    i.putExtra("alarmReset", 1);
		    
		    PendingIntent pendingIntent =
		    PendingIntent.getActivity(context, 0, i, 0);
	
		    NotificationManager nm = (NotificationManager)
		        		context.getSystemService(Context.NOTIFICATION_SERVICE); 
		       
		    CharSequence title = ""+name+"'s Birthday Today!";// for testing
		        
		    CharSequence message = "Change Settings?";
		        
		    Notification notif = new Notification.Builder(context)
		        .setContentTitle(title)         
		        .setContentText(message) 
		        .setContentIntent(pendingIntent)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .getNotification();
		  
		    	//---100ms delay, vibrate for 250ms, pause for 100 ms and
		    // then vibrate for 500ms---
		    	notif.flags |= Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;
		    	notif.defaults |= Notification.DEFAULT_SOUND;
		    	notif.vibrate = new long[] {100, 250, 100, 500,100,250,100,500}; 
		        nm.notify(alarmID, notif);  
		        //nm.cancel(alarmID);
		    }
		
		else if (action==1){			

			Intent i = new Intent (android.content.Intent.ACTION_VIEW);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("address",phone);
			i.putExtra("sms_body", text);
			i.setType("vnd.android-dir/mms-sms");
			context.startActivity(i);
			}
		
		else if ((action==2)&&(phone.length()>0)){
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phone, null, text, null, null);
			
			NotificationManager nm = (NotificationManager)
		        		context.getSystemService(Context.NOTIFICATION_SERVICE); 
		       
		    CharSequence title = ""+name+"'s Birthday Today!";// for testing
		        
		    CharSequence message = "Text Message Sent";
		        
		    Notification notif = new Notification.Builder(context)
		        .setContentTitle(title)         
		        .setContentText(message) 		        
		        .setSmallIcon(R.drawable.ic_launcher)
		        .getNotification();
		  
		    	//---100ms delay, vibrate for 250ms, pause for 100 ms and
		    // then vibrate for 500ms---
		    	notif.flags |= Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;
		    	notif.defaults |= Notification.DEFAULT_SOUND;
		    	notif.vibrate = new long[] {100, 250, 100, 500,100,250,100,500};
		        nm.notify(alarmID, notif);  
		        Log.d("AlarmManagerBroadcastReceiver","Text message sent to "+phone);// for testing
		        //nm.cancel(alarmID);
		    }
		
		
		//Release the lock         
		wakeLock.release(); 
		
	}  
	

	public void SetAlarm(Context context, long idAlarm, String alarmDetails){ 
		/*
		 * This method is the main method in the APP to set a timer/alarm
		 */
		long alarm = ConvertCalendarToSystemTime(alarmDetails);	
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);   
		
		//set pending intent based on action	
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class); 
		intent.putExtra("idAlarm", idAlarm);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)(idAlarm), intent, 0); 
		//Toast.makeText(context, "In SetAlarm, idAlarm is "+idAlarm, Toast.LENGTH_LONG).show();  
		Log.d("AlarmManagerBroadcastReceiver","currentTimeMillis is "+System.currentTimeMillis());// for testing
		Log.d("AlarmManagerBroadcastReceiver","AlarmTime in TimeMillis is "+alarm);// for testing
		
		if (alarm>System.currentTimeMillis()){			
			am.set(AlarmManager.RTC_WAKEUP, alarm, pendingIntent);	
			Log.d("AlarmManagerBroadcastReceiver","Alarm "+idAlarm+" Set for "+alarmDetails);// for testing
		}
		else {			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			Date date = null;
			try{
				date = dateFormat.parse(alarmDetails);
			}catch (Exception  e) {
				 System.out.println(e.toString());
				}
			int nextYear = date.getYear()+1;
			date.setYear(nextYear);
			Calendar calendar = Calendar.getInstance();	
			calendar.setTime(date); 			
			long alarmNextYear = calendar.getTimeInMillis();
			am.set(AlarmManager.RTC_WAKEUP, alarmNextYear, pendingIntent);	
			Log.d("AlarmManagerBroadcastReceiver","Alarm "+idAlarm+" Set for "+alarmDetails+" next year "+alarmNextYear);// for testing
		}
	}
	
	
	public long ConvertCalendarToSystemTime(String sdate){
		/*
		 * This method converts a date represented by a String variable into the system date format.
		 */
			long systemDate = 0;
			Date date = null;			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

			try{
				date = dateFormat.parse(sdate);				
				}
			catch (Exception  e) {
				 System.out.println(e.toString());
				}
		
			Calendar calendar = Calendar.getInstance();	
			calendar.setTime(date); 			
			systemDate = calendar.getTimeInMillis();
			return systemDate;
	}
	
	
	public String ConvertToAlarmString (String sDate, int notification){		
		
		String sTime="";
		Calendar c = Calendar.getInstance();
        int thisYear = c.get(Calendar.YEAR);       
        int [] date = new int [3]; //String array with 3 entries
        date = DateDisplayFragment.ExtractDateValues(sDate);
    	
        switch (notification) {	// Switch - Set sTime variable based on notification variable
		case 0:
			sTime="20:00";
			date = DayBefore(date);
			break;
		case 1:
			sTime="07:00";
			break;
		case 2:
			sTime="09:00";
			break;
		case 3:
			sTime="11:00";
			break;
		case 4:
			sTime="13:00";
			break;
		default:
			break;	}
		
        date [2] = thisYear; 
    	sDate = EditScreen.FormatDate(date);    				
		String alarmDescription = sDate+" "+sTime+":00";        
        return alarmDescription;
	}
	
	
	public String ConvertToAlarmStringNextYear (String sDate, int notification){		
		String sTime="";
		Calendar c = Calendar.getInstance();
        int thisYear = c.get(Calendar.YEAR);       
        int [] date = new int [3]; //String array with 3 entries
        date = DateDisplayFragment.ExtractDateValues(sDate);
    	
        switch (notification) {	// Switch - Set sTime variable based on notification variable
		case 0:
			sTime="20:00";
			date = DayBefore(date);
			break;
		case 1:
			sTime="07:00";
			break;
		case 2:
			sTime="09:00";
			break;
		case 3:
			sTime="11:00";
			break;
		case 4:
			sTime="13:00";
			break;
		default:
			break;	}
		
        date [2] = thisYear+1; 
    	sDate = EditScreen.FormatDate(date);    				
		String alarmDescription = sDate+" "+sTime+":00";		 
		return alarmDescription;
	}
	
	
	/*public boolean IsFutureDate(long alarmTime){
		boolean isFutureDate = false;
		if(alarmTime>System.currentTimeMillis()){
			isFutureDate = true;
		}
		return isFutureDate;
	}*/
	
	
	public int [] DayBefore (int [] origDate){
		int [] newDate = new int [3];
		newDate [1] = origDate [1];
		Calendar c = Calendar.getInstance();
        int thisYear = c.get(Calendar.YEAR);
       // Log.d("AlarmManagerBroadcastReceiver","origDate month is "+origDate[1]);// for testing
		if (origDate[0]>1){
			//Log.d("AlarmManagerBroadcastReceiver","In first if, origDate[0] is "+origDate[0]);// for testing
			newDate[0] = origDate[0]-1;
			}
		else if ((thisYear==2016)||(thisYear==2020)){
			//Log.d("AlarmManagerBroadcastReceiver","In else if, origDate[0] is "+origDate[0]);// for testing
			switch (origDate[1]) {
				case 1:
					newDate[0]=31;
					newDate[1]=12;
					break;	
				case 3:
					newDate[0]=29;
					newDate[1]=2;
					break;
				case 5:
					newDate[0]=30;
					newDate[1]=4;
					break;
				case 7:
					newDate[0]=30;
					newDate[1]=6;
					break;
				case 10:
					newDate[0]=30;
					newDate[1]=9;
					break;
				case 12:
					newDate[0]=30;
					newDate[1]=11;
					break;
				default:
					newDate[0]=31;
					newDate[1]=origDate[1]-1;
					break;}
			}
		else {
			//Log.d("AlarmManagerBroadcastReceiver","In else, origDate[0] is "+origDate[0]);// for testing
			switch (origDate[1]) {
				case 1:
					newDate[0]=31;
					newDate[1]=12;
					break;	
				case 3:
					newDate[0]=28;
					newDate[1]=2;
					break;
				case 5:
					newDate[0]=30;
					newDate[1]=4;
					break;
				case 7:
					newDate[0]=30;
					newDate[1]=6;
					break;
				case 10:
					newDate[0]=30;
					newDate[1]=9;
					break;
				case 12:
					newDate[0]=30;
					newDate[1]=11;
					break;
				default:
					newDate[0]=31;
					newDate[1]=origDate[1]-1;
					break;}
			}
		//Log.d("AlarmManagerBroadcastReceiver","newDate month is "+newDate[1]);// for testing
		return newDate;
	}
	
}


