package com.example.app26broadcastingpractical;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import receivers.ConnectivityChangeReceiver;

public class MainActivity extends AppCompatActivity {

    ConnectivityChangeReceiver connectivityChangeReceiver = new ConnectivityChangeReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Main OnCreate", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Activity1.class);
                startActivity(intent);
                //mehea finish kaloth me yana intent ekedi main eke hadala thiyana receiver wada karanne na.. ehema wada karanna ona nm getApplicationContext().registerReceiver() kiyala danna ona
//                Receiver me intent ekata witharak danawa nm eka ondestroy eke unregistor karanna ona.. eka rule ekak
//                finish();
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

//        getApplicationContext().registerReceiver(connectivityChangeReceiver,intentFilter);
        registerReceiver(connectivityChangeReceiver,intentFilter);


    }

    @Override
    protected void onResume() {
        super.onResume();
//        meke reciver register karanawanm unregistor karanna ona onPause eke
//        meke registor kalama minimize kalama wada karanne na
        Toast.makeText(this, "Main OnResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Main OnPause", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Main OnDestroy", Toast.LENGTH_SHORT).show();

//        Receiver me intent ekata witharak danawa nm eka ondestroy eke unregistor karanna ona.. eka rule ekak
        unregisterReceiver(connectivityChangeReceiver);

    }
}
