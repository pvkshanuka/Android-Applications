package com.example.myaccount;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import DB.SQLiteHelper;

public class ActivityUpdateCus extends AppCompatActivity {

    EditText et_fname, et_lname, et_cno, et_email, et_pword;

    Button btn_edit, btn_delete;

    int cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        init();

        Bundle userdata = getIntent().getBundleExtra("userdata");

        cid = userdata.getInt("cid");
        et_cno.setText(userdata.getInt("cno") + "");
        et_fname.setText(userdata.getString("fname"));
        et_lname.setText(userdata.getString("lname"));
        et_email.setText(userdata.getString("email"));
        et_pword.setText(userdata.getString("pword"));

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_fname.getText().toString().equals("") || et_lname.getText().toString().equals("") || et_cno.getText().toString().equals("") || et_email.getText().toString().equals("") || et_pword.getText().toString().equals("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUpdateCus.this);
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
//                        System.out.println(et_cno.getText().toString().length() + " >>>>>>>>>>>>>>>>>>>");
                        if (et_cno.getText().toString().length() >= 9 && et_cno.getText().toString().length() <= 12) {

                            String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
                            if (et_email.getText().toString().matches(regex)) {

                                SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
                                SQLiteDatabase database = helper.getWritableDatabase();
                                database.execSQL("UPDATE customer SET fname='" + et_fname.getText().toString() + "',lname='" + et_lname.getText().toString() + "',cno='" + et_cno.getText().toString() + "',email='" + et_email.getText().toString() + "',pword='" + et_pword.getText().toString() + "' WHERE cid='" + cid + "'");
                                System.out.println("Updated");

                                Toast.makeText(ActivityUpdateCus.this, "Customer Updated Successfully.", Toast.LENGTH_SHORT).show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUpdateCus.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUpdateCus.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUpdateCus.this);
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

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUpdateCus.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you suer you want to DELETE Customer ID-" + cid).
                        setCancelable(false);

                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
                        SQLiteDatabase database = helper.getWritableDatabase();
                        database.execSQL("DELETE FROM customer WHERE cid='" + cid + "'");

                        Toast.makeText(ActivityUpdateCus.this, "Customer Deleted Successfully. ID=" + cid, Toast.LENGTH_LONG).show();

                        onBackPressed();
                    }
                });
                builder.create().show();


            }
        });

    }

    private void init() {

        et_fname = findViewById(R.id.et_fnamee);
        et_lname = findViewById(R.id.et_lnamee);
        et_cno = findViewById(R.id.et_cnoed);
        et_email = findViewById(R.id.et_emaile);
        et_pword = findViewById(R.id.et_pworde);

        btn_edit = findViewById(R.id.btn_edcus);
        btn_delete = findViewById(R.id.btn_dec);

    }
}
