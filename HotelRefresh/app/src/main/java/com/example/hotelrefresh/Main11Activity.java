package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main11Activity extends AppCompatActivity {

    private Button button09;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);

        button09 = findViewById(R.id.button9);

        button09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent6 = new Intent(  Main11Activity.this, Main12Activity.class);
                intent6.putExtra(  "first",  0);
                startActivity(intent6);
            }
        });
    }
}
