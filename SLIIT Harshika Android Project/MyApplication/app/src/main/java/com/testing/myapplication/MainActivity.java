package com.testing.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;

import connectionsqlite.DBConnection;

public class MainActivity extends AppCompatActivity {
    private DBConnection connection;
    private SQLiteDatabase database;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connection = new DBConnection(this);
        database = connection.getWritableDatabase();
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);

//        addDataToDB();

    }

    public void logIn(View view) {


        System.out.println(username.getText().toString()+"-"+password.getText().toString());

        loadDrivers();

        String query1 = "SELECT * FROM driver WHERE email='" + username.getText().toString() + "' AND password = '" + password.getText().toString() + "'";
        Cursor cursor = database.rawQuery(query1, null);
        int count = cursor.getCount();

        if (count == 1) {
            Intent i = new Intent(this, ProfileActivity.class);
            Bundle bundle = new Bundle();
            while (cursor.moveToNext()) {

                bundle.putInt("userid", cursor.getInt(0));
                bundle.putString("fname", cursor.getString(1));
                bundle.putString("lname", cursor.getString(2));
                bundle.putString("nic", cursor.getString(5));
            }
            i.putExtra("userdata", bundle);
            startActivity(i);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Login Error");
            builder.setMessage("Invalid Username/Password").
                    setCancelable(false);

            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setIcon(R.drawable.ic_person_black_24dp);
            builder.create().show();

        }


    }

    private void loadDrivers() {
        String query1 = "SELECT * FROM driver";
        Cursor cursor = database.rawQuery(query1, null);

        while (cursor.moveToNext()){
            System.out.println(cursor.getString(3));
            System.out.println(cursor.getString(4));
        }
    }

    public void addDataToDB() {
        try {
            String query1 = "INSERT INTO driver (fname,lname,email,password,nic,age,status) VALUES ('shakthi','Dilshan','exampleemail1@gmail.com','1234','922334963V',56,1)";
            String query2 = "INSERT INTO driver (fname,lname,email,password,nic,age,status) VALUES ('Saman','Kumara','exampleemail2@gmail.com','1234','922334963V',56,1)";
            String query3 = "INSERT INTO driver (fname,lname,email,password,nic,age,status) VALUES ('Shakila','Madushan','exampleemail3@gmail.com','1234','922334963V',56,1)";
            String query4 = "INSERT INTO driver (fname,lname,email,password,nic,age,status) VALUES ('Lakal','Deepaka','exampleemail14@gmail.com','1234','922334963V',56,1)";
            String query5 = "INSERT INTO driver (fname,lname,email,password,nic,age,status) VALUES ('Sandun','DilsDematagodahan','exampleemail5@gmail.com','1234','922334963V',56,1)";

            database.execSQL(query1);
            database.execSQL(query2);
            database.execSQL(query3);
            database.execSQL(query4);
            database.execSQL(query5);

            String query6 = "INSERT INTO orders (dri_id,paddress,daddress,pdate,deliverytime,note,status) VALUES (1,'Address 01','Deliveri Address 01','" + new Date().getTime() + "','" + new Date().getTime() + "','',1)";
            String query7 = "INSERT INTO orders (dri_id,paddress,daddress,pdate,deliverytime,note,status) VALUES (2,'Address 02','Deliveri Address 02','" + new Date().getTime() + "','" + new Date().getTime() + "','',1)";
            String query8 = "INSERT INTO orders (dri_id,paddress,daddress,pdate,deliverytime,note,status) VALUES (1,'Address 03','Deliveri Address 03','" + new Date().getTime() + "','" + new Date().getTime() + "','',1)";
            String query9 = "INSERT INTO orders (dri_id,paddress,daddress,pdate,deliverytime,note,status) VALUES (2,'Address 04','Deliveri Address 04','" + new Date().getTime() + "','" + new Date().getTime() + "','',1)";
            String query10 = "INSERT INTO orders (dri_id,paddress,daddress,pdate,deliverytime,note,status) VALUES (1,'Address 05','Deliveri Address 05','" + new Date().getTime() + "','" + new Date().getTime() + "','',1)";
            String query11 = "INSERT INTO orders (dri_id,paddress,daddress,pdate,deliverytime,note,status) VALUES (2,'Address 06','Deliveri Address 06','" + new Date().getTime() + "','" + new Date().getTime() + "','',1)";
            String query12 = "INSERT INTO orders (dri_id,paddress,daddress,pdate,deliverytime,note,status) VALUES (1,'Address 07','Deliveri Address 07','" + new Date().getTime() + "','" + new Date().getTime() + "','',1)";

            database.execSQL(query6);
            database.execSQL(query7);
            database.execSQL(query8);
            database.execSQL(query9);
            database.execSQL(query10);
            database.execSQL(query11);
            database.execSQL(query12);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
}
