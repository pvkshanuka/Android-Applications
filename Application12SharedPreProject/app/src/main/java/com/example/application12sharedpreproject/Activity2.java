package com.example.application12sharedpreproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Button btn_sin = findViewById(R.id.button3);
        Button btn_nxt = findViewById(R.id.button4);

        btn_sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = ((EditText) findViewById(R.id.editText6)).getText().toString();
                String pword = ((EditText) findViewById(R.id.editText5)).getText().toString();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Activity2.this);

                Map<String, ?> data = sharedPreferences.getAll();
                boolean isFound = false;
                for (Map.Entry<String,?> e: data.entrySet()){

                    if (e.getValue().equals(name)){

                        String id = e.getKey().split("name")[0];
                        String pword2 = sharedPreferences.getString(id+"pword",null);

                        if (pword2.equals(pword)){
                            isFound = true;
                            break;
                        }

                    }

                }

                if (isFound){
                    Toast.makeText(Activity2.this, "Login Success!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Activity2.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity2.this,Activity3.class);
                startActivity(intent);

            }
        });

    }
}
