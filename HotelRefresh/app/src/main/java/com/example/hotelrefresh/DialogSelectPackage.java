package com.example.hotelrefresh;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Database.SQLiteHelper;

public class DialogSelectPackage extends AppCompatDialogFragment {

    Button btn_stime, btn_sdate, btn_pres;
    RadioButton rbtn_cash, rbtn_card;
    RadioGroup radioGroup;
    String time, date, pmethod;
    int packid;

    public DialogSelectPackage(int packid) {
        this.packid = packid;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_select_package, null);

        init(view);

        builder.setView(view)
                .setTitle("Package Reservation")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogSelectPackage.this.dismiss();
//                        Toast.makeText(getActivity(), "Email Sign In Failed.!", Toast.LENGTH_SHORT).show();
                    }
                });


        btn_stime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        time = hourOfDay + ":" + minute;
                        System.out.println(time);
                    }
                }, 12, 0, false);
                timePickerDialog.show();
            }
        });

        btn_sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date = i + "-" + (++i1) + "-" + i2;
                    }
                }, 2019, 0, 1);
                datePickerDialog.show();

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rbtn_cash:
                        // Your code
                        pmethod = "Cash";
                        break;
                    case R.id.rbtn_card:
                        // Your code
                        pmethod = "Card";
                        break;
                }
                System.out.println(pmethod);
            }
        });

        btn_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(time + "-" + date + "-" + pmethod);
                if (time == null || date == null || pmethod == null) {
                    Toast.makeText(getActivity(), "Some Details are Empty", Toast.LENGTH_SHORT).show();
                } else {
                    Date ptime = null;
                    Date pdate = null;
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        ptime = simpleDateFormat.parse(time);

                        simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");
                        pdate = simpleDateFormat.parse(date);

                        System.out.println(ptime.toString() + "||" + pdate.toString() + "||" + pmethod);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SQLiteHelper helper = new SQLiteHelper(getActivity());
                    SQLiteDatabase database = helper.getWritableDatabase();
                    database.execSQL("INSERT INTO packagereservation(cus_id,pa_id,ptime,pdate,pmethod,status) VALUES ('" + MainActivity.customer.getId() + "','" + packid + "','" + ptime + "','" + pdate + "','" + pmethod + "',1)");


                    Cursor cursor = database.rawQuery("SELECT * FROM packagereservation", null);

                    while (cursor.moveToNext()) {

                        System.out.println("_________________________");
                        System.out.println(cursor.getInt(0));
                        System.out.println(cursor.getInt(1));
                        System.out.println(cursor.getInt(2));
                        System.out.println(cursor.getString(3));
                        System.out.println(cursor.getString(4));
                        System.out.println(cursor.getString(5));
                        System.out.println(cursor.getInt(6));

                    }
                    Toast.makeText(getActivity(), "Package Reserved Successfully", Toast.LENGTH_SHORT).show();
                    DialogSelectPackage.this.dismiss();

                }
            }
        });


        return builder.create();
    }

    private void init(View view) {
        btn_stime = view.findViewById(R.id.btn_stime);
        btn_sdate = view.findViewById(R.id.btn_sdate);
        btn_pres = view.findViewById(R.id.btn_pres);

        radioGroup = view.findViewById(R.id.radioGroup);

        rbtn_cash = view.findViewById(R.id.rbtn_cash);
        rbtn_card = view.findViewById(R.id.rbtn_card);

    }
}
