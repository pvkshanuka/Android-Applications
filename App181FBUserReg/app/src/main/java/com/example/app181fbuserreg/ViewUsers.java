package com.example.app181fbuserreg;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

import Models.User;

public class ViewUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usertbl = database.getReference("user");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                GenericTypeIndicator<Map<String,User>> indicator = new GenericTypeIndicator<Map<String,User>>(){};
//
//                Map<String, User>  userMap = dataSnapshot.getValue(indicator);
//
//                for(Map.Entry<String,User> e: userMap.entrySet()){

                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    User user = ds.getValue(User.class);

//                    ((TextView) findViewById(R.id.textView11)).setText(e.getValue().getName());
//                    ((TextView) findViewById(R.id.textView12)).setText(e.getValue().getMobileno());
//                    ((TextView) findViewById(R.id.textView13)).setText(e.getValue().getEmail());
//                    ((TextView) findViewById(R.id.textView14)).setText(e.getValue().getPword());
//                    ((TextView) findViewById(R.id.textView15)).setText(e.getValue().getGender());

                    ((TextView) findViewById(R.id.textView11)).setText(user.getName());
                    ((TextView) findViewById(R.id.textView12)).setText(user.getMobileno());
                    ((TextView) findViewById(R.id.textView13)).setText(user.getEmail());
                    ((TextView) findViewById(R.id.textView14)).setText(user.getPword());
                    ((TextView) findViewById(R.id.textView15)).setText(user.getGender());

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference userFolder = storage.getReference("user");
//                    userFolder.child(ds.getKey());
                    StorageReference file = userFolder.child(user.getImg());

                    final ImageView iv = findViewById(R.id.imageView2);

                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getApplicationContext()).load(uri).into(iv);
                            Toast.makeText(ViewUsers.this, "User Load Successfuly.!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        usertbl.addListenerForSingleValueEvent(valueEventListener);

    }
}
