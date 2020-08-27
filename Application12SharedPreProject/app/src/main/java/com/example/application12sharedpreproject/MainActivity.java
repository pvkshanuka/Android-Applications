package com.example.application12sharedpreproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_sup = findViewById(R.id.button);
        Button btn_nxt = findViewById(R.id.button2);

        btn_sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = Integer.parseInt(((TextView) findViewById(R.id.editText)).getText().toString());
                String name = ((TextView) findViewById(R.id.editText2)).getText().toString();
                String pword= ((TextView) findViewById(R.id.editText3)).getText().toString();
                String no = ((TextView) findViewById(R.id.editText4)).getText().toString();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt(id+"id",id);
                editor.putString(id+"name",name);
                editor.putString(id+"pword",pword);
                editor.putString(id+"mobile",no);

                editor.apply();

                Toast.makeText(MainActivity.this, "User "+name+" Added Successfuly", Toast.LENGTH_SHORT).show();

            }
        });

        btn_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Activity2.class);
                startActivity(intent);

            }
        });

    }
}
