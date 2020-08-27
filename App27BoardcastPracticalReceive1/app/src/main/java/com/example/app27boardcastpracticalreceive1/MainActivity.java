package com.example.app27boardcastpracticalreceive1;

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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(20);
        intentFilter.addAction("action1");
        registerReceiver(receiver1,intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver1);
    }
}
