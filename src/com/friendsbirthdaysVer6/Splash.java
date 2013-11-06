package com.friendsbirthdaysVer6;



import com.friendsbirthdaysVer6.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.app.Activity;
import android.content.Intent;


public class Splash extends Activity {
	
	protected boolean _active = true;
	//protected int _splashTime = 1500; // time to display the splash screen in ms
	private static final int SPLASH_DURATION = 2000; // 2 seconds

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_splash);
	    
	 /*
	    // thread for displaying the SplashScreen
	    Thread splashThread = new Thread() {
	        @
	        public void run() {
	            try {
	                int waited = 0;
	                while(_active && (waited < _splashTime)) {
	                    sleep(100);
	                    if(_active) {
	                        waited += 100;
	                    }
	                }
	            } catch(InterruptedException e) {
	            	 System.out.println("I was interrupted!");
	            	 
	            } finally {
	                finish();
	                Intent mainIntent = new Intent(Splash.this, HomeScreen.class);            
	                Splash.this.startActivity(mainIntent);
	            }
	        }
	    };
	    splashThread.start();
	}*/
	    
  	           
	    	Handler handler = new Handler();           
	    		
	    	// run a thread after 2 seconds to start the home screen         
	    	handler.postDelayed(new Runnable() {               
	    	            
	    	public void run() {                   
	    	// make sure we close the splash screen so the user won't come back when it presses back key
	    	finish();                                   
	    				
	    	// start the home screen if the back button wasn't pressed already                      
	    	Intent intent = new Intent(Splash.this, HomeScreen.class);                     
	    	Splash.this.startActivity(intent);                
	    	}           
	    }, SPLASH_DURATION); // time in milliseconds 
	    					//(1 second = 1000 milliseconds) until the run() method will be called       
	 } 
 }

	
	
	
	
	
	
	
	
	

