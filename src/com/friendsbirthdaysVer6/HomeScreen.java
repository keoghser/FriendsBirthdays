package com.friendsbirthdaysVer6;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.friendsbirthdaysVer6.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;



public class HomeScreen extends Activity {

	
	DatabaseAdapter db;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_screen);
        
        try {
        	String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(/*sdCard.getAbsolutePath() + */destPath); //sdCard.getAbsolutePath() added recently
            if (!f.exists()) 
            	{            	
            	f.mkdirs();
                f.createNewFile();               
                Log.d("HomeScreen",""+f.getName()+" was created");// for testing
            	//---copy the db from the assets folder into the databases folder---
    		    CopyDB(getBaseContext().getAssets().open("birthdaysdb"),new FileOutputStream(/*f*/destPath+ "/BirthdaysDB")); //f was destPath and it worked
    		  }
            
           
    		 } 
        	catch (FileNotFoundException e) 
    		 		{
    		        e.printStackTrace();
    		 		} 
        	catch (IOException e) 
        			{
    		        e.printStackTrace();
    		    	}
        
        
        
        
    // Start Button1 code   
    Button btn1 = (Button) findViewById(R.id.button1);
    btn1.setOnClickListener(new View.OnClickListener() {
    	
     public void onClick(View view) {
         //change this to entry screen when ready   
    	 Intent newIntent = new Intent(view.getContext(), EditScreen.class);
            startActivity(newIntent);
        	}
    	});   // end Button 1 code
   
    
    
    // Start Button2 code   
    Button btn2 = (Button) findViewById(R.id.button2);
    btn2.setOnClickListener(new View.OnClickListener() {
    	
     public void onClick(View view) {
            Intent newIntent = new Intent(view.getContext(), ReviewScreen.class);
            startActivity(newIntent);
        	}
    	});  // end Button 2 code
    
          
    }  
    public void showDatePickerDialog(View v) { 
        DialogFragment newFragment = new DatePickerFragment ();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    
    public void CopyDB(InputStream inputStream, 
   	OutputStream outputStream) throws IOException 
   	{
   		    //---copy 1K bytes at a time---
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = inputStream.read(mBuffer))>0)
        {
        	outputStream.write(mBuffer, 0, mLength);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();

    	}
} // end class
    

