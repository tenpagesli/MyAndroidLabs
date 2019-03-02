package com.my.myandroidlabs;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyChatDatabase";
    public static final int VERSION_NUM = 1;
    // the column names in database
    public static final String TABLE_NAME = "Message";
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IS_SENT = "is_sent"; // 0: false; 1: true

    public MyDatabaseOpenHelper(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    // it will run only if the database file doesn't exist yet
    public void onCreate(SQLiteDatabase db)
    {
        Log.i("Database onCreate:", "trying to create the database .");
        //Make sure you put spaces between SQL statements and Java strings:
       String sql = "CREATE TABLE " + TABLE_NAME + "( "
              + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
               + COL_CONTENT + " TEXT,"
               + COL_IS_SENT + " INTEGER)";
       // execute SQL
     //   String sql = "CREATE TABLE TEST ( ID INTEGER )";

        db.execSQL(sql);
        //
        Log.i("Database onCreate:", "the query is: " + sql);
        Log.i("Database onCreate:", "the database is created.");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade:", " Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", " Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    /**
     *   this function will print:
     *   •	The database version number
     *   •	The number of columns in the cursor.
     *   •	The name of the columns in the cursor.
     *   •	The number of results in the cursor
     *   •	Each row of results in the cursor.
     * @param c: the cursor
     */
    public void printCursor(Cursor c) {
        int totalColumnAmount = c.getColumnCount();
        String[] columns = c.getColumnNames();
        String columnNames = "";
        String eachRow = "";
        // get all the column names in one string
        for(int i=0; i<columns.length; i++){
            columnNames += (columns[i] + ", ");
        }
        Log.i("in printCursor: ", "The database version number is: " + VERSION_NUM);
        Log.i("in printCursor: ", "The number of columns in the cursor is: " + totalColumnAmount);
        Log.i("in printCursor: ", "The name of the columns in the cursor is/are: " + columnNames);
        Log.i("in printCursor: ", "The number of results in the cursor is: " + c.getCount());
        // get each row's content in one string
        if(totalColumnAmount>0){
            c.moveToFirst();
            do {
                for(int i=0; i<columns.length; i++) {
                    eachRow += (c.getString(i) + " ");
                }
                Log.i("in printCursor: ", "Each row of results in the cursor is " + eachRow);
                eachRow = "";
            }while(c.moveToNext());
        }else{
            Log.i("in printCursor: ", "No results. The cursor is empty.");
        }
    }
}
