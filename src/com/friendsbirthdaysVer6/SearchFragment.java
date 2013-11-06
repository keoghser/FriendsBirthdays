package com.friendsbirthdaysVer6;

import java.util.ArrayList;

import com.friendsbirthdaysVer6.R;



import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


	public class SearchFragment extends Fragment {
		
		DatabaseAdapter db;	
		ArrayList<String> names = new ArrayList<String>();
		String [] friendsNames;			
		
		  
		  public void onAttach(Activity activity) {
	            super.onAttach(activity);
	            db = new DatabaseAdapter(activity); 
	           
	            db.open();
	        	Cursor c = db.getAllFriends();
	        	getFriendsName(c);	// adds friends names to names arraylist	        	
	        	db.close(); 
	    	
		    	int arraySize = names.size();
		    	
		    	friendsNames = new String [arraySize];
		    	
		    	friendsNames = ScrollerFragment.convertToArray(names); 
		    			    	
		  	}
		
		  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
				return inflater.inflate(R.layout.search_fragment,container,false);
				}
		 
	
		
		  public void getFriendsName(Cursor c)
  			{
		    	c.moveToFirst();
		    	while(!c.isAfterLast()) {
		    		names.add(c.getString(1)); //add the item
	    	     c.moveToNext();
	    		}
  			}


}
