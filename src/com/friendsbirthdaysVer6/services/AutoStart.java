package com.friendsbirthdaysVer6.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class is used to automatically start an intent on device boot
 * It creates an intent to start StartService class
 * and starts the onStartCommand method of StartService
 */
public class AutoStart extends BroadcastReceiver {   
	 @Override  
	 public void onReceive(Context context, Intent intent) {   
	  if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) { //after the system has finished booting
	  Intent openIntent = new Intent(context, StartService.class);   // Creates intent to start StartService class
	  context.startService(openIntent);   // starts the onStartCommand Method of StartService
	 }   
   }
}
