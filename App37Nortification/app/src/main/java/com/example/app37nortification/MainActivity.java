package com.example.app37nortification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1234", "a", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("TEST TEST TEST");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNortification(v);
            }
        });

    }

    private void addNortification(View v) {

//        Notification.Builder builder = new Notification.Builder(this);
//        Aluth versions wala mehema ganne
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1234");

        builder.setSmallIcon(R.drawable.ic_shipping_t);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_shipping);
        builder.setLargeIcon(icon);

        builder.setContentTitle("New Nortification");
        builder.setContentText("This is new test nortification");

        Intent i = new Intent(this, MainActivity.class);
        PendingIntent p = PendingIntent.getActivity(this, 0, i, 0);

        builder.setContentIntent(p);

        Notification notification = builder.build();
        NotificationManagerCompat compat = NotificationManagerCompat.from(this);
        compat.notify(123, notification);
    }
}
