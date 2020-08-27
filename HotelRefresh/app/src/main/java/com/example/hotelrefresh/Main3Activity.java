package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import  android.widget.ImageButton;
import android.widget.ImageView;

public class Main3Activity extends AppCompatActivity {

    private Button button;
    private ImageButton button1;
    private  ImageButton button3;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        button = findViewById(R.id.button2);
        button1 = findViewById(R.id.imageButton3);
        button3 = findViewById(R.id.imageButton6);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(  Main3Activity.this, Main7Activity.class);
                intent5.putExtra(  "four",  0);
                startActivity(intent5);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent( Main3Activity.this, Main8Activity.class);
                intent1.putExtra( "first",  0);
                startActivity(intent1);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openMain4Activity();}

            private void openMain4Activity(){
                Intent intent01 = new Intent(  Main3Activity.this, Main4Activity.class);
                intent01.putExtra( "first",  0);
                startActivity(intent01);
            }
        });


    }
}
