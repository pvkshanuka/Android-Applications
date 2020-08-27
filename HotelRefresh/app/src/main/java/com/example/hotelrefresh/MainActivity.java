package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Database.SQLiteHelper;
import Models.Customer;


public class MainActivity extends AppCompatActivity {

    public static Customer customer = null;

    private Button button, btn_addp, btn_loadcus,btn_editp;
    private EditText et_cusid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        btn_addp = findViewById(R.id.btn_addp);
        btn_loadcus = findViewById(R.id.btn_loadcus);
        et_cusid = findViewById(R.id.et_cusid);
        btn_editp = findViewById(R.id.btn_editp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openMain2Activity();
                Intent intent = new Intent(MainActivity.this, ActivityVenues.class);
                startActivity(intent);
            }

            private void openMain2Activity() {
                Intent intent01 = new Intent(MainActivity.this, Main2Activity.class);
                intent01.putExtra("first", 0);
                startActivity(intent01);
            }
        });

        btn_addp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityPackageAdd.class);
                startActivity(intent);
            }
        });

        btn_loadcus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
                SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

                Cursor cursor = database.rawQuery("SELECT * FROM customer WHERE cus_id='" + et_cusid.getText().toString() + "'", null);

                if (cursor.moveToNext()) {

                    customer = new Customer(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));

                    System.out.println("________________________________");
                    System.out.println(cursor.getString(0));
                    System.out.println(cursor.getString(1));
                    System.out.println(cursor.getString(2));
                    System.out.println(cursor.getString(3));
                    System.out.println(cursor.getString(4));
                    System.out.println("________________________________");

                    Toast.makeText(MainActivity.this, "Customer Loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Invalid Customer ID", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_editp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ActivityPackageViewA.class);
                startActivity(intent);

            }
        });
    }
}
