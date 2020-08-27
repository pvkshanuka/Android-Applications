package com.example.app27broadcastsend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import receivers.Receiver2;

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

        Intent intent = new Intent();
//        intent.setAction("javainstitute");
        intent.putExtra("name","Kusal");
        ////Meka dnmama me dena package eke thiyana class walata witharai boardcast eka receive karanna puluwan.
////        intent.setPackage("com.example.app27broadcastreceive");
        ////MEka danne same app ekenma receive karanawanm bt same app ekenma send & receive karanawanm receiver reg karanna ona xml eke
////            intent.setClass(this, Receiver2.class);

//        Selected clas ekakata witharak broadcast karanawanm me widihata karanna ona
//            Meke First parameter eka denne Receiver inna app eke minifist eke package name eka second parameter ekata denne reciver class eka, e class eka package ekak asse thiyanawanm pahala widihata denawa, package ekaka nemennm minifist eke package name eka dot class name eka kiyala denne
        intent.setClassName("com.example.app27broadcastreceive","receivers.Receiver1");

        sendBroadcast(intent);

    }
}
