package com.example.application16firebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.User;


public class SignUp2 extends AppCompatActivity {

    Button btn_addData;
    Button btn_addData2;
    Button btn_changp;
    DatabaseReference users;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);

        users = firebaseDatabase.getReference("users");

        btn_addData = findViewById(R.id.button4);
        btn_addData2 = findViewById(R.id.button5);
        btn_changp = findViewById(R.id.button6);

        btn_addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(v);
            }
        });

        btn_addData2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData2(v);
            }
        });

        btn_changp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPiority(v);
            }
        });

    }


    private void addData(View v) {

        User user = new User(33,"Chammika",111111,"5678");

//        firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseDatabase.setPersistenceEnabled(true);
//
//        users = firebaseDatabase.getReference("users");
        users.push().setValue(user);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = (User) dataSnapshot.getValue(User.class);

                Toast.makeText(SignUp2.this, u.getName()+" Changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        users.addValueEventListener(valueEventListener);

    }


    private void addData2(View v) {

//        firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseDatabase.setPersistenceEnabled(true);
//
//        DatabaseReference users = firebaseDatabase.getReference("users");
        DatabaseReference pword = users.child("pword");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String pw = dataSnapshot.getValue().toString();

                Toast.makeText(SignUp2.this, "password Changed "+pw, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        pword.addValueEventListener(valueEventListener);

    }

    private void setPiority(View v){

//        Null mulin
//        nombers piliwelata itapasse
//        piority string eka piliwelata itapasse
//        same piority nm key eke alphabat piliwelata


        users.child("pword").setPriority(null);
        users.child("mobile").setPriority(1);
        users.child("name").setPriority(2);
        users.child("id").setPriority("1");

    }
}
