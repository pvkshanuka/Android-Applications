package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityAdmin extends AppCompatActivity {

    Button btn_addp, btn_editp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        init();


        btn_addp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityAdmin.this, ActivityPackageAdd.class);
                startActivity(intent);

            }
        });

        btn_editp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityAdmin.this, ActivityPackageViewA.class);
                startActivity(intent);

            }
        });

    }

    private void init() {

        btn_addp = findViewById(R.id.btn_addpackagea);
        btn_editp = findViewById(R.id.btn_editpackagea);

    }
}
