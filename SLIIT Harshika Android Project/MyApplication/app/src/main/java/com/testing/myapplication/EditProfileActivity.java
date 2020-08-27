package com.testing.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import connectionsqlite.DBConnection;

public class EditProfileActivity extends AppCompatActivity {
    private EditText fname;
    private EditText lname;
    private EditText nic;
    private EditText age;
    private EditText email;
    private EditText password;
    private DBConnection connection;
    private SQLiteDatabase database;
    private int driverid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        driverid = getIntent().getIntExtra("userid", 0);

        fname = findViewById(R.id.ep_fname);
        lname = findViewById(R.id.ep_lname);
        nic = findViewById(R.id.ep_nic);
        age = findViewById(R.id.ep_age);
        email = findViewById(R.id.ep_email);
        password = findViewById(R.id.ep_password);
        connection = new DBConnection(this);
        database = connection.getWritableDatabase();
        loadInfo();

    }

    public void saveInfo(View view) {

        boolean b = validateNic(nic.getText().toString());
        boolean digitsOnly = TextUtils.isDigitsOnly(age.getText().toString());

        if (b && digitsOnly) {

            try {
                String query = "";
                if (password.getText().toString().equals("")) {
                    query = "UPDATE driver SET " +
                            "fname='" + fname.getText().toString() + "'," +
                            "lname = '" + lname.getText().toString() + "'," +
                            "nic= '" + nic.getText().toString() + "'," +
                            "age = '" + age.getText().toString() + "' " +
                            "WHERE dri_id='" + driverid + "'";
                } else {
                    query = "UPDATE driver SET " +
                            "fname='" + fname.getText().toString() + "'," +
                            "lname = '" + lname.getText().toString() + "'," +
                            "nic= '" + nic.getText().toString() + "'," +
                            "age = '" + age.getText().toString() + "'," +
                            "password='" + password.getText().toString() + "' " +
                            "WHERE dri_id='" + driverid + "'";
                }

                database.execSQL(query);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Update Success");
                builder.setMessage("User Detail Updated ! Please Login again").
                        setCancelable(false);

                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                        finish();
                    }
                }).setIcon(R.drawable.ic_done_black_24dp);
                builder.create().show();


            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else {
            if (!b) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Invalid NIC");
                builder.setMessage("Please Use Valid NIC No !").
                        setCancelable(false);

                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setIcon(R.drawable.ic_warning_black_24dp);
                builder.create().show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Invalid Age");
                builder.setMessage("Please Use Valid Age !").
                        setCancelable(false);

                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setIcon(R.drawable.ic_warning_black_24dp);
                builder.create().show();
            }
        }


    }


    public void backtoProfile(View view) {
        onBackPressed();
    }

    private void loadInfo() {

        String query1 = "SELECT * FROM driver WHERE dri_id = '" + driverid + "'";
        Cursor cursor = database.rawQuery(query1, null);

        while (cursor.moveToNext()) {
            fname.setText(cursor.getString(1));
            lname.setText(cursor.getString(2));
            nic.setText(cursor.getString(5));
            age.setText(cursor.getString(6));
            email.setText(cursor.getString(3));
            password.setText("");
        }
        if (cursor.getCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("User Error");
            builder.setMessage("Invalid Userdata").
                    setCancelable(false);

            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setIcon(R.drawable.ic_person_black_24dp);
            builder.create().show();
        }
    }


    private boolean validateNic(String Nic) {
        boolean validate = false;
        if (validateNICOld(Nic) || validateNICNew(Nic)) {
            validate = true;
        }

        return validate;
    }

    public static boolean validateNICOld(String nic) {
        int length = nic.length();
        if (length != 10) {
            return false;
        } else {
            char lastChar = nic.charAt(length - 1);
            String lastCharacter = String.valueOf(lastChar);
            if (lastCharacter.equalsIgnoreCase("v") || lastCharacter.equalsIgnoreCase("x")) {
                String number = nic.substring(0, length - 1);
                Log.e("NUmber", number);
                if (!number.trim().matches("/^[0-9]{9}/")&& TextUtils.isDigitsOnly(number)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                for (int i = 0; i < length - 2; i++) {
                    char currentChar = nic.charAt(i);
                    if (currentChar < '0' || '9' < currentChar) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static boolean validateNICNew(String nic) {
        int length = nic.length();
        if (length != 12) {
            return false;
        } else {
            Log.e("NIC", nic);
            if (!nic.trim().matches("/[0-9]{12}/")) {
                return true;
            } else {
                return false;
            }
        }
    }

}
