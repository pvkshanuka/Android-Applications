package com.example.myaccount;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import DB.SQLiteHelper;

public class ActivityHome extends AppCompatActivity {

    EditText et_cusid;

    Button btn_loadcus, btn_upcus, btn_addcus;

    int cid;
    String fname;
    String lname;
    int cno;
    String email;
    String pword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        btn_upcus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cid == 0 || fname == null || lname == null || cno == 0 || email == null || pword == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHome.this);
                    builder.setTitle("Customer Update Error");
                    builder.setMessage("Please Load Customer.").
                            setCancelable(false);

                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();

                } else {

                    Intent intent = new Intent(ActivityHome.this, ActivityUpdateCus.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("cid", cid);
                    bundle.putString("fname", fname);
                    bundle.putString("lname", lname);
                    bundle.putInt("cno", cno);
                    bundle.putString("email", email);
                    bundle.putString("pword", pword);

                    intent.putExtra("userdata", bundle);
                    startActivity(intent);


                }
            }
        });

        btn_addcus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityHome.this, ActivityAddCus.class);
                startActivity(intent);
            }
        });


        btn_loadcus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
                SQLiteDatabase database = helper.getWritableDatabase();
                Cursor cursor = database.rawQuery("SELECT * FROM customer WHERE status=1 AND cid='" + et_cusid.getText().toString() + "'", null);

                if (cursor.moveToNext()) {

                    System.out.println("_________________________");
                    System.out.println(cursor.getString(0));
                    System.out.println(cursor.getString(1));
                    System.out.println(cursor.getString(2));
                    System.out.println(cursor.getString(3));
                    System.out.println(cursor.getString(4));
                    System.out.println(cursor.getString(5));
                    System.out.println(cursor.getString(6));

                    cid = cursor.getInt(0);
                    fname = cursor.getString(1);
                    lname = cursor.getString(2);
                    cno = cursor.getInt(3);
                    email = cursor.getString(4);
                    pword = cursor.getString(5);

                    Toast.makeText(ActivityHome.this, "Customer Loaded.!", Toast.LENGTH_SHORT).show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHome.this);
                    builder.setTitle("Customer Load Error");
                    builder.setMessage("Invalid Customer ID.").
                            setCancelable(false);

                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                }

            }
        });

    }

    private void init() {

        et_cusid = findViewById(R.id.et_cusidh);

        btn_loadcus = findViewById(R.id.btn_loadcush);
        btn_upcus = findViewById(R.id.btn_editcus);
        btn_addcus = findViewById(R.id.btn_regcus);

    }
}
