package com.my.myandroidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    EditText typeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_linear);
        //setContentView(R.layout.activity_main_grid);
        // setContentView(R.layout.activity_main_relative);
        setContentView(R.layout.activity_login);
        typeField = findViewById(R.id.editEmail);
        // name: Desired preferences file.
        // If a preferences file by this name does not exist, it will be created when you retrieve an
        // editor (SharedPreferences.edit()) and then commit changes (Editor.commit()).
        sp = getSharedPreferences("EmailInfo", Context.MODE_PRIVATE);
        // "key" is the reserved field name in the sharedPreferences file
        String savedString = sp.getString("ReserveEmail", "");
        // save the typed email into the file and save it on hard disk.
        typeField.setText(savedString);

        // onClickListener to listen for "Login"
        Button logBtn = findViewById(R.id.login_button);
        logBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(nextPage);
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        //get an editor object
        SharedPreferences.Editor editor = sp.edit();

        //save what was typed under the name "ReserveEmail"
        String whatWasTyped = typeField.getText().toString();
        editor.putString("ReserveEmail", whatWasTyped);

        //write it to disk:
        editor.commit();
    }
}
