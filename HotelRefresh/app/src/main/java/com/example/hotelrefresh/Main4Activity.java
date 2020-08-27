package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class Main4Activity extends AppCompatActivity {;

    private ImageButton button4;
    private ImageButton button8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        button4 = findViewById(R.id.imageButton2);
        button8 = findViewById(R.id.imageButton8);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent( Main4Activity.this, Main9Activity.class);
                intent5.putExtra( "first",  0);
                startActivity(intent5);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent( Main4Activity.this, Main10Activity.class);
                intent01.putExtra( "first",  0);
                startActivity(intent01);
            }
        });

    }
}
