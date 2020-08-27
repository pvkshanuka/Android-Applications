package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import Classes.Validation;
import Database.SQLiteHelper;

public class ActivityPackageAdd extends AppCompatActivity {

    Spinner venue_spinner;
    EditText et_hours, et_price, et_offer;
    Button btn_addpack;

    ArrayList<String> venues_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_add);

        init();

        btn_addpack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (venue_spinner.getSelectedItem().toString().equals("") || venue_spinner.getSelectedItem().toString().equals("Please Select Venue") || et_hours.getText().toString().equals("") || et_price.getText().toString().equals("") || et_offer.getText().toString().equals("")) {

                    Toast.makeText(ActivityPackageAdd.this, "Some Details are Empty.!", Toast.LENGTH_SHORT).show();

                } else {

                    if (Validation.isInt(et_hours.getText().toString()) || Validation.isDouble(et_price.getText().toString()) || Validation.isDouble(et_offer.getText().toString())) {

                        int hours = Integer.parseInt(et_hours.getText().toString());
                        double price = Double.parseDouble(et_price.getText().toString());
                        double offer = Double.parseDouble(et_offer.getText().toString());

                        if (hours != 0 && price > 1000 && offer < 100){

                            int venue_id = Integer.parseInt(venue_spinner.getSelectedItem().toString().split("-")[0]);

                            SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
                            SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

                            database.execSQL("INSERT INTO package(ve_id,hours,price,offer,status) VALUES ('"+venue_id+"','"+hours+"','"+price+"','"+offer+"','"+1+"')");
                            System.out.println("Inserted");

                            venue_spinner.setSelection(0);

                            et_hours.setText("");
                            et_price.setText("");
                            et_offer.setText("");

                            Toast.makeText(ActivityPackageAdd.this, "Package Added Successfuly", Toast.LENGTH_SHORT).show();

                            Cursor cursor = database.rawQuery("SELECT * FROM package WHERE status = 1", null);

                            while (cursor.moveToNext()) {
                                System.out.println("________________________________");
                                System.out.println(cursor.getString(0));
                                System.out.println(cursor.getString(1));
                                System.out.println(cursor.getString(2));
                                System.out.println(cursor.getString(3));
                                System.out.println(cursor.getString(4));
                                System.out.println(cursor.getString(5));
                                System.out.println("________________________________");
                            }


                        }else{
                            Toast.makeText(ActivityPackageAdd.this, "Invalid Values.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ActivityPackageAdd.this, "Invalid Details.", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });

    }

    private void init() {

        venue_spinner = findViewById(R.id.sp_venue);

        et_hours = findViewById(R.id.et_hours);
        et_price = findViewById(R.id.et_price);
        et_offer = findViewById(R.id.et_offer);

        btn_addpack = findViewById(R.id.btn_addpack);

        venues_list = new ArrayList<>();
        venues_list.add("Please Select Venue");

        SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM venue WHERE status = 1", null);

        while (cursor.moveToNext()) {
            venues_list.add(cursor.getString(0) + "- " + cursor.getString(1));
            System.out.println("________________________________");
            System.out.println(cursor.getString(0));
            System.out.println(cursor.getString(1));
            System.out.println("________________________________");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityPackageAdd.this, R.layout.my_simple_list_item1, venues_list);
        adapter.setDropDownViewResource(R.layout.my_spinner);

        venue_spinner.setAdapter(adapter);

    }
}
