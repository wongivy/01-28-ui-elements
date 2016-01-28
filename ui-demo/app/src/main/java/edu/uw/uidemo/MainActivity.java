package edu.uw.uidemo;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "**DEMO**";

    private static final int REQUEST_PICTURE_CODE = 1;

    private int mClickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActionBar actionBar = getSupportActionBar(); //for reference

        //View launchButton = findViewById(R.id.btnLaunch);
        //View callButton = findViewById(R.id.btnDial);
        //View cameraButton = findViewById(R.id.btnPicture);
        //View messageButton = findViewById(R.id.btnMessage);
        //View clickerButton = findViewById(R.id.btnClicker);

    }

    public void startSecondActivity(View v) {
        Log.v(TAG, "Launch button pressed");

        //Explicit intent
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("edu.uw.uidemo.message", "Hello number 2!");
        startActivity(intent);
    }

    public void callNumber(View v) {
        Log.v(TAG, "Call button pressed");

        //implicit intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("tel:206-685-1622"));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void takePicture(View v) {
        Log.v(TAG, "Camera button pressed");

        //implicit intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_PICTURE_CODE);
        }
    }

    public void sendMessage(View v) {
        Log.v(TAG, "Message button pressed");
        //...
    }

    public void clickerPressed(View v) {
        Log.v(TAG, "Clicker button pressed");
        //...
    }
}
