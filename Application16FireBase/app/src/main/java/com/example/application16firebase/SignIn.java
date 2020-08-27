package com.example.application16firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.User;


public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button btn_signin = findViewById(R.id.button13);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn(v);

            }
        });

    }

    private void signIn(View v) {
        final String uname = ((EditText) findViewById(R.id.editText5)).getText().toString();
        final String pword = ((EditText) findViewById(R.id.editText6)).getText().toString();

        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        DatabaseReference r1 = fbdb.getReference("user");

        System.out.println("Pressed2.!");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

        System.out.println("Pressed.!");

                User u = dataSnapshot.getValue(User.class);

                if (u.getName().equals(uname) && u.getPword().equals(pword)){
                    Toast.makeText(SignIn.this, "Sign In Success.! |"+u.getName()+"|", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignIn.this, "Sign in Failed.!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        };

        r1.addListenerForSingleValueEvent(valueEventListener);

    }
}
