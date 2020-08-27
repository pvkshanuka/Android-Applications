package com.example.app27boardcastpracticalreceive2;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import receivers.Receiver2;

public class MainActivity extends AppCompatActivity {

    Receiver2 receiver2 = new Receiver2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(10);
        intentFilter.addAction("action1");
        registerReceiver(receiver2,intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver2);
    }
}
