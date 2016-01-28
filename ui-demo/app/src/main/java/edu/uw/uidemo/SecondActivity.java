package edu.uw.uidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "**DEMO 2**";

    //for listview
    private SimpleCursorAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //action bar "back"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String msg = extras.getString("edu.uw.uidemo.message");

        Log.v(TAG, msg);

        ((TextView)findViewById(R.id.txtSecond)).setText(msg);



        //TODO: fill in the link between columns and ids!
        String[] cols = new String[]{};
        int[] ids = new int[]{};

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item,
                null,
                cols, ids,
                0 //no flag
        );
        //set the adapter
        AdapterView listView = (AdapterView)findViewById(R.id.listSecond);
        listView.setAdapter(adapter);
    }
}
