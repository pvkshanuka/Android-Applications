package com.example.app32fragments2;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Button btn_f1 = findViewById(R.id.button4);
        Button btn_f2 = findViewById(R.id.button5);

        btn_f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment1();
            }
        });

        btn_f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment2();
            }
        });

    }

    private void fragment1() {

        Fragment2 fragment1 = new Fragment2();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.linear,fragment1,"f1");
        transaction.commit();

    }
    private void fragment2() {

        Fragment3 fragment2 = new Fragment3();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.linear,fragment2,"f2");
        transaction.commit();

    }

}
