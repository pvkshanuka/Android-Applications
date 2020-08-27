package com.example.application14sqltestproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Database.SQLiteHelper;

public class MainActivity extends AppCompatActivity {

    private EditText et_name,et_mobile,et_pword;
    private Button btn_insert,btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.editText);
        et_mobile = findViewById(R.id.editText2);
        et_pword = findViewById(R.id.editText3);

        btn_insert = findViewById(R.id.button);
        btn_search = findViewById(R.id.button2);

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((EditText)v).setText("");
                System.out.println("Method ekata awaaaaaa");
                return false;
            }
        };

        et_name.setOnLongClickListener(longClickListener);
        et_mobile.setOnLongClickListener(longClickListener);
        et_pword.setOnLongClickListener(longClickListener);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData(v);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData(v);
            }
        });

    }

    private void searchData(View v) {

        SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM user", null);

        while (cursor.moveToNext()){
            System.out.println("________________________________");
            System.out.println(cursor.getString(0));
            System.out.println(cursor.getString(1));
            System.out.println(cursor.getString(2));
            System.out.println(cursor.getString(3));
            System.out.println("________________________________");
        }

        cursor = database.rawQuery("SELECT * FROM user WHERE mobile='"+et_mobile.getText().toString()+"'",null);
        
        if (cursor.moveToNext()){
            et_name.setText(cursor.getString(1));
            et_pword.setText(cursor.getString(3));
        }else{
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }

    }

    private void insertData(View v) {

        SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        String name = et_name.getText().toString();
        String mobile = et_mobile.getText().toString();
        String pword = et_pword.getText().toString();

        database.execSQL("INSERT INTO user(name,mobile,password) VALUES ('"+name+"','"+mobile+"','"+pword+"')");
        System.out.println("Inserted");

        et_name.setText("");
        et_mobile.setText("");
        et_pword.setText("");

    }
}
