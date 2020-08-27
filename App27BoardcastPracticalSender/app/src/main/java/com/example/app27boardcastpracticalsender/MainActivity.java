package com.example.app27boardcastpracticalsender;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import receivers.LastReceiver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendBroadcastMessage();

            }
        });


    }


    private void sendBroadcastMessage() {
        Intent intent= new Intent();
        intent.setAction("action1");
//        sendBroadcast(intent);
//        Mehema dnmama thama piority eke order ekata yanne
        LastReceiver lastReceiver = new LastReceiver();
//        sendOrderedBroadcast(intent,null);

        Bundle bundle = new Bundle();
        bundle.putString("x","100");
        intent.putExtra("y","100");
        sendOrderedBroadcast(intent,null,lastReceiver,null,1,"a",bundle);
    }
}
