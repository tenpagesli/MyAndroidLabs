package com.my.myandroidlabs;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get layout xml object
        setContentView(R.layout.activity_chat_room);
        // get message array list
        if (msgList == null) {
            msgList = new ArrayList<>();
        }

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

        sendBtn.setOnClickListener((e) -> {
            Log.e("you clicked on :", " send button");
            msg = new Message(chatContent.getText().toString(), 1);
            msgList.add(msg);
            //this restarts the process of getCount() and getView()
            ((ChatAdapter) adt).notifyDataSetChanged();
            // reset textView to null
            chatContent.setText(null);
        });

        receiveBtn.setOnClickListener((e) -> {
            Log.e("you clicked on :", " receive button");
            msg = new Message(chatContent.getText().toString(), 2);
            msgList.add(msg);
            ((ChatAdapter) adt).notifyDataSetChanged();
            // reset textView to null
            chatContent.setText(null);
        });
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
         *  this method set up the view that will be added to the bottom of the view list
         *   @param position: locates the one that will be add to the bottom
         *   @return the new view
         **/
        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = null;
            LayoutInflater inflater = getLayoutInflater();
            msg = (Message)getItem(position);
            if (msg.getMessageDirection() == 1) {
                newView = inflater.inflate(R.layout.sender_row, parent, false);
                content = (TextView) newView.findViewById(R.id.sendContent);
            }
            if (msg.getMessageDirection() == 2) {
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
