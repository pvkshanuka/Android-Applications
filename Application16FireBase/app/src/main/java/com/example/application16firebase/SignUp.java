package com.example.application16firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.User;

public class SignUp extends AppCompatActivity {

    EditText et_id;
    EditText et_name;
    EditText et_mobile;
    EditText et_pword;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_id = findViewById(R.id.editText);
        et_name = findViewById(R.id.editText2);
        et_mobile = findViewById(R.id.editText3);
        et_pword = findViewById(R.id.editText4);


        Button btn_sup = findViewById(R.id.button3);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);

    btn_sup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {



        }
    });

    }

    private void signUp(View v) {

        int id = Integer.parseInt(et_id.getText().toString());
        String name = et_name.getText().toString();
        int mobile = Integer.parseInt(et_mobile.getText().toString());
        String pword = et_pword.getText().toString();



        User user = new User(id,name,mobile,pword);

        DatabaseReference reference = firebaseDatabase.getReference("user");

        DatabaseReference push = reference.push();

        push.setValue(user);

        Toast.makeText(this, "Sign Up Pressed.!", Toast.LENGTH_SHORT).show();
    }

}
