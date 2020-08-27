package com.app.wooker;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.app.wooker.DBClasses.Notification;
import com.app.wooker.DBClasses.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Date;

import CustomClasses.Settings;
import es.dmoral.toasty.Toasty;

public class ReminderReceiver extends BroadcastReceiver {

    Date date;
    Settings settings;
    Gson gson;

    @Override
    public void onReceive(final Context context, Intent intent) {
        date = new Date();
        System.out.println("onReceive AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 1");
        gson = new Gson();
        System.out.println("onReceive AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 2");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        System.out.println("onReceive AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 3");


        settings = gson.fromJson(sharedPreferences.getString(FirebaseAuth.getInstance().getUid(), null), Settings.class);
//        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
//        mediaPlayer.start();
//        if (sharedPreferences.getBoolean("wrem",false)){
//            Toasty.info(context,"ReminderReceiver - onReceive").show();
//        }
        System.out.println("onReceive AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 4" + settings.isNor() + "-" + settings.isRem() + "-" + settings.getTime());


        if (settings != null) {
            if (settings.isRem()) {
                System.out.println("Loooooooooooooooooop AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa rem");
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    System.out.println("Loooooooooooooooooop AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa rem 2");


                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(final DocumentSnapshot documentSnapshot) {
                                    System.out.println("Loooooooooooooooooop AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa rem 3");

                                    if (documentSnapshot.exists()) {
                                        System.out.println("Loooooooooooooooooop AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa rem 4");

                                        User user = documentSnapshot.toObject(User.class);

                                        if (user.getType().equals("1")) {
                                            System.out.println("Loooooooooooooooooop AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa rem 5");

                                            context.startActivity(new Intent(context, Activity_Reminder.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS));
                                        } else if (user.getType().equals("2")) {
                                            context.startActivity(new Intent(context, Activity_Reminder_C.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS));
                                        }

                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toasty.error(context, e.getMessage()).show();
                                }
                            });

                }
            }

            if (settings.isNor()) {
                System.out.println("Loooooooooooooooooop AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");


                createNotificationChannel(context);
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
                final PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
//            builder.setSmallIcon(R.drawable.wooker)
//                    .setContentTitle("Test Notification")
//                    .setContentText("Notification Content")
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true);
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//            notificationManager.notify(12125, builder.build());

                FirebaseFirestore.getInstance().collection("notifications")
                        .whereEqualTo("to", FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                System.out.println("Loooooooooooooooooop AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa 2");

                                Notification notification;
                                int i = 0;
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                    i++;
                                    notification = documentSnapshot.toObject(Notification.class);
                                    if (notification.getStatus().equals("1") || notification.getStatus().equals("2")) {
                                        builder.setSmallIcon(R.drawable.wooker)
                                                .setContentTitle(notification.getTitle())
                                                .setContentText(notification.getMessage())
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                                .setContentIntent(pendingIntent)
                                                .setAutoCancel(true);

                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                                        notificationManager.notify(i, builder.build());
                                    }
//                                } else {
//                                    builder.setSmallIcon(R.drawable.wooker)
//                                            .setContentTitle("New Notification")
//                                            .setContentText(notification.getMessage())
//                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                                            .setContentIntent(pendingIntent)
//                                            .setAutoCancel(true);
//
//                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//                                    notificationManager.notify(i, builder.build());
//                                }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(context, e.getMessage()).show();
                            }
                        });


            }
        }
        //        } else {
//            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, viewController, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
//
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
//                    .setSmallIcon(R.drawable.money)
//                    .setContentTitle("Daily transaction reminder")
//                    .setContentText("Touch to add new transactions")
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                    //.setContentIntent(pendingIntent)
//                    .setAutoCancel(true);
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//            notificationManager.notify(1, builder.build());
//        }

        setupNextAlarm(context);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminders";
            String description = "Reminders to check your job";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //
    private void setupNextAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmReceiverIntent = new Intent(context, ReminderReceiver.class);
        long time_mills = date.getTime() + (60000 * 60 * 24);
        ;
//        long time_mills = date.getTime() + (5000);
        ;
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time_mills, alarmIntent);
    }

}
