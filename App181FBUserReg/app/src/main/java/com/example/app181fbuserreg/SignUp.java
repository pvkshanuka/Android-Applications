package com.example.app181fbuserreg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

import Models.User;

public class SignUp extends AppCompatActivity {

    InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button btn_choose = findViewById(R.id.button);
        Button btn_signup = findViewById(R.id.button2);
        Button btn_vusers = findViewById(R.id.button3);

        btn_vusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUp.this,ViewUsers.class);
                startActivity(intent);

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData(v);

            }
        });

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

    }

    private void saveData(View v) {

        EditText txt_name = findViewById(R.id.editText);
        EditText txt_mobileno = findViewById(R.id.editText2);
        EditText txt_email = findViewById(R.id.editText3);
        EditText txt_pword = findViewById(R.id.editText4);

        RadioGroup group = findViewById(R.id.radioGroup);

        RadioButton radioButton = findViewById(group.getCheckedRadioButtonId());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usertbl = database.getReference("user");
        final DatabaseReference row = usertbl.push();

        final User user = new User(txt_name.getText().toString(),txt_mobileno.getText().toString(),txt_email.getText().toString(),txt_pword.getText().toString(),radioButton.getText().toString());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference userfol = storage.getReference("user");
        StorageReference storageFile = userfol.child(row.getKey());

        UploadTask uploadTask = storageFile.putStream(is);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                user.setImg(row.getKey());
                row.setValue(user);
                Toast.makeText(SignUp.this, "User Savedd Successfuly", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void choosePhoto() {

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

                try {
                    is = getContentResolver().openInputStream(data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(this, "Please Select Image.!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
