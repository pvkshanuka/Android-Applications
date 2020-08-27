package com.app.wooker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import CustomClasses.Validations;
import es.dmoral.toasty.Toasty;

public class Activity_Reminder_C extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private SensorEventListener accelerometerListener;

    TextView tv_check_job;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reminder);

        setFinishOnTouchOutside(false);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setupAlarmTone();
        setupView();
        setupFlipToDismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (accelerometerSensor != null) {
            sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();

        if (accelerometerSensor != null) {
            sensorManager.unregisterListener(accelerometerListener);
        }
    }

    private void setupView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        tv_check_job = findViewById(R.id.tv_check_jobs);

        Button btn_openapp, btn_dismiss;

        btn_openapp = findViewById(R.id.btn_open_app);
        btn_openapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Reminder_C.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Activity_Reminder_C.this.startActivity(intent);
                Activity_Reminder_C.this.finish();
            }
        });

        btn_dismiss = findViewById(R.id.btn_dismiss);
        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String txt = "";

            FirebaseFirestore.getInstance().collection("jobs")
                    .whereEqualTo("cid", FirebaseAuth.getInstance().getUid())
                    .whereLessThanOrEqualTo("status", "5")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                            } else {
                                int i = 0;
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    if (Validations.dateObjToString(documentSnapshot.toObject(Job.class).getJob_date(), "yyyy-MM-dd").equals(Validations.dateObjToString(new Date(), "yyyy-MM-dd"))) {
                                        i++;
                                    }
                                }
                                if (i != 0) {
                                    if (i == 1) {
                                        tv_check_job.setText("You requested a job today");

                                    } else {

                                        tv_check_job.setText("You requested " + queryDocumentSnapshots.size() + " jobs today");
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(getApplicationContext(), e.getMessage()).show();
                            e.printStackTrace();
                        }
                    });

        }

//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @SuppressLint("InlinedApi")
//            @Override
//            public void run() {
//                View mContentView = findViewById(R.id.reminderAlarmView);
//                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//
//                RippleBackground rippleBackground = findViewById(R.id.reminderAlarmView_rippleBackground);
//                rippleBackground.startRippleAnimation();
//            }
//        }, 500);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 30000);
    }

//    private void authenticateUser() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (user != null) {
//            FirebaseData.setupUserData(user);
//
//            SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
//            boolean accountSetupComplete = userData.getBoolean("accountSetupComplete", false);
//
//            if (accountSetupComplete) {
//                startActivity(new Intent(getApplicationContext(), ViewController.class).putExtra("target", NavigationController.TRANSACTION_CONTROLLER_TAG).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//            } else {
//                startActivity(new Intent(getApplicationContext(), AccountController.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//            }
//        } else {
//            startActivity(new Intent(getApplicationContext(), AccountController.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//        }
//    }

    private void setupAlarmTone() {
        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mediaPlayer = MediaPlayer.create(this, alarmTone);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    private void setupFlipToDismiss() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!sensorList.isEmpty()) {
            accelerometerSensor = sensorList.get(0);

            accelerometerListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float z_value = event.values[2];
                    if (z_value < 0) {
                        finish();
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        }
    }
}
