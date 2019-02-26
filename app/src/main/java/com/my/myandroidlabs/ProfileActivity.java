package com.my.myandroidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Intent takePictureIntent;
    ImageButton mImageButton;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    private void dispatchTakePictureIntent() {
        // print log
        Log.e(ACTIVITY_NAME, " in function: " + "dispatchTakePictureIntent");
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // print log
        Log.e(ACTIVITY_NAME, " in function: " + "onCreate");
        super.onCreate(savedInstanceState);
        // get the typed email from sharedPreferences file on the phone
        setContentView(R.layout.activity_profile);
        EditText typeField = (EditText) findViewById(R.id.editEmail2);
        SharedPreferences sp = getSharedPreferences("EmailInfo", Context.MODE_PRIVATE);
        String savedString = sp.getString("ReserveEmail", "");
        typeField.setText(savedString);
        // add on click listener for button
        mImageButton = findViewById(R.id.picButton);
        mImageButton.setOnClickListener ( b -> {dispatchTakePictureIntent();});

        Button goChatBtn = findViewById(R.id.goChat);
        goChatBtn.setOnClickListener((e)->{
            Intent nextPage = new Intent(ProfileActivity.this, ChatRoomActivity.class);
            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() {
        // print log
        Log.e(ACTIVITY_NAME, " in function: " + "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        // print log
        Log.e(ACTIVITY_NAME, " in function: " + "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        // print log
        Log.e(ACTIVITY_NAME, " in function: " + "onPause");
        super.onPause();
    }
    @Override
    protected void onStop() {
        Log.e(ACTIVITY_NAME, " in function: " + "onStop");
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        // print log
        Log.e(ACTIVITY_NAME, " in function: " + "onDestroy");
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // print log
        Log.e(ACTIVITY_NAME, " in function: " + "onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }
}
