package com.my.myandroidlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    private Message msg;
    ArrayList<Message> msgList;
    ListView theList;
    TextView content;
    //get a database:
    MyDatabaseOpenHelper dbOpener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get layout xml object
        setContentView(R.layout.activity_chat_room);
        // get message array list
        if (msgList == null) {
            msgList = new ArrayList<>();
        }

        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();
        // find all data and put them into message list

        this.findAllData(db);

        // get the "ListView" object
        theList = (ListView) findViewById(R.id.the_list);
        // get two buttons, and set on click listener for them
        Button sendBtn = findViewById(R.id.sendBtn);
        Button receiveBtn = findViewById(R.id.receiveBtn);
        // get chat content for message object
        TextView chatContent = findViewById(R.id.chatContent);

        // get the adapter
        ListAdapter adt = new ChatAdapter(msgList);
        theList.setAdapter(adt);
        if(!msgList.isEmpty()){
            ((ChatAdapter) adt).notifyDataSetChanged();
        }

        sendBtn.setOnClickListener((e) -> {
            Log.e("you clicked on :", " send button");
            this.sendMessage(db, chatContent,  adt, true, sendBtn);
        });

        receiveBtn.setOnClickListener((e) -> {
            Log.e("you clicked on :", " receive button");
            // get the last message's id
            this.sendMessage(db, chatContent,  adt, false, receiveBtn);

        });

    }

    private void sendMessage(SQLiteDatabase db, TextView chatContent,  ListAdapter adt,
                             boolean clickOnSentBtn, Button buttonClicked){
        // get message content
        String content = chatContent.getText().toString();

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string name in the NAME column:
        newRowValues.put(MyDatabaseOpenHelper.COL_CONTENT, content);
        //put string email in the EMAIL column:
        newRowValues.put(MyDatabaseOpenHelper.COL_IS_SENT, clickOnSentBtn);
        //insert in the database:
        long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

        // create Message Object and add it to the list
        msg = new Message( newId,content , clickOnSentBtn);
        msgList.add(msg);
        // update ListView
        ((ChatAdapter) adt).notifyDataSetChanged();
        // reset textView to null
        chatContent.setText(null);
        //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
       // Snackbar.make(buttonClicked, "Inserted item id:"+newId, Snackbar.LENGTH_LONG).show();
    }

    // to find all the data, and put them into message list
    private void findAllData(SQLiteDatabase db){
        Log.e("you ", " are looking for all the data");
        //query all the results from the database:
        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_CONTENT, MyDatabaseOpenHelper.COL_IS_SENT};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        //find the column indices:
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);
        int contentColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_CONTENT);
        int isSentColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_IS_SENT);
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            boolean isSent = false;
            long id = results.getLong(idColIndex);
            String content = results.getString(contentColumnIndex);
            if(Integer.valueOf(results.getString(isSentColIndex))==1){ // 1: true
                isSent = true;
            }
            //add the new Contact to the array list:
            msgList.add(new Message(id, content, isSent));
        }
        // call printCursor() to print all the cursor results
        dbOpener.printCursor(results);
    }

    //This class needs 4 functions to work properly:
    protected class ChatAdapter<E> extends BaseAdapter {
        private List<E> dataCopy = null;

        //Keep a reference to the data:
        public ChatAdapter(List<E> originalData) {
            dataCopy = originalData;
        }

        //You can give it an array
        public ChatAdapter(E[] array) {
            dataCopy = Arrays.asList(array);
        }

        public ChatAdapter() {
        }

        // return how many items to display
        @Override
        public int getCount() {
            return dataCopy.size();
        }

        // return the contents will show up in each row
        @Override
        public E getItem(int position) {
            return dataCopy.get(position);
        }


        /***
         *  this method set up and add the view that will be added to the bottom of the view list.
         *  Thsi method will be run list.size() times
         *   @param position: locates the one that will be add to the bottom
         *   @return the new view
         **/
        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = null;
            LayoutInflater inflater = getLayoutInflater();
            msg = (Message)getItem(position);
            if (msg.isSent()) {
                newView = inflater.inflate(R.layout.sender_row, parent, false);
                content = (TextView) newView.findViewById(R.id.sendContent);
            }else{
                // Locate the layout (single_row), and add it to the bottom of current listView
                newView = inflater.inflate(R.layout.receiver_row, parent, false);
                content = (TextView) newView.findViewById(R.id.receiveContent);
            }
            //Get the string to go in row: position
            String toDisplay = getItem(position).toString();
            //Set the text of the text view
            content.setText(toDisplay);
            return newView;
        }

        // get the item id for a specific position in the view list.
        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
