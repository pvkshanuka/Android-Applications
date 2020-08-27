package com.app.wooker.AsyncTask;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.wooker.ClientMain;
import com.app.wooker.Frag_Worker_Profile_Settings;
import com.app.wooker.MainActivity;
import com.app.wooker.R;
import com.app.wooker.WorkerMain;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class UploadAndSaveUserImgAsyncTask extends AsyncTask implements ActivityCompat.OnRequestPermissionsResultCallback {

    Activity activity;
    String from;
    CircleImageView circleImageView;
    ProgressBar progressBar;
    Uri mImageUri;


    String permissions[] = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CAMERA};
    String[] deniedPermissionsAccess;
    List<String> deniedList;
    boolean allPermissionGranted = true;


    StorageReference mStorageRef;


    public UploadAndSaveUserImgAsyncTask(Activity activity, String from, CircleImageView circleImageView, ProgressBar progressBar, Uri mImageUri) {
        this.activity = activity;
        this.from = from;
        this.circleImageView = circleImageView;
        this.progressBar = progressBar;
        this.mImageUri = mImageUri;
        mStorageRef = FirebaseStorage.getInstance().getReference("userimages");
    }

    @Override
    protected void onPostExecute(Object o) {
        Frag_Worker_Profile_Settings.mUploadTask = null;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        if (checkLocationPermissionsAreGranted()) {

            uploadAndSaveImage();

        } else {
            deniedPermissionsAccess = new String[deniedList.size()];
            deniedPermissionsAccess = deniedList.toArray(deniedPermissionsAccess);

            Toasty.warning(activity, "Need permissions to continue").show();
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
                ActivityCompat.requestPermissions(activity, deniedPermissionsAccess, MainActivity.STORAGE_AND_CAMERS_PERMITION_CODE);
            }
        }


        return null;
    }

    private void uploadAndSaveImage() {

        final StorageReference fileReference;

        if (from.equals("worker")) {
            fileReference = mStorageRef.child(WorkerMain.firebaseUser.getUid() + ".jpg");
        } else {
            fileReference = mStorageRef.child(ClientMain.firebaseUser.getUid() + ".jpg");
        }

        Frag_Worker_Profile_Settings.mUploadTask = fileReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        }, 1000);
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

//                        taskSnapshot.getStorage().getDownloadUrl()
                                if (from.equals("worker")) {
                                    System.out.println(uri);
                                    WorkerMain.mUser.setImageUrl(uri.toString());
                                    WorkerMain.mUser.setImageName(WorkerMain.firebaseUser.getUid() + "." + "jpg");
                                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).set(WorkerMain.mUser);
                                } else {
                                    ClientMain.mUser.setImageUrl(uri.toString());
                                    ClientMain.mUser.setImageName(ClientMain.firebaseUser.getUid() + "." + getFileExtrntion(mImageUri));
                                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).set(ClientMain.mUser);
                                }
                            }

                        });

                        NavigationView navigationView = activity.findViewById(R.id.nav_view);

                        CircleImageView circleImageView2 = null;

                        if (from.equals("worker")) {
                            circleImageView2 = navigationView.getHeaderView(0).findViewById(R.id.circleImageViewheaderw);
                        } else {
                            circleImageView2 = navigationView.getHeaderView(0).findViewById(R.id.circleImageViewheaderc);
                        }
                        circleImageView2.setImageDrawable(circleImageView.getDrawable());
                        Drawable drawable = (BitmapDrawable) circleImageView.getDrawable();

                        File privateFile = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/User Images", FirebaseAuth.getInstance().getUid() + ".jpg");

                        System.out.println("File Created");

                        try {
                            if (mImageUri != null) {
                                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), mImageUri);
                                FileOutputStream fileOutputStream = new FileOutputStream(privateFile);
                                mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                fileOutputStream.close();
                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


//                        try {
//
//                            if (!privateFile.exists()) {
//                                privateFile.createNewFile();
//                            }
//
//                            OutputStream outputStream = new FileOutputStream(privateFile);
//
//                            ((BitmapDrawable) drawable).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                            outputStream.flush();
//                            outputStream.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

                        Toasty.success(activity, "Image uploaded successfully.!").show();
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(activity, e.getMessage(), Toasty.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        if (progressBar.getVisibility() == View.GONE) {
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
                        System.out.println("AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA....");
                    }
                });

    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private String getFileExtrntion(Uri uri) {
        ContentResolver contentResolver = activity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MainActivity.STORAGE_AND_CAMERS_PERMITION_CODE) {

            allPermissionGranted = true;

            for (int i = 0; i < grantResults.length; i++) {

                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allPermissionGranted = false;
                }
            }

            if (allPermissionGranted) {
                uploadAndSaveImage();
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



