package com.example.app22runtimepermition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Practical extends AppCompatActivity {
//boolean camper = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Button btncall = findViewById(R.id.button3);
        Button btncam = findViewById(R.id.button4);

        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPermCheck(v);
            }
        });

        btncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camPermCheck(v);
            }
        });

    }

    private void camPermCheck(View v) {

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Required Permission.!", Toast.LENGTH_SHORT).show();

            String permissions[] = new String[1];
            permissions[0] = Manifest.permission.CAMERA;

            requestPermissions(permissions,111);
//            if (camper) {
//                openCam(v);
//            }
        }

    }


    private void callPermCheck(View v) {
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_CALL);

            Uri uri = Uri.parse("tel:0772101676");
            intent.setData(uri);

            startActivity(intent);
        }else{
            Toast.makeText(this, "Required Permission.!", Toast.LENGTH_SHORT).show();

            String permissions[] = new String[2];
            permissions[0] = Manifest.permission.CALL_PHONE;
            permissions[1] = Manifest.permission.SEND_SMS;

            requestPermissions(permissions,112);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 111){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                try {
//                    PermissionInfo permissionInfo = getPackageManager().getPermissionInfo(permissions[0], PackageManager.GET_META_DATA);
//                    String details = permissionInfo.loadLabel(getPackageManager()).toString();
//                    Toast.makeText(this, "Permission Granted to "+details, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                    startActivity(intent);

//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }


//                Toast.makeText(this, "Phone Call Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Error Try Again", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == 112){

            boolean allPermissionGranted = true;

            for (int i = 0; i < grantResults.length;i++){

                if (grantResults[i]==PackageManager.PERMISSION_GRANTED){

//                    try {
//                        PermissionInfo permissionInfo = getPackageManager().getPermissionInfo(permissions[i], PackageManager.GET_META_DATA);
//                        String details = permissionInfo.loadLabel(getPackageManager()).toString();
//                        Toast.makeText(this, "Permission Granted to "+details, Toast.LENGTH_SHORT).show();
//
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }

                }else {
                    allPermissionGranted = false;
                    break;
                }

            }

            if (allPermissionGranted){
                Intent intent = new Intent(Intent.ACTION_CALL);

                Uri uri = Uri.parse("tel:0772101676");
                intent.setData(uri);

                startActivity(intent);
            }else{
                Toast.makeText(this, "Permission Grant Failed.!", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
