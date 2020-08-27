package com.example.application8userregistration;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_signup = findViewById(R.id.button);
        Button btn_view = findViewById(R.id.button3);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_id =  ((EditText) findViewById(R.id.editText)).getText().toString();
                String txt_name =  ((EditText) findViewById(R.id.editText2)).getText().toString();
                String txt_mobile =  ((EditText) findViewById(R.id.editText3)).getText().toString();
                String txt_pword =  ((EditText) findViewById(R.id.editText4)).getText().toString();

                User user = new User(txt_id,txt_name,txt_mobile,txt_pword);

                Gson gson = new Gson();
                final String jsonString = gson.toJson(user);
                System.out.println(jsonString);

                AsyncTask asyncTask = new AsyncTask() {
                        String txt = "";
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        HttpURLConnection connection = null;


                        try {
                            URL url = new URL("http://192.168.12.2:8080/AndroidWeb/S1");

                            connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("POST");
                            connection.setChunkedStreamingMode(0);
                            connection.setDoOutput(true);
                            connection.setDoInput(true);

                            OutputStream outputStream = connection.getOutputStream();
                            String data = "json="+jsonString;
                            outputStream.write(data.getBytes());
                            outputStream.flush();
                            outputStream.close();

                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){

                                InputStream inputStream = connection.getInputStream();


                                int no;

                                while ((no = inputStream.read()) != -1){
                                    txt+= (char) no;
                                }

                                System.out.println(txt);

                                inputStream.close();

                                publishProgress();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            if (connection != null){
                                connection.disconnect();
                            }
                        }

                        return null;
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                    }

                    @Override
                    protected void onProgressUpdate(Object[] values) {
                        Toast.makeText(MainActivity.this, txt, Toast.LENGTH_SHORT).show();
                    }
                };
                asyncTask.execute();

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpURLConnection connection = null;
//
//                        try {
//                            URL url = new URL("http://192.168.12.2:8080/AndroidWeb/S1");
//
//                            connection = (HttpURLConnection) url.openConnection();
//
//                            connection.setRequestMethod("POST");
//                            connection.setChunkedStreamingMode(0);
//                            connection.setDoOutput(true);
//                            connection.setDoInput(true);
//
//                            OutputStream outputStream = connection.getOutputStream();
//                            String data = "json="+jsonString;
//                            outputStream.write(data.getBytes());
//                            outputStream.flush();
//                            outputStream.close();
//
//                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
//
//                                InputStream inputStream = connection.getInputStream();
//
//                                String txt = "";
//                                int no;
//
//                                while ((no = inputStream.read()) != -1){
//                                    txt+= (char) no;
//                                }
//
//                                System.out.println(txt);
//
//                                inputStream.close();
//
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }finally {
//                            if (connection != null){
//                                connection.disconnect();
//                            }
//                        }
//
//                    }
//                }).start();



            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Activity2.class);
                startActivity(intent);

            }
        });

    }
}
