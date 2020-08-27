package com.example.application16firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_1 = findViewById(R.id.button);
        Button btn_2 = findViewById(R.id.button2);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insert(v);

            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update(v);

            }
        });

    }

    private void update(View v) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("name");
        databaseReference.setValue("Shanuka");
    }


    public void insert(View v){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("user");
        User user = new User(01,"Kusal",0772101676,"1234");
        databaseReference.setValue(user);
    }

}
