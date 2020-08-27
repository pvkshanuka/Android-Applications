package com.example.myaccount;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import DB.SQLiteHelper;

public class ActivityAddCus extends AppCompatActivity {

    EditText et_fname, et_lname, et_cno, et_email, et_pword, et_pwordre;

    Button btn_regcus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        loadAllCus();

        btn_regcus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_fname.getText().toString().equals("") || et_lname.getText().toString().equals("") || et_cno.getText().toString().equals("") || et_email.getText().toString().equals("") || et_pword.getText().toString().equals("") || et_pwordre.getText().toString().equals("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddCus.this);
                    builder.setTitle("Registration Error");
                    builder.setMessage("Some Details are Empty.!").
                            setCancelable(false);

                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();

                } else {

                    if (et_pword.getText().toString().length() > 3) {
                        System.out.println(et_cno.getText().toString().length() + " >>>>>>>>>>>>>>>>>>>");
                        if (et_cno.getText().toString().length() >= 9 && et_cno.getText().toString().length() <= 12) {

                            if (et_pword.getText().toString().equals(et_pwordre.getText().toString())) {

                                String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
                                if (et_email.getText().toString().matches(regex)) {

                                    SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
                                    SQLiteDatabase database = helper.getWritableDatabase();
                                    database.execSQL("INSERT INTO customer(fname,lname,cno,email,pword,status) VALUES ('" + et_fname.getText().toString() + "','" + et_lname.getText().toString() + "','" + et_cno.getText().toString() + "','" + et_email.getText().toString() + "','" + et_pword.getText().toString() + "',1)");

                                    et_pword.setText("");
                                    et_fname.setText("");
                                    et_lname.setText("");
                                    et_email.setText("");
                                    et_pwordre.setText("");
                                    et_cno.setText("");

                                    String selectQuery = "SELECT  * FROM " + "customer";
                                    Cursor cursor = database.rawQuery(selectQuery, null);
                                    cursor.moveToLast();

                                    Toast.makeText(ActivityAddCus.this, "Customer Registered Successfully. ID-"+cursor.getInt(0), Toast.LENGTH_LONG).show();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddCus.this);
                                    builder.setTitle("Registration Error");
                                    builder.setMessage("Invalid Email Address.").
                                            setCancelable(false);

                                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.create().show();
                                }

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddCus.this);
                                builder.setTitle("Registration Error");
                                builder.setMessage("Passwords are not matching").
                                        setCancelable(false);

                                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.create().show();
                            }

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddCus.this);
                            builder.setTitle("Registration Error");
                            builder.setMessage("Invalid Mobile No").
                                    setCancelable(false);

                            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.create().show();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddCus.this);
                        builder.setTitle("Registration Error");
                        builder.setMessage("Password is too short").
                                setCancelable(false);

                        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                    }

                }
            }
        });

    }

    private void loadAllCus() {

        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM customer WHERE status=1", null);

        while (cursor.moveToNext()) {

            System.out.println("_________________________");
            System.out.println(cursor.getString(0));
            System.out.println(cursor.getString(1));
            System.out.println(cursor.getString(2));
            System.out.println(cursor.getString(3));
            System.out.println(cursor.getString(4));
            System.out.println(cursor.getString(5));
            System.out.println(cursor.getString(6));

        }

    }


    private void init() {

        et_fname = findViewById(R.id.et_fnamer);
        et_lname = findViewById(R.id.et_lnamer);
        et_cno = findViewById(R.id.et_cnor);
        et_email = findViewById(R.id.et_emailr);
        et_pword = findViewById(R.id.et_pwordr);
        et_pwordre = findViewById(R.id.et_pwordrer);

        btn_regcus = findViewById(R.id.btn_regcus);

    }
}
