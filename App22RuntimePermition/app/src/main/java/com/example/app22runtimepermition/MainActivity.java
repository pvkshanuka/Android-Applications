package com.example.app22runtimepermition;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        Button button2 = findViewById(R.id.button2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClick(v);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2Click(v);
            }
        });

    }

    private void btn2Click(View v) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 111){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){

                try {

                    PermissionInfo permissionInfo = getPackageManager().getPermissionInfo(permissions[0], PackageManager.GET_META_DATA);
                    String details = permissionInfo.loadLabel(getPackageManager()).toString();
                    Toast.makeText(this, "Permission Granted to "+details, Toast.LENGTH_SHORT).show();

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


//                Toast.makeText(this, "Phone Call Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Error Try Again", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void btnClick(View v){

//        PackageManager.PERMISSION_GRANTED = 0
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(Intent.ACTION_CALL);

            Uri uri = Uri.parse("tel:0772101676");
            intent.setData(uri);

            startActivity(intent);
        }else{
            Toast.makeText(this, "Required Permission.!", Toast.LENGTH_SHORT).show();

            String permissions[] = new String[1];
            permissions[0] = Manifest.permission.CALL_PHONE;

            requestPermissions(permissions,111);

        }



    }

}
