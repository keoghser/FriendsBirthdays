package com.friendsbirthdaysVer6;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseAdapter {

    static final String KEY_ROWID = "id";
    static final String KEY_NAME = "Name";
    static final String KEY_BIRTHDAY = "DateOfBirth";
    static final String KEY_NOTIFICATION = "Notification";
    static final String KEY_ACTION = "Action";
    static final String KEY_TEXT = "PhoneText";
    static final String KEY_PHONENO = "PhoneNumber";
    static final String TAG = "DBAdapter";    

    static final String DATABASE_NAME = "BirthdaysDB";
    static final String DATABASE_TABLE = "Friends";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE =
        "create table Friends (id integer primary key autoincrement, "
        + "Name text not null, DateOfBirth int not null, Notification int not null, " 
        		+ "Action int not null, PhoneText text, PhoneNumber text);";
    
    

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    Cursor mCursor;
   
        
    
    public DatabaseAdapter (Context ctx)
    	{
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    	}

    
    
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        	{
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        	}
        

      @Override
    public void onCreate(SQLiteDatabase db)
        {
          try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
      

        @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Friends");
            onCreate(db);
        }
    }

    
    
    
    //---opens the database---
    public DatabaseAdapter open() throws SQLException 
    	{
        db = DBHelper.getWritableDatabase();
        return this;
    	}

    
    //---closes the database---
    public void close() 
    	{
        DBHelper.close();
    	}
    

    //---insert a contact into the database---
    public long insertFriend(String name, String sdateOfBirth, int notification, int action, String phoneText, String phoneNumber) 
    //should return long	
    {
        double dateOfBirth = dateToJulian(sdateOfBirth);
       String apostropheName = name.replace("'", "€"); 
        
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, apostropheName); // ****************************************************************
        initialValues.put(KEY_BIRTHDAY, dateOfBirth);
        initialValues.put(KEY_NOTIFICATION, notification);
        initialValues.put(KEY_ACTION, action);
        initialValues.put(KEY_TEXT, phoneText);
        initialValues.put(KEY_PHONENO, phoneNumber);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    	
    	}
    

    //---deletes a particular contact---
    public boolean deleteFriend(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    	}
    

    //---retrieves all the Friends---
    public Cursor getAllFriends()
    	{
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_BIRTHDAY, KEY_NOTIFICATION, KEY_ACTION, KEY_TEXT, KEY_PHONENO}, null, null, null, null, KEY_NAME  + " ASC" );
        
    	}
    
        

    //---retrieves a particular contact with a known ID---
    public Cursor getFriendAtId(long rowId) throws SQLException 
    	{
        mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_NAME, KEY_BIRTHDAY, KEY_NOTIFICATION, KEY_ACTION, KEY_TEXT, KEY_PHONENO}, KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    	}
    
    //---retrieves a particular contact at a known row (not ID)---
    public int getFriendIdAtRow(int row) throws SQLException 
    	{
    	int idRow=0;
    	mCursor = getAllFriends();    
        
        if (mCursor != null) {        	
        	mCursor.moveToFirst();
        	mCursor.moveToPosition(row);
        	idRow = mCursor.getInt(0);      	            
        	}
            
        return  idRow;             
    	}
    
    
    //---retrieves a particular contact's ID using a known name---
    public int getFriendId(String name) throws SQLException 
	{
    String apostropheName = name.replace("'", "€");	//*****************************************************************
    int idRow=0;	   
    mCursor =
            db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
            KEY_NAME, KEY_BIRTHDAY, KEY_NOTIFICATION, KEY_ACTION, KEY_TEXT, KEY_PHONENO}, KEY_NAME + "= '" + apostropheName.trim()+"'", null,
            null, null, null, null); //*******************************************************************************
    
    if (mCursor != null) {
        mCursor.moveToFirst();
    }
    idRow = mCursor.getInt(0);
    return idRow;
	}
    

    //---updates a contact---
    public boolean updateFriend(long rowId, String name, String sdateOfBirth, int notification, int action, String phoneText, String phoneNumber) 
    	{
    	
    	double dateOfBirth = dateToJulian(sdateOfBirth); 
    	String apostropheName = name.replace("'", "€");  //**************************************************************  	
    	
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, apostropheName); //***************************************************************************
        args.put(KEY_BIRTHDAY, dateOfBirth);
        args.put(KEY_NOTIFICATION, notification);
        args.put(KEY_ACTION, action);
        args.put(KEY_TEXT, phoneText);
        args.put(KEY_PHONENO, phoneNumber);
        
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    	}
   
    
    //---Converts standard date to Julian date---
    public static double dateToJulian(String sdate) 
	 {

		Date date = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try{
			date = dateFormat.parse(sdate);
			}
		catch (Exception  e) {
			 System.out.println(e.toString());
			}
	
		Calendar calendar = Calendar.getInstance();	
		calendar.setTime(date); 

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);			
			double julian;

		double extra = (100.0 * year) + month - 190002.5;

		julian = (367.0 * year) - (Math.floor(7.0 * (year + Math.floor((month + 9.0) / 12.0)) / 4.0)) + 
				  Math.floor((275.0 * month) / 9.0) +  day +
				  1721013.5 - ((0.5 * extra) / Math.abs(extra)) + 0.5;
		return julian;
	}

    
    /**
     * Converts Julian date to standard date
     */
    public static int [] FromJulian(double JD) {
	
		double HALFSECOND = 0.5;
		int J,j,g,dg,c,dc,b,db,a,da,y,m,d,year,month,day;
	  	J = (int)(JD + HALFSECOND);
	 
		j = J + 32044;		  
		g=j/146097; 		
		dg=j%146097;		
		c=(dg/36524+1)*3/4; 		
		dc=dg-c*36524;		
		b=dc/1461;		
		db=dc%1461;		
		a=(db/365+1)*3/4; 		
		da=db-a*365;		
		y=g*400+c*100+b*4+a; 		
		m=(da*5+308)/153-2; 		
		d=da-(m+4)*153/5+122; 	
		year=y-4800+(m+2)/12;
		month=(m+2)%12+1;
		day=d+1;
	
		int [] intDate ={day,month,year};
		return intDate;
	}
    
    
    public static String StringDate (int [] sdate){
    	
    	String stringDate = sdate[0]+"/"+sdate[1]+"/"+sdate[2];
    	
    	return stringDate;
    }
    
    
 //Displays the details of each friend in the Database, for testing   
    public void DisplayFriend(Cursor c)
		{
       double date = c.getDouble(2);
       int [] dateOfBirth = FromJulian(date);
		Toast.makeText(context,
               "id: " + c.getString(0) + "\n" +
               "Name: " + c.getString(1).replace("€", "'") + "\n" + // ***************************************************
               "Date of Birth:  " + dateOfBirth[0]+"/"+ dateOfBirth[1]+"/"+ dateOfBirth[2], 
                
               Toast.LENGTH_LONG).show();
       Toast.makeText(context,
               "Notification: " + c.getInt(3) + "\n" +
               "Action: " + c.getInt(4) + "\n" +
               "Phone Text:  " + c.getString(5)+ "\n" +
               "Phone Number:  " + c.getString(6), 
               Toast.LENGTH_LONG).show();
		}
    
  

}
	


