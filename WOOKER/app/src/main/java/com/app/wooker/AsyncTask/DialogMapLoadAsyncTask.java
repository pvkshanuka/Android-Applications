package com.app.wooker.AsyncTask;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.app.wooker.ClientMain;
import com.app.wooker.DialogMap;
import com.app.wooker.Dialog_Lodaing;
import com.app.wooker.MainActivity;
import com.app.wooker.R;
import com.app.wooker.WorkerMain;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DialogMapLoadAsyncTask extends AsyncTask implements ActivityCompat.OnRequestPermissionsResultCallback {


    String permissions[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
    String[] deniedPermissionsAmongLocationAccess;
    List<String> deniedList;
    boolean allPermissionGranted = true;


    Dialog_Lodaing dialog_lodaing;
    Activity activity;
    String from;
    Boolean isOnlyView = false;

    public DialogMapLoadAsyncTask(Activity activity, String from) {
        this.activity = activity;
        this.from = from;
    }
    public DialogMapLoadAsyncTask(Activity activity, String from,Boolean isOnlyView) {
        this.activity = activity;
        this.from = from;
        this.isOnlyView = isOnlyView;
    }

    @Override
    protected void onPreExecute() {
        dialog_lodaing = new Dialog_Lodaing("Processing..!", "Loading Map.!");
        if (from.equals("client")) {
            ClientMain.fragmentController.setupDialog(dialog_lodaing);
        } else if (from.equals("worker")) {
            WorkerMain.fragmentController.setupDialog(dialog_lodaing);
        } else if (from.equals("main")) {
            MainActivity.fragmentController.setupDialog(dialog_lodaing);
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        dialog_lodaing.dismiss();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (checkLocationPermissionsAreGranted()) {
            openDialogMap();
        } else {
            deniedPermissionsAmongLocationAccess = new String[deniedList.size()];
            deniedPermissionsAmongLocationAccess = deniedList.toArray(deniedPermissionsAmongLocationAccess);

//            Toasty.warning(activity, "Need permissions to continue").show();
//                    .makeText(activity, "Need permissions to continue", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermissionsAmongLocationAccess[0])) {

                if (from.equals("client")) {

                    Snackbar.make(activity.findViewById(R.id.client_coor_lay), "App needs permissions to access your location & connect internet", Snackbar.LENGTH_LONG)
                            .setAction("ENABLE",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(activity, deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
                                        }
                                    })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                } else if (from.equals("worker")) {

                    Snackbar.make(activity.findViewById(R.id.worker_coor_lay), "App needs permissions to access your location & connect internet", Snackbar.LENGTH_LONG)
                            .setAction("ENABLE",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(activity, deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
                                        }
                                    })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                } else if (from.equals("main")) {

                    Snackbar.make(activity.findViewById(R.id.main_coor_lay), "App needs permissions to access your location & connect internet", Snackbar.LENGTH_LONG)
                            .setAction("ENABLE",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(activity, deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
                                        }
                                    })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                }


            } else {
                ActivityCompat.requestPermissions(activity, deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
            }
        }

        return null;
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

    private void openDialogMap() {

        if (isServicesOK()) {

            if (isNetworkConnected()) {
//                DialogMap dialogMap = new DialogMap();
//                dialogMap.show(getActivity().getSupportFragmentManager(), "DialogMap");

                if (from.equals("client")) {
                    ClientMain.fragmentController.setupDialog(new DialogMap(isOnlyView));
                } else if (from.equals("worker")) {
                    WorkerMain.fragmentController.setupDialog(new DialogMap(isOnlyView));
                } else if (from.equals("main")) {
                    MainActivity.fragmentController.setupDialog(new DialogMap(isOnlyView));
                }

//                ClientMain.fragmentController.setupDialog(new DialogMap());
            } else {

                if (from.equals("client")) {

                    Snackbar.make(activity.findViewById(R.id.client_coor_lay), "Please connect to internet", Snackbar.LENGTH_LONG).setAction("Connect",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                                    activity.startActivity(intent);
                                }
                            })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                } else if (from.equals("worker")) {

                    Snackbar.make(activity.findViewById(R.id.worker_coor_lay), "Please connect to internet", Snackbar.LENGTH_LONG).setAction("Connect",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                                    activity.startActivity(intent);
                                }
                            })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                } else if (from.equals("main")) {

                    Snackbar.make(activity.findViewById(R.id.main_coor_lay), "Please connect to internet", Snackbar.LENGTH_LONG).setAction("Connect",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                                    activity.startActivity(intent);
                                }
                            })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                }
            }
        }
    }

    private boolean isServicesOK() {

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

    private boolean isNetworkConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else
            return false;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MainActivity.LOCATION_PERMITION_CODE) {

            allPermissionGranted = true;

            for (int i = 0; i < grantResults.length; i++) {

                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allPermissionGranted = false;
                }
            }

            if (allPermissionGranted) {
                openDialogMap();
            } else {

                if (from.equals("client")) {
                    Snackbar.make(activity.findViewById(R.id.client_coor_lay), "App needs permissions to continue", Snackbar.LENGTH_LONG).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(activity, deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
                                }
                            })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                } else if (from.equals("worker")) {
                    Snackbar.make(activity.findViewById(R.id.worker_coor_lay), "App needs permissions to continue", Snackbar.LENGTH_LONG).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(activity, deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
                                }
                            })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();


                } else if (from.equals("main")) {
                    Snackbar.make(activity.findViewById(R.id.client_coor_lay), "App needs permissions to continue", Snackbar.LENGTH_LONG).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(activity, deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
                                }
                            })
                            .setActionTextColor((ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary))).show();

                }


            }

        }
    }
}
