package com.friendsbirthdaysVer6;


import java.util.ArrayList;

import com.friendsbirthdaysVer6.R;


import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;


	
	public class ScrollerFragment extends ListFragment {	
		
		DatabaseAdapter db;	
		ArrayList<String> names = new ArrayList<String>();
		String [] friendsNames;		
		OnNameSelectedListener nmListener;
		
		  
		  public void onAttach(Activity activity) {
	            super.onAttach(activity);
	            db = new DatabaseAdapter(activity); 
	           
	            db.open();
	        	Cursor c = db.getAllFriends();
	        	getFriendsName(c);	// adds friends names to names arraylist	        	
	        	db.close(); 
	    	
		    	int arraySize = names.size();
		    	
		    	friendsNames = new String [arraySize];
		    	
		    	friendsNames = convertToArray(names);   
		    	
		    	try {
		              nmListener = (OnNameSelectedListener) activity;
		            } catch (ClassCastException e) {
		                throw new ClassCastException(activity.toString() + " must implement OnNameSelectedListener");
		            }

	        } 	
		  
		public void onCreate(Bundle savedInstanceState) 
				{
		        super.onCreate(savedInstanceState);
		        setListAdapter(new ArrayAdapter<String>(getActivity(),
		                android.R.layout.simple_list_item_1, friendsNames));
		        }
		
		public View onCreateView(LayoutInflater inflater, 
			    ViewGroup container, Bundle savedInstanceState) 
				{        
			        return inflater.inflate(R.layout.scroller_fragment, container, false);
				}
		
		
		        
		public void onListItemClick(ListView parent, View v, 
		        int position, long id) 
		        {   
			
			//********************
		        /*Toast.makeText(getActivity(), 
		                "You have selected " + friendsNames[position]+" at position "+position, 
		                Toast.LENGTH_SHORT).show();*/
		         
		         DialogFragmentReview dialogFragment = new DialogFragmentReview();
		         dialogFragment.show(getFragmentManager(), "detailsReview");
		         
		                    
		             // Send the event and int to the host activity
		             nmListener.onNameSelected(position);		        
		        }  
		    
		    
	   public void DisplayFriend(Cursor c)
				{
	        	Toast.makeText(getActivity(),"Name: " + c.getString(1),Toast.LENGTH_SHORT).show();
	           	}
		    
		    
	   public void getFriendsName(Cursor c)
	    		{
			    	c.moveToFirst();
			    	while(!c.isAfterLast()) {
			    		names.add(c.getString(1).replace("€", "'")); //add the item //********************************************    
		    	     c.moveToNext();
		    		}
	    		}
	    	
	    
	   public static String [] convertToArray(ArrayList<String> fnames)	
	   	//	Copies the values of an arraylist to an array
	    	{    	
	        	int arraySize = fnames.size();
	    	
	        	String [] arrayNames = new String [arraySize];
	    	
		    	for (int i=0;i<arraySize;i++){
		    		arrayNames[i] = fnames.get(i);
		    		}
	    	
	    	return arrayNames;    	
	    	}
	   
	
	// Container Activity must implement this interface
	    public interface OnNameSelectedListener {
	        public void onNameSelected(int location);
	    }

		
	}

