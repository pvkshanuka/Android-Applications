package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityHome extends AppCompatActivity {

    Button btn_ad, btn_cus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        btn_cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityHome.this, MainActivity.class);
                startActivity(intent);

            }
        });

        btn_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityHome.this, ActivityAdmin.class);
                startActivity(intent);

            }
        });

    }

    private void init() {

        btn_cus = findViewById(R.id.btn_cush);
        btn_ad = findViewById(R.id.btn_admh);

    }
}
