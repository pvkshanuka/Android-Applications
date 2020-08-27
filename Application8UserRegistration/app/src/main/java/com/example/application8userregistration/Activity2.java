package com.example.application8userregistration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.User;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Button btn_load = findViewById(R.id.button2);

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        HttpURLConnection connection = null;

                        try {

                            URL url = new URL("http://192.168.12.2:8080/AndroidWeb/View");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setChunkedStreamingMode(0);
                            System.out.println(connection.getResponseCode()+"-"+connection.getRequestMethod()+"  ////////////////////////");


                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){

                                InputStream inputStream = connection.getInputStream();
                                InputStreamReader streamReader = new InputStreamReader(inputStream);
                                BufferedReader bufferedReader = new BufferedReader(streamReader);

                                String responseText = bufferedReader.readLine();

                                Gson gson = new Gson();
                                TypeToken<ArrayList<User>> token = new TypeToken<ArrayList<User>>(){};
                                ArrayList<User> users = gson.fromJson(responseText,token.getType());

                                for (User u:users){
                                    System.out.println(u.getId());
                                    System.out.println(u.getName());
                                    System.out.println(u.getMobile());
                                    System.out.println(u.getPassword());
                                }

                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            if (connection != null){
                                connection.disconnect();
                            }
                        }

                    }
                }).start();



            }
        });

    }
}
