package com.example.application2practical;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name = findViewById(R.id.txt_uname);
                EditText email = findViewById(R.id.txt_email);
                EditText mobile = findViewById(R.id.txt_mobile);
                EditText password = findViewById(R.id.txt_pword);
                RadioButton male = findViewById(R.id.rdbtn_male);
                RadioButton female = findViewById(R.id.rdbtn_female);
                CheckBox cb = findViewById(R.id.checkBox);

                if (name.getText().toString().isEmpty()) {
                    Toast t = Toast.makeText(MainActivity.this, "Please Fill Username", Toast.LENGTH_SHORT);
                    t.show();
                } else if (email.getText().toString().isEmpty()) {
                    Toast t = Toast.makeText(MainActivity.this, "Please Fill Email", Toast.LENGTH_SHORT);
                    t.show();
                } else if (mobile.getText().toString().isEmpty()) {
                    Toast t = Toast.makeText(MainActivity.this, "Please Fill Mobile", Toast.LENGTH_SHORT);
                    t.show();
                } else if (password.getText().toString().isEmpty()) {
                    Toast t = Toast.makeText(MainActivity.this, "Please Fill Password", Toast.LENGTH_SHORT);
                    t.show();
                } else if (!(male.isChecked() || female.isChecked())) {
                    Toast t = Toast.makeText(MainActivity.this, "Please Select Gender", Toast.LENGTH_SHORT);
                    t.show();
                } else if (!cb.isChecked()) {
                    Toast t = Toast.makeText(MainActivity.this, "Please Agree Agrement", Toast.LENGTH_SHORT);
                    t.show();
                }else {
                    Toast.makeText(MainActivity.this, "All Complete", Toast.LENGTH_SHORT).show();

                    String gen = "";

                    if(male.isChecked()){
                        gen="Male";
                    }else{
                        gen="Female";
                    }

                    User user = new User(name.getText().toString(),email.getText().toString(),mobile.getText().toString(),password.getText().toString(),gen);
                    User.userlist.add(user);

                    Intent intent = new Intent(getApplicationContext(),UserDetailsView.class);
                    intent.putExtra("msg","Sign Up Sucess");
                    startActivity(intent);

                }

            }
        });

    }
    }
