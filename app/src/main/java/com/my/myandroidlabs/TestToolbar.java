package com.my.myandroidlabs;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    private String toastString = "You clicked on the overflow menu.";
    Toolbar tBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);


	    /* slide 15 material:
	    MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }  });

	    */

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            //when click on "go to studying":
            case R.id.goStudying:
                // Show the toast immediately:
                Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
                break;
            // when click on "go travelling":
            case R.id.goTravelling:
                // Show the toast immediately:
                Toast.makeText(this, "This is the initial message.", Toast.LENGTH_LONG).show();
                break;
            // when click on "go shopping":
            case R.id.goShopping:
                showDialog();
                break;
            // when click on "go eating":
            case R.id.goEating:
                // Snackbar code:
                Snackbar sb = Snackbar.make(tBar, "Go Back?", Snackbar.LENGTH_LONG)
                                      .setAction("Go to previous page", e -> finish());
                sb.show();
                break;
        }
        return true;
    }

    private void showDialog(){
        View middle = getLayoutInflater().inflate(R.layout.dialog_box, null);
        EditText et = (EditText)middle.findViewById(R.id.view_edit_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        toastString = et.getText().toString();
                    }
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                })
                .setView(middle);

        builder.create().show();
    }

}
