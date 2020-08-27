package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main9Activity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);

        button = findViewById(R.id.button7);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openMain11Activity();}

                private void openMain11Activity (){
                   Intent intent01 = new Intent(  Main9Activity.this, Main11Activity.class);
                   intent01.putExtra(  "first",  0);
                   startActivity(intent01);

                    }

        });
    }
}
