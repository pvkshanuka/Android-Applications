package com.example.application18firebaseuserreg;

import android.content.Intent;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Button btn_choose = findViewById(R.id.button2);

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto(v);
            }
        });

    }

    private void choosePhoto(View v) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 101){
            if (resultCode == RESULT_OK){
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageURI(data.getData());
            }else{
                Toast.makeText(this, "Please Select Image.!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
