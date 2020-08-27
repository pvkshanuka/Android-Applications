package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Classes.Validation;
import Database.SQLiteHelper;

public class ActivityPackageEdit extends AppCompatActivity {

    EditText et_hours, et_price, et_offer;
    Button btn_update, btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_edit);

        init();

        final int id = getIntent().getIntExtra("pid", 0);
        int hours = getIntent().getIntExtra("hours", 0);
        double price = getIntent().getDoubleExtra("price", 0.0);
        double offer = getIntent().getDoubleExtra("offer", 0.0);

        et_hours.setText(hours + "");
        et_price.setText(price + "");
        et_offer.setText(offer + "");

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_hours.getText().toString().equals("") || et_price.getText().toString().equals("") || et_offer.getText().toString().equals("")) {

                    Toast.makeText(ActivityPackageEdit.this, "Some Details are Empty.!", Toast.LENGTH_SHORT).show();

                } else {

                    if (Validation.isInt(et_hours.getText().toString()) || Validation.isDouble(et_price.getText().toString()) || Validation.isDouble(et_offer.getText().toString())) {

                        int hours = Integer.parseInt(et_hours.getText().toString());
                        double price = Double.parseDouble(et_price.getText().toString());
                        double offer = Double.parseDouble(et_offer.getText().toString());

                        if (hours != 0 && price > 1000 && offer < 100) {


                            SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
                            SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

//                            database.execSQL("INSERT INTO package(ve_id,hours,price,offer,status) VALUES ('"+id+"','"+hours+"','"+price+"','"+offer+"','"+1+"')");
                            database.execSQL("UPDATE package SET hours='" + hours + "', price='" + price + "',offer='" + offer + "' WHERE pa_id='" + id + "'");
                            System.out.println("Inserted");

                            Toast.makeText(ActivityPackageEdit.this, "Package Updated Successfully", Toast.LENGTH_SHORT).show();

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


                        } else {
                            Toast.makeText(ActivityPackageEdit.this, "Invalid Values.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ActivityPackageEdit.this, "Invalid Details.", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
                SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

//                            database.execSQL("INSERT INTO package(ve_id,hours,price,offer,status) VALUES ('"+id+"','"+hours+"','"+price+"','"+offer+"','"+1+"')");
                database.execSQL("UPDATE package SET status=0 WHERE pa_id='" + id + "'");
                System.out.println("Inserted");

                Intent intent = new Intent(ActivityPackageEdit.this, ActivityPackageViewA.class);
                startActivity(intent);

                Toast.makeText(ActivityPackageEdit.this, "Package Updated Successfully", Toast.LENGTH_SHORT).show();

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


            }
        });

    }

    private void init() {

        et_hours = findViewById(R.id.et_hoursed);
        et_price = findViewById(R.id.et_pricee);
        et_offer = findViewById(R.id.et_offere);

        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);

    }
}
