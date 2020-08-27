package com.app.wooker;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

import CustomClasses.Settings;
import CustomClasses.Validations;
import es.dmoral.toasty.Toasty;

public class Frag_Settings extends Fragment {


    Switch sw_rem, sw_nor;
    TextView tv_time;
    Button btn_settime;
    Settings settings;
    ConstraintLayout cons_lay_timepick;
    int mHour, mMinute;

    String from;

    Gson gson;

    public Frag_Settings() {
    }

    @SuppressLint("ValidFragment")
    public Frag_Settings(String from) {
        this.from = from;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.where = "Settings Client";

        View view = inflater.inflate(R.layout.frag_settings, container, false);

        init(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gson = new Gson();

        btn_settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                settings = gson.fromJson(sharedPreferences.getString(FirebaseAuth.getInstance().getUid(), null), Settings.class);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                if (settings != null) {
                                    settings.setTime(hourOfDay + ":" + minute);
                                    editor.putString(FirebaseAuth.getInstance().getUid(), gson.toJson(settings));

                                } else {
                                    settings = new Settings(sw_nor.isChecked(), sw_rem.isChecked(), hourOfDay + ":" + minute,from);
                                    editor.putString(FirebaseAuth.getInstance().getUid(), gson.toJson(settings));

                                }
//                                editor.putBoolean("wnor", sw_nor.isChecked());
//                                editor.putBoolean("wrem", sw_rem.isChecked());
//                                editor.putString("wtime", hourOfDay + ":" + minute);
                                editor.apply();
                                tv_time.setText(Validations.convert24hTo12h(hourOfDay + ":" + minute));
                                setupReminder();
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        sw_rem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                if (sharedPreferences.getString(FirebaseAuth.getInstance().getUid(), null) != null) {

                    settings = gson.fromJson(sharedPreferences.getString(FirebaseAuth.getInstance().getUid(), null), Settings.class);
                    settings.setRem(isChecked);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(FirebaseAuth.getInstance().getUid(), gson.toJson(settings));
                    editor.apply();


                } else {
                    settings = new Settings();
                    settings.setRem(isChecked);
                    settings.setFrom(from);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(FirebaseAuth.getInstance().getUid(), gson.toJson(settings));
                    editor.apply();

                }
                setupReminder();
                if (isChecked) {
                    cons_lay_timepick.setVisibility(View.VISIBLE);
                } else {
                    if (!sw_rem.isChecked()) {
                        cons_lay_timepick.setVisibility(View.GONE);
                    }
                }
            }
        });

        sw_nor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                if (sharedPreferences.getString(FirebaseAuth.getInstance().getUid(), null) != null) {

                    settings = gson.fromJson(sharedPreferences.getString(FirebaseAuth.getInstance().getUid(), null), Settings.class);
                    settings.setNor(isChecked);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(FirebaseAuth.getInstance().getUid(), gson.toJson(settings));
                    editor.apply();


                } else {
                    settings = new Settings();
                    settings.setNor(isChecked);
                    settings.setFrom(from);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(FirebaseAuth.getInstance().getUid(), gson.toJson(settings));
                    editor.apply();

                }
                setupReminder();
                if (isChecked) {
                    cons_lay_timepick.setVisibility(View.VISIBLE);
                } else {
                    if (!sw_rem.isChecked()) {
                        cons_lay_timepick.setVisibility(View.GONE);
                    }
                }
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.getString(FirebaseAuth.getInstance().getUid(), null) != null) {
            System.out.println("Settings null na");
            settings = gson.fromJson(sharedPreferences.getString(FirebaseAuth.getInstance().getUid(), null), Settings.class);
            System.out.println(settings.isNor() + " " + settings.isRem() + " " + settings.getTime());
            sw_rem.setChecked(settings.isRem());
            sw_nor.setChecked(settings.isNor());
            if (settings.getTime() == null) {
                tv_time.setText(Validations.convert24hTo12h("07:00"));
            } else {
                tv_time.setText(Validations.convert24hTo12h(settings.getTime()));
            }
        } else {
            System.out.println("Settings null");

            sw_rem.setChecked(false);
            sw_nor.setChecked(false);
            tv_time.setText(null);

        }
//        sw_rem.setChecked(sharedPreferences.getBoolean("wrem", false));
//        sw_nor.setChecked(sharedPreferences.getBoolean("wnor", false));
//        tv_time.setText(Validations.convert24hTo12h(sharedPreferences.getString("wtime", "00:00 xx")));

        if (sw_nor.isChecked() || sw_rem.isChecked()) {
            cons_lay_timepick.setVisibility(View.VISIBLE);
        } else {
            cons_lay_timepick.setVisibility(View.GONE);
        }

    }

    private void setupReminder() {
        Toasty.info(getActivity(), "Frag_Settings - setupReminder").show();
        Intent alarmReceiverIntent = new Intent(getContext(), ReminderReceiver.class);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.getString(FirebaseAuth.getInstance().getUid(), null) != null) {
            settings = gson.fromJson(sharedPreferences.getString(FirebaseAuth.getInstance().getUid(), null), Settings.class);
            System.out.println(settings.isNor() + " " + settings.isRem() + " " + settings.getTime());
            if (settings.isRem() || settings.isNor()) {
                Calendar calendar = Calendar.getInstance();


                if (settings.getTime() != null) {
                    System.out.println(Integer.parseInt(settings.getTime().split(":")[0]) + "   " + Integer.parseInt(settings.getTime().split(":")[1]));
                    if (calendar.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(settings.getTime().split(":")[0]) || (calendar.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(settings.getTime().split(":")[0]) && calendar.get(Calendar.MINUTE) >= Integer.parseInt(settings.getTime().split(":")[1]))) {
                        calendar.add(Calendar.DATE, 1);
                    }

                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(settings.getTime().split(":")[0]));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(settings.getTime().split(":")[1]));
                    calendar.set(Calendar.SECOND, 0);
                    System.out.println(calendar.getTime().toString());


                    PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), 1, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, new Date().getTime() + 5000, alarmIntent);
                    System.out.println("Alam added awaaaaaaaaaaaaaaaaaa");
                } else {
                    Toasty.error(getActivity(), "Please set time.!").show();
                }
            } else {
                System.out.println("Alam added awaaaaaaaaaaaaaaaaaa cancle");

                PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), 1, alarmReceiverIntent, PendingIntent.FLAG_NO_CREATE);
                if (alarmIntent != null) {
                    alarmManager.cancel(alarmIntent);
                }
            }
        } else {
            Toasty.error(getActivity(), "Reminder adding failed.!").show();
        }
//        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//        Intent intent =

    }

    private void init(View view) {

        sw_rem = view.findViewById(R.id.sw_rem);
        sw_nor = view.findViewById(R.id.sw_nor);
        tv_time = view.findViewById(R.id.tv_s_time_view);
        btn_settime = view.findViewById(R.id.btn_s_pick_time);
        cons_lay_timepick = view.findViewById(R.id.cons_lay_time_choose);

    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.where="";
    }
}
