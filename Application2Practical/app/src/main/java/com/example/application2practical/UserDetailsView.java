package com.example.application2practical;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import model.User;

public class UserDetailsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_view);

        Intent intent=getIntent();
        String msg = intent.getStringExtra("msg");

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        TextView tv = findViewById(R.id.textView7);
        tv.setText(msg);


        Button btn = findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText email = findViewById(R.id.editText);
                EditText pword = findViewById(R.id.editText2);
                System.out.println(email.getText().toString()+"-"+pword.getText().toString());
                for (User u:User.userlist){
                System.out.println("DATA  "+u.getEmail()+"-"+u.getPassword());

                    if (u.getEmail().equals(email.getText().toString()) && u.getPassword().equals(pword.getText().toString())){
                        Toast.makeText(UserDetailsView.this, "Found", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(UserDetailsView.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

}
