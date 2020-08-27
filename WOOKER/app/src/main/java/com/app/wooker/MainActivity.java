package com.app.wooker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wooker.DBClasses.User;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import CustomClasses.CustomFragmentController;
import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {

    public static final int LOCATION_PERMITION_CODE = 1;
    public static final int STORAGE_PERMITION_CODE = 2;
    public static final int STORAGE_AND_CAMERS_PERMITION_CODE = 4;
    public static final int LOCATION_PERMITION_AND_INTERNET_ONLY_CODE = 3;

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int CALL_PERMISSION_CODE = 5;

    public static CustomFragmentController fragmentController;

    public static String where = "Main Activity";

    public static ProgressBar progressBar;


    private long backPressedTime;
    Toast toasty_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.p_bar_main);
//
//        DoubleBounce doubleBounce = new DoubleBounce();
//
//        progressBar.setIndeterminateDrawable(doubleBounce);
//        progressBar.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);

        fragmentController = new CustomFragmentController(getSupportFragmentManager());

//        Frag_Login fragLogin = new Frag_Login();


        FirebaseFirestore.getInstance().collection("lang");

        progressBar = findViewById(R.id.p_bar_main);

        DoubleBounce loader = new DoubleBounce();

        progressBar.setIndeterminateDrawable(loader);


//        findViewById(R.id.main_cons_lay).setBackgroundColor(getColor(R.color.white));
//        progressBar.setVisibility(View.VISIBLE);


//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        for (Fragment fragment : fragmentManager.getFragments()) {
//            fragmentTransaction.remove(fragment);
//        }
//
//        fragmentTransaction.add(R.id.main_cons_lay, fragLogin, "fragLogin");
//        fragmentTransaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) {


                    Frag_User_Login fragLogin = new Frag_User_Login();

                    fragmentController.setupFragment(fragLogin, false);
                } else {

                    ((TextView) findViewById(R.id.textView38)).setText("Signing In..!");

                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {

                                        User user = documentSnapshot.toObject(User.class);

                                        if (user.getType().equals("1")) {
                                            WorkerMain.mUser = user;
                                            Intent intent = new Intent(MainActivity.this, WorkerMain.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            MainActivity.this.startActivity(intent);
                                            MainActivity.this.finish();
                                        } else if (user.getType().equals("2")) {
                                            ClientMain.mUser = user;
                                            Intent intent = new Intent(MainActivity.this, ClientMain.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            MainActivity.this.startActivity(intent);
                                            MainActivity.this.finish();
                                        } else {
                                            Toasty.error(MainActivity.this, "Signing In Failed.! Please Sign In Againn.").show();
                                            Frag_User_Login fragLogin = new Frag_User_Login();

                                            fragmentController.setupFragment(fragLogin, false);
                                        }

                                    } else {
                                        Toasty.error(MainActivity.this, "Signing In Failed.! Please Sign In Againn.").show();
                                        Frag_User_Login fragLogin = new Frag_User_Login();

                                        fragmentController.setupFragment(fragLogin, false);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toasty.error(MainActivity.this, "Signing In Failed.! Please Sign In Againn.").show();
                                    Frag_User_Login fragLogin = new Frag_User_Login();

                                    fragmentController.setupFragment(fragLogin, false);
                                }
                            });

                }
            }

//        }, 1000);
        }, 0);

    }

    public void onBackPressed() {

        if (where.equals("Login")) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                toasty_exit.cancel();
                super.onBackPressed();
                return;
            } else {
                toasty_exit = Toasty.info(this, "Press back again to exit.!", 2000);
                toasty_exit.show();
            }

            backPressedTime = System.currentTimeMillis();

        } else {
            super.onBackPressed();
        }
    }


    public static void dismissAllDialogs(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();

        if (fragments == null)
            return;

        for (Fragment fragment : fragments) {
            if (fragment instanceof DialogFragment) {
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismissAllowingStateLoss();
            }

            FragmentManager childFragmentManager = fragment.getChildFragmentManager();
            if (childFragmentManager != null)
                dismissAllDialogs(childFragmentManager);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void requestPermission(Activity activity) {

        String permissions[] = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        String[] deniedPermissionsAccess;
        List<String> deniedList;
        boolean allPermissionGranted = true;

    }

    public static BitmapDescriptor getBitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        try {
            Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
            background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
            vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
            Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            background.draw(canvas);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }


    public static boolean isConnectedUseThread(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com/");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("User-Agent", "test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1000); // mTimeout is in seconds
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    Log.i("warning", "Error checking internet connection", e);
                    return false;
                }
            }

        } catch (Exception e) {
            Log.i("warning", "Error checking internet connection2", e);
            e.printStackTrace();
        }
        return false;

    }

    public static void downloadImage(Context context,String fileName,String fileExtention,String destinationDirectory,String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+"."+fileExtention);

        downloadManager.enqueue(request);

    }

    public static boolean isServicesOK(Activity activity) {

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);

        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, available, MainActivity.ERROR_DIALOG_REQUEST);
            errorDialog.show();
        } else {

        }
        return false;

    }

}
