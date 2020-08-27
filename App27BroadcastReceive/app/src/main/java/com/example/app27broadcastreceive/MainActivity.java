package com.example.app27broadcastreceive;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import receivers.Receiver1;

public class MainActivity extends AppCompatActivity {

        Receiver1 receiver1 = new Receiver1();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        Direct Target class ekakata broadcast eka enawanm java walin map karanne na mehema karata hariyanne na xml walin thama map karanna ona
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("javainstitute");
//        registerReceiver(receiver1,intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Direct Target class ekakata broadcast eka enawanm java walin map karanne na mehema karata hariyanne na xml walin thama map karanna ona
//        unregisterReceiver(receiver1);
    }
}
