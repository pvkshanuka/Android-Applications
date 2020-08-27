package com.example.app26broadcastingpractical;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Activity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Activity1 OnCreate", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Activity1 OnResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Activity1 OnPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Activity1 OnDestroy", Toast.LENGTH_SHORT).show();
    }

}
