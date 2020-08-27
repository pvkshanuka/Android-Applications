package com.example.application12sharedpreproject;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Map;

import Models.User;

public class Activity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        Button btn_svj = findViewById(R.id.button5);
        Button btn_rej = findViewById(R.id.button6);
        Button btn_remo = findViewById(R.id.button7);

        final TextView txt_v3 = findViewById(R.id.textView3);

        final SharedPreferences sharedPreferences = getSharedPreferences("com.users",MODE_PRIVATE);

        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                System.out.println(key);

            }
        });

        btn_svj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user1 = new User(1,"Kusal","1234","0772101676");
                User user2 = new User(2,"Shanuka","5678","0752230333");

                Gson gson = new Gson();

                String jsonString1 = gson.toJson(user1);
                String jsonString2 = gson.toJson(user2);

//                SharedPreferences sharedPreferences = getSharedPreferences("com.users",MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("user1",jsonString1);
                editor.putString("user3",jsonString2);

                editor.apply();

                txt_v3.setText(sharedPreferences.getString("user1","No Data")+"--"+sharedPreferences.getString("user2","No Data"));

            }
        });

        btn_rej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("com.users", MODE_PRIVATE);

                System.out.println(sharedPreferences.contains("user1"));

                Gson gson= new Gson();
                User user = gson.fromJson(sharedPreferences.getString("user1", "No Data"), User.class);

                txt_v3.setText("Id-"+ user.getId()+"/ Name-"+user.getName()+"/ Password-"+user.getPassword()+"/ Mobile-"+user.getMobile());

                Map<String, ?> map_data = sharedPreferences.getAll();

                for (Map.Entry<String,?> e: map_data.entrySet()){

                    User u = gson.fromJson(e.getValue().toString(), User.class);
                    System.out.println(u.getId()+","+ u.getName()+","+ u.getPassword()+","+ u.getMobile());

                }

            }
        });

        btn_remo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

//                editor.remove("user3");

                editor.clear();

                editor.apply();

            }
        });

    }
}
