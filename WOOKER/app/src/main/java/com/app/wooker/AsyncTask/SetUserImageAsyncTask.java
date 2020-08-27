package com.app.wooker.AsyncTask;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.app.wooker.ClientMain;
import com.app.wooker.Dialog_Lodaing;
import com.app.wooker.MainActivity;
import com.app.wooker.R;
import com.app.wooker.WorkerMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import CustomClasses.RunOnUIThread;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class SetUserImageAsyncTask extends AsyncTask implements ActivityCompat.OnRequestPermissionsResultCallback {


    String permissions[] = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
    String[] deniedPermissionsAccess;
    List<String> deniedList;
    boolean allPermissionGranted = true;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    CircleImageView circleImageView;

    Dialog_Lodaing dialog_lodaing;
    Activity activity;
    String from;

    public SetUserImageAsyncTask(Activity activity, String from) {
        this.activity = activity;
        this.from = from;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Object doInBackground(Object[] objects) {

        if (checkLocationPermissionsAreGranted()) {
            setImage();
        } else {
            deniedPermissionsAccess = new String[deniedList.size()];
            deniedPermissionsAccess = deniedList.toArray(deniedPermissionsAccess);

//            Toasty.warning(activity, "Need permissions to continue").show();
//                    .makeText(activity, "Need permissions to continue", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermissionsAccess[0])) {

                if (from.equals("client")) {

                    Snackbar.make(activity.findViewById(R.id.client_coor_lay), "App needs permissions to access read & write to Storage.!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ENABLE",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(activity, deniedPermissionsAccess, MainActivity.STORAGE_PERMITION_CODE);
                                        }
                                    })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                } else if (from.equals("worker")) {

                    Snackbar.make(activity.findViewById(R.id.worker_coor_lay), "App needs permissions to access read & write to Storage.!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ENABLE",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(activity, deniedPermissionsAccess, MainActivity.STORAGE_PERMITION_CODE);
                                        }
                                    })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                } else if (from.equals("main")) {

                    Snackbar.make(activity.findViewById(R.id.main_coor_lay), "App needs permissions to access read & write to Storage.!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ENABLE",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(activity, deniedPermissionsAccess, MainActivity.STORAGE_PERMITION_CODE);
                                        }
                                    })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                }
            } else {
                ActivityCompat.requestPermissions(activity, deniedPermissionsAccess, MainActivity.STORAGE_PERMITION_CODE);
            }
        }

        return null;
    }

    private void setImage() {
//        Toasty.success(activity, "AWAAAAAAAAAAAAAAAAAAAAAAAAAAAA").show();
        System.out.println("setImage AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        NavigationView navigationView = activity.findViewById(R.id.nav_view);


        if (from.equals("worker")) {
            circleImageView = navigationView.getHeaderView(0).findViewById(R.id.circleImageViewheaderw);
        } else if (from.equals("client")) {
            circleImageView = navigationView.getHeaderView(0).findViewById(R.id.circleImageViewheaderc);
        }
        File externalFolder = activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        final File userimgdir = new File(externalFolder + "/User Images");
//        User Image direcctory eka thiyanawada balanawa
        if (!userimgdir.exists()) {
//            nathnm user image dir eka hadanawa
            if (!userimgdir.mkdir()) {
                Toasty.error(activity, "Storage accessing failed.!", Toasty.LENGTH_LONG).show();
            }
        }

        System.out.println("setImage AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 2");

        final File privateFile = new File(userimgdir, FirebaseAuth.getInstance().getUid() + ".jpg");
//        userge image file ekak thiyanawada balanawa
        if (privateFile.exists()) {
            System.out.println("setImage AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 3");

//thiyanawa nm eka image view ekata load karanawa
            RunOnUIThread.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    if (BitmapFactory.decodeFile(privateFile.getAbsolutePath()) != null) {

                        circleImageView.setImageBitmap(BitmapFactory.decodeFile(privateFile.getAbsolutePath()));
                    }
                }
            });

        } else {
            System.out.println("setImage AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 4");


//            nathnm methana karala thiyenne userge email eken hari fb eken hari image eka load karala aran eka memory eke save karala thiyana eka
//                    eth harinm karanna ona fb storage eke check karala eketh nathnm thama userge mail hari fb eken hari image eka ganna ona

//            try {
//
//                privateFile.createNewFile();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            final String imageUrl;
            if (from.equals("worker")){
                imageUrl = WorkerMain.mUser.getImageUrl();
            }else{
                imageUrl = ClientMain.mUser.getImageUrl();
            }

            if (imageUrl == null || imageUrl.equals("")) {
                System.out.println("No user image in Under");

                final Uri photoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

                System.out.println(photoUrl + "                  >>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

                if (photoUrl != null) {

                    RunOnUIThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Picasso.with(activity)
                                    .load(photoUrl.toString())
                                    .into(circleImageView);

                        }
                    });


                }
            } else {

                System.out.println("Under Image uri");

                RunOnUIThread.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (circleImageView != null) {
                            Picasso.with(activity)
                                    .load(imageUrl)
                                    .into(circleImageView);
                        }
                        System.out.println("Image Seted....!");
                    }
                });


                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {

//                            Thread.sleep(10000);


                            MainActivity.downloadImage(activity,FirebaseAuth.getInstance().getUid(),"jpg",Environment.DIRECTORY_DOCUMENTS+"/User Images",imageUrl);

//                            Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.parse(imageUrl));
//                            FileOutputStream fileOutputStream = new FileOutputStream(privateFile);
//                            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//                            fileOutputStream.close();


//                        BitmapDrawable bitmapDrawable = (BitmapDrawable) circleImageView.getDrawable();
//                        Bitmap bitmap = bitmapDrawable.getBitmap();
//                        OutputStream outputStream = new FileOutputStream(privateFile);
//
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                        outputStream.flush();
//                        outputStream.close();

                            System.out.println("FIle Downloaded...............!!!");
//                            Toasty.success(activity, "File Downloaded").show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }).start();
            }

        }


    }


    private boolean checkLocationPermissionsAreGranted() {
        deniedList = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedList.add(permission);
            }
        }
        if (deniedList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == MainActivity.STORAGE_PERMITION_CODE) {

            allPermissionGranted = true;

            for (int i = 0; i < grantResults.length; i++) {

                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allPermissionGranted = false;
                }
            }

            if (allPermissionGranted) {
                setImage();
            } else {

                if (from.equals("client")) {
                    Snackbar.make(activity.findViewById(R.id.client_coor_lay), "App needs Storage permissions to continue", Snackbar.LENGTH_LONG).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(activity, deniedPermissionsAccess, MainActivity.LOCATION_PERMITION_CODE);
                                }
                            })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                } else if (from.equals("worker")) {
                    Snackbar.make(activity.findViewById(R.id.worker_coor_lay), "App needs Storage permissions to continue", Snackbar.LENGTH_LONG).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(activity, deniedPermissionsAccess, MainActivity.LOCATION_PERMITION_CODE);
                                }
                            })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();


                } else if (from.equals("main")) {
                    Snackbar.make(activity.findViewById(R.id.client_coor_lay), "App needs Storage permissions to continue", Snackbar.LENGTH_LONG).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(activity, deniedPermissionsAccess, MainActivity.LOCATION_PERMITION_CODE);
                                }
                            })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                }


            }

        }
    }
}
