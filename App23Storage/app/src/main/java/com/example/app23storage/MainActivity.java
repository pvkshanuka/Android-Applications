package com.example.app23storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_cnfin = findViewById(R.id.button2);
        Button btn_cnfex = findViewById(R.id.button);
        Button btn_dwnimg = findViewById(R.id.button4);
        Button btn_nomedia = findViewById(R.id.button3);
        Button btn_cexprfi = findViewById(R.id.button5);

        btn_cnfin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileCreateInternal(v);
            }
        });

        btn_cnfex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileCreateExternal(v);
            }
        });

        btn_dwnimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage(v);
            }
        });

        btn_nomedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noMedia(v);
            }
        });

        btn_cexprfi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExternalPrivateFile(v);
            }
        });

    }

    private void createExternalPrivateFile(View v) {

        try {

//            External Private Dir ganna widiha
        File externalFolder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        File privateFile = new File(externalFolder,"data.txt");
        privateFile.createNewFile();

        String data = "Password:123";
        FileOutputStream outputStream = new FileOutputStream(privateFile);
        outputStream.write(data.getBytes());

//        FILE DELETE

//            Any Location
//            privateFile.delete();

//            Internal Storage
//            deleteFile("");

//            GET FREE SPACE
            long freeGB = externalFolder.getFreeSpace()/(1024*1024*1024);
//            GET TOTAL SPACE
            long totalGB = externalFolder.getTotalSpace()/(1024*1024*1024);
            System.out.println(freeGB);
            System.out.println(totalGB);

//            FileInputStream inputStream = new FileInputStream(privateFile);
//            InputStreamReader reader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(reader);
//
//            Toast.makeText(this, bufferedReader.readLine(), Toast.LENGTH_SHORT).show();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void noMedia(View v) {
        try {

//            Me .nomedia file eka haduwama e folder eke thiyana media files gallery eke pennanne na

            File folderDCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File folderCamera = new File(folderDCIM,"Camera");
            File fileNoMedia = new File(folderCamera,"no.nomedia");
            fileNoMedia.createNewFile();

            Toast.makeText(this, "No Media.!", Toast.LENGTH_SHORT).show();
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void downloadImage(View v) {

        Thread thread1 = new Thread(){
            @Override
            public void run() {

                try {

                    URL url = new URL("https://image.flaticon.com/icons/png/512/37/37112.png");
                    InputStream inputStream = url.openStream();

                    File folderPicture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                    File folderAndroid = new File(folderPicture, "Android");
                    folderAndroid.mkdir();

                    File fileImg = new File(folderAndroid, "logo.png");
                    fileImg.createNewFile();
                    
                    FileOutputStream fileOutputStream = new FileOutputStream(fileImg);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };

        thread1.start();

    }

    private void fileCreateExternal(View v) {

        String state = Environment.getExternalStorageState();

        if (state.equals((Environment.MEDIA_MOUNTED))){
            try {
            Toast.makeText(this, "External Storage Found.!", Toast.LENGTH_SHORT).show();

//            External Folders Ganna widiha
            File folderDocument = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

            File file1 = new File(folderDocument,"android.txt");
            file1.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(this, "External Storage Not Found.!", Toast.LENGTH_SHORT).show();
        }

    }

    private void fileCreateInternal(View v) {

        try {
//            FILE WRITE
//                Internal File Dir ganna widiha
            File appFolder = getFilesDir();

//                Internal Cache File Dir ganna widiha
//            File appFolder = getCacheDir();
//            File file1 = File.createTempFile("abc","xyz");
            File file1 = new File(appFolder,"a.txt");
//            file1.createNewFile();

//            FileOutputStream fileOutputStream = new FileOutputStream(file1);
//            FileOutputStream fileOutputStream = openFileOutput("b.txt",MODE_PRIVATE);
//            String txt = "Hello Kusal";
//            fileOutputStream.write(txt.getBytes());


//            FILE READ

            FileInputStream fileInputStream = new FileInputStream(file1);
//            int data = 0;
//            String read = "";
//            while ((data = fileInputStream.read()) !=-1){
//                char dataChar = (char)data;
//                read += dataChar;
//            }
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

//            Toast.makeText(this, read, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, bufferedReader.readLine(), Toast.LENGTH_SHORT).show();

//            GETTING FILE LIST OF INTERNAL STORAGE (Not cache)

            String fileNames[] = fileList();

            for (String name: fileNames){
                System.out.println(name);
            }

//            ACCESS RAW RESOURCE FOLDER FILES

            InputStream inputStream = getResources().openRawResource(R.raw.java);
            InputStreamReader reader1 = new InputStreamReader(inputStream);
            BufferedReader bufferedReader1 = new BufferedReader(reader1);

            String line ="";

            while ((line=bufferedReader1.readLine()) != null){
                System.out.println(line);
            }

//            CREATE FOLDER IN INTERNAL STORAGE

//            Thiayanwa nm thiyana eka gannawa nathnm aluthin ekak hadnawa
            File folder1 = getDir("Folder1", MODE_PRIVATE);
            File file = new File(folder1,"android.txt");
            file.createNewFile();

//            getFilesDir();    internal storage eke location eka ganna method eka
//            getCacheDir();    internal storage eke cache eke location eka ganna method eka
//            getDir(); internal storage eke dir ekak hadanna & ganna use karana method eka


//            All in one code

            File filesDir = getFilesDir();

            File androidFilder = new File(filesDir,"Android");
            androidFilder.mkdir();

            File file2 = new File(androidFilder,"a.txt");
            file2.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(file2);
            String text = "Hello Android";
            outputStream.write(text.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
