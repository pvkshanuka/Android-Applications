package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class Main2Activity extends AppCompatActivity {

    private Button button;
    private ImageButton button3;
    private ImageButton button6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button = findViewById(R.id.button2);
        button3 = findViewById(R.id.imageButton);
        button6 = findViewById(R.id.imageButton5);


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(Main2Activity.this,Main5Activity.class);
                intent4.putExtra("five",0);
                startActivity(intent4);
            }
        });


        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent7 = new Intent( Main2Activity.this, Main6Activity.class);
                intent7.putExtra( "first", 0);
               startActivity(intent7);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openMain3Activity();}

            private void openMain3Activity (){
                Intent intent01 = new Intent(Main2Activity.this, Main3Activity.class);
                intent01.putExtra("first", 0);
                startActivity(intent01);


            }

        });
    }
}
