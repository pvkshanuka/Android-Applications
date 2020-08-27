package com.example.application16firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.User;

public class Practical extends AppCompatActivity {

    Button btn_addu,btn_addcem,btn_chchna,btn_rechem,btn_mochpw,btn_addchli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical);

        btn_addu = findViewById(R.id.button7);
        btn_addcem = findViewById(R.id.button8);
        btn_chchna = findViewById(R.id.button9);
        btn_rechem = findViewById(R.id.button10);
        btn_mochpw = findViewById(R.id.button11);
        btn_addchli = findViewById(R.id.button12);

        btn_addu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser(v);
            }
        });

        btn_addcem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChildEmail(v);
            }
        });

        btn_chchna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeChildName(v);
            }
        });

        btn_rechem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeChildEmail(v);
            }
        });

        btn_mochpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveChildPword(v);
            }
        });

        btn_addchli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChildListener(v);
            }
        });

    }



    public void addUser(View v){

        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference r1 = fbd.getReference("user");

        User user = new User(1,"Kusal",25485,"1234");
        r1.setValue(user);

    }



    public void addChildEmail(View v){

        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference r1 = fbd.getReference("user");

        DatabaseReference r2 = r1.child("email");
        r2.setValue("pvkshanuka@gmil.com");

    }



    public void changeChildName(View v){

        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference r1 = fbd.getReference("user");

        DatabaseReference r2 = r1.child("name");
        r2.setValue("Shanuka");

    }



    public void removeChildEmail(View v){

        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference r1 = fbd.getReference("user");

        DatabaseReference r2 = r1.child("email");
        r2.setValue(null);
//        r2.removeValue();

    }



    public void moveChildPword(View v){

        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference r1 = fbd.getReference("user");

        r1.child("password").setPriority(1);
        r1.child("mobile").setPriority(2);
        r1.child("name").setPriority(3);
        r1.child("id").setPriority(4);

    }



    public void addChildListener(View v){

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("Added : "+dataSnapshot.getValue().toString());
                System.out.println("Added : "+s);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("Changed : "+dataSnapshot.getValue().toString());
                System.out.println("Changed : "+s);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Removed : "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("Moved : "+dataSnapshot.getValue().toString());
                System.out.println("Moved : "+s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference r1 = fbd.getReference("user");

        r1.addChildEventListener(childEventListener);

    }



}
