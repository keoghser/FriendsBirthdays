package com.friendsbirthdaysVer6;


import java.util.ArrayList;



import com.friendsbirthdaysVer6.R;
import com.friendsbirthdaysVer6.DialogFragmentReview.OnNameTransferred;
import com.friendsbirthdaysVer6.ScrollerFragment.OnNameSelectedListener;

import android.app.Activity;

import android.content.Intent;

import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReviewScreen extends Activity implements OnNameSelectedListener, OnNameTransferred{
	
	int nameChosen = 0;
	DatabaseAdapter db;	
	String details ="";	
	String fname ="";
	String fdate ="";
	String fnotification ="";
	String faction ="";
	String fphonetext ="";
	String fphonenumber ="";
		
	ArrayList<String> names = new ArrayList<String>();
	String [] friendsNames;	
	int idFriend = 0;
	AutoCompleteTextView textView;

	
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_review_screen);
        
    	fname = getResources().getString(R.string.fname);
    	fdate = getResources().getString(R.string.fdate);
    	fnotification = getResources().getString(R.string.fnotification);
    	faction = getResources().getString(R.string.faction);
    	fphonetext = getResources().getString(R.string.fphonetext);
    	fphonenumber = getResources().getString(R.string.fphonenumber);    	
        
    		db = new DatabaseAdapter(this);
        	        	
        	db.open();        	
	        Cursor c = db.getAllFriends(); // get all information for all friends and put into cursor object
	        getFriendsName(c);	// call getFriendsName method to add friend's names to 'names' arraylist	            
	    	
		    int arraySize = names.size(); // get length of 'names' arraylist
		    	
		    friendsNames = new String [arraySize]; // create 'friendsNames' array of length 'arraySize'
		    	
		    friendsNames = ScrollerFragment.convertToArray(names);  // Copies the values of 'names' arraylist 
		    														// to 'friendsNames' array
        	        	
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	// create and populate ArrayAdapter Object
	                android.R.layout.simple_dropdown_item_1line, friendsNames); // with values from 'friendsNames' array 
	        
        	textView = (AutoCompleteTextView) findViewById (R.id.search); // create AutoCompleteTextView
	        																		// and assign to 'search' editText view
	        
	        textView.setThreshold(2);			// Set number of letter before AutoCompleteTextView prompts user
	        textView.setAdapter(adapter);		// Assign ArrayAdapter to AutoCompleteTextView	      
	        
	       
	        // Set up Home button code 
	        Button btn8 = (Button) findViewById(R.id.button8);
	        btn8.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View view) {
	        	 Intent newIntent = new Intent(view.getContext(), HomeScreen.class); // go to HomeScreen
	                startActivity(newIntent);
	            	}
	        	});   // end Button 8 code
         }
    		    
	
     // Start Button3 code  
     public void onClick(View view) {
               
        	Intent nameData = new Intent(); // is this needed?
        	
     		EditText searchFriend = (EditText) findViewById (R.id.search); 
     		nameData.setData(Uri.parse(searchFriend.getText().toString())); // is this needed?
     		
     		String nameSearch = searchFriend.getText().toString();       		
     		idFriend = db.getFriendId(nameSearch.replace("'", "€")); //********************************************
     		
     		
     		//
     		Cursor c = db.getFriendAtId(idFriend);		
    		
    		details = ConvertCursorToString(c);
    		
    		DialogFragmentReview dialogFragment = new DialogFragmentReview();
	        dialogFragment.show(getFragmentManager(), "detailsReview");	        
     		
     		/*db.close();
     		
     		// Start EditScreen activity and send 'idFriend' to it
            Intent nextIntent = new Intent();            
     	   	nextIntent.setClass(view.getContext(), EditScreen.class);
            nextIntent.putExtra("idFriend", idFriend);
            startActivity(nextIntent);
        	*/
            }
          // end Button 3 code


	@Override
	public void onNameSelected(int positionChosen) {
		
		nameChosen = positionChosen;
		
		int rowId = db.getFriendIdAtRow(nameChosen);	
		
		Cursor c = db.getFriendAtId(rowId);		
		
		details = ConvertCursorToString(c);
	}

	
	private String ConvertCursorToString(Cursor c) {
		
		idFriend = c.getInt(0);
		
		int notif = c.getInt(3);
		String notific="";
		switch (notif) {
			case 0:
				notific="8pm on day before";
				break;		
			case 1:
				notific="7am on day";
				break;
			case 2:
				notific="9am on day";
				break;
			case 3:
				notific="11am on day";
				break;
			case 4:
				notific="1pm on day";
				break;			
			default:
				break;
			}
		
		int act = c.getInt(4);
		String actn="";
		switch (act) {
			case 0:
				actn="Reminder";
				break;
			case 1:
				actn="Prepare Text Message";
				break;
			case 2:
				actn="Send Text Message";
				break;			
			default:
				break;
			}
		
		String text = c.getString(5);		
		if (text==null)
			{			
			text = "Empty";
			}
		
		String phone = c.getString(6);		
		if (phone==null)
			{			
			phone = "Empty";
			}
		
			String idDetails =  "£"+c.getString(1).replace("€", "'")+"£"+DatabaseAdapter.StringDate(DatabaseAdapter.FromJulian(c.getDouble(2))) 
    		  +"£"+notific+"£"+actn+"£"+phone+"£"+text+"£";		//********************************************       
      return idDetails;
		}
     
	public String onNameTransfer() {		
		return details;		
		}
	
	
	public void getFriendsName(Cursor c){
			c.moveToFirst();	// Start at first item in cursor
	    	while(!c.isAfterLast()) { //While loop through cursor object
	    		names.add(c.getString(1).replace("€", "'")); //add the String item at index 1 to names arraylist //******************************************** 
  	     c.moveToNext();
	    	}
		}
	
	
	  public int getFriendsId (Cursor c){  
		   c.moveToFirst();	    	
	      idFriend = c.getInt(0); //add the item	    
	      return idFriend;
	  }	
	  
	  
	  protected void onResume() {
	        // TODO Auto-generated method stub
	        super.onResume();	        
	        textView.setText("");
	     }
	  
	  
	// Displays the details of each friend in the Database, for testing   
	     public void DisplayFriend(Cursor c)
			{
	        double date = c.getDouble(2);
	        int [] dateOfBirth = DatabaseAdapter.FromJulian(date);
			Toast.makeText(this,
	                "id: " + c.getString(0) + "\n" +
	                "Name: " + c.getString(1).replace("€", "^") + "\n" +
	                "Date of Birth:  " + dateOfBirth[0]+"/"+ dateOfBirth[1]+"/"+ dateOfBirth[2], 
	                 
	                Toast.LENGTH_LONG).show();
	        Toast.makeText(this,
	                "Notification: " + c.getInt(3) + "\n" +
	                "Action: " + c.getInt(4) + "\n" +
	                "Phone Text:  " + c.getString(5)+ "\n" +
	                "Phone Number:  " + c.getString(6), 
	                Toast.LENGTH_LONG).show();
			}
	  
      
}
