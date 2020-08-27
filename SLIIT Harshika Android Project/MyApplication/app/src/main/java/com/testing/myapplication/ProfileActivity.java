package com.testing.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    private TextView fname;
    private TextView lname;
    private TextView nic;
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fname = findViewById(R.id.userprofile_fname);
        lname = findViewById(R.id.userprofile_lastname);
        nic = findViewById(R.id.userprofile_nic);
        Bundle userdata = getIntent().getBundleExtra("userdata");
        fname.setText(userdata.getString("fname"));
        lname.setText(userdata.getString("lname"));
        nic.setText(userdata.getString("nic"));

        userid = userdata.getInt("userid");

    }

    public void openEditProfile(View view) {
        Intent i = new Intent(this, EditProfileActivity.class);
        System.out.println("userdata"+userid);
        i.putExtra("userid", userid);
        startActivity(i);
        //finish();

    }

    public void openOrders(View view) {
        Intent i = new Intent(this, ViewOrdersActivity.class);
        i.putExtra("userid", userid);
        startActivity(i);

    }


}
