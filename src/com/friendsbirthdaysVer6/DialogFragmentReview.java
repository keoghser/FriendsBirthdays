package com.friendsbirthdaysVer6;



import com.friendsbirthdaysVer6.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;



public class DialogFragmentReview extends DialogFragment { 
	
	DatabaseAdapter db;	
	String friendsDetails = "";
	OnNameTransferred nmListener;	
	String fname ="";
	String fdate ="";
	String fnotification ="";
	String faction ="";
	String fphonetext ="";
	String fphonenumber ="";
	String details ="";
	String [] separateDetails;
	int idFriend = 0;
	
	 public void onAttach(Activity activity) {
         super.onAttach(activity);  
         
         db = new DatabaseAdapter(activity); 
         
         db.open();
         
         try {
             nmListener = (OnNameTransferred) activity;
           } catch (ClassCastException e) {
               throw new ClassCastException(activity.toString() + " must implement OnDateTransferred");
           }
       fname = getResources().getString(R.string.fname);
       fdate = getResources().getString(R.string.fdate);
       fnotification = getResources().getString(R.string.fnotification);
       faction = getResources().getString(R.string.faction);
       fphonetext = getResources().getString(R.string.fphonetext);
       fphonenumber = getResources().getString(R.string.fphonenumber);   
       friendsDetails = nmListener.onNameTransfer();
       
       ConvertCursorToString(friendsDetails);	
     } 
	
		
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(Html.fromHtml(details))
	               .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {	                	
	                	   db.close();
	                	   Intent nextIntent = new Intent();    	   
	                	   nextIntent.setClass(getActivity(), EditScreen.class);
	                       nextIntent.putExtra("idFriend", idFriend);
	                       startActivity(nextIntent);
                   	   }
	               })
	               .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   Toast.makeText(getActivity(),separateDetails[0]+" deleted", Toast.LENGTH_SHORT).show();	
	                	   db.deleteFriend(idFriend);
	                	   db.close();
	                	  
	                	   Intent newIntent = new Intent(getActivity(),ReviewScreen.class); 
	                	   startActivity(newIntent);
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	
	
	// Container Activity must implement this interface
		public interface OnNameTransferred {
			public String onNameTransfer();
    }
	
		
		private void ConvertCursorToString(String stringDetails) {
			
			separateDetails = new String [6]; //String array with 6 entries
			int [] locationOfHyphens = new int [7]; 			
			String cutString = stringDetails;
			
			for(int i=0;i<7;i++){
				int locate = cutString.indexOf("£");
				cutString = cutString.substring(locate+1, cutString.length());
				
				if (i==0){
					locationOfHyphens[i]=locate;
				}
				else{
					locationOfHyphens[i]=locate+locationOfHyphens[i-1]+1;
					}
				}		
			
			for(int i=0;i<6;i++){
			separateDetails[i] = stringDetails.substring(locationOfHyphens[i]+1, locationOfHyphens[i+1]);			
			}
			
			String textMessage = separateDetails[5];			
			
			textMessage = CutString(textMessage);							
			
			int [] date = new int [3]; //int array with 3 entries
	        date = DateDisplayFragment.ExtractDateValues(separateDetails[1]);
	        String sdate = EditScreen.FormatDate(date);
					
			details=""+"<b>" +fname+ "</b>  "+separateDetails[0]		        
				+"<br/><b>"+fdate+ "</b>  "+sdate	
		        +"<br/><b>"+fphonenumber+ "</b>  "+separateDetails[4] 		
		        +"<br/><b>"+fnotification+ "</b>  "+separateDetails[2]  
		        +"<br/><b>"+faction+ "</b>  "+separateDetails[3]  		   		
		   		+"<br/><b>"+fphonetext+ "</b>  "+textMessage;	
						
			
			idFriend = db.getFriendId(separateDetails[0]);		
		}
		
		private String CutString (String ptext) {
			
			String cutText ="";
						
				if (ptext.length()>42)
					{
					ptext = ptext.substring(0,42);
					
					int lastSpace = 0;
					
					lastSpace = ptext.lastIndexOf(" ");					
						
					if((lastSpace>38)&&(lastSpace!=-1))
							{
							cutText = ptext.substring(0,lastSpace)+"...";	
							}
					else{
							cutText = ptext+"...";
							}					
					}
				else {
					cutText = ptext; 					 
					}						
			
			return cutText;
		}
	
	}
