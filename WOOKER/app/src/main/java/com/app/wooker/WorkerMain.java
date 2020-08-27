package com.app.wooker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wooker.AsyncTask.SetUserImageAsyncTask;
import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.Notification;
import com.app.wooker.DBClasses.User;
import com.app.wooker.ViewModels.JobsViewModel;
import com.app.wooker.ViewModels.NotificationsViewModel;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CustomClasses.CustomFragmentController;
import CustomClasses.RunOnUIThread;
import CustomClasses.Validations;
import es.dmoral.toasty.Toasty;

public class WorkerMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static User mUser = null;
    public static FirebaseUser firebaseUser;


    private long backPressedTime;
    Toast toasty_exit;
    NavigationView navigationView;

    private int menuItemId = 0;

    public static CustomFragmentController fragmentController;

    Fragment fragment;

    private static LocationManager locationManager;
    private static LocationListener locationListener;

    Map<String, Double> live_location;
    Geocoder geocoder;
    List<Address> addresses;

    List<Notification> nortiList;

    NotificationsViewModel notificationsViewModel;
    NotificationChannel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("WOOKER - Worker");

        setSupportActionBar(toolbar);

        fragmentController = new CustomFragmentController(getSupportFragmentManager());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            setUserData(navigationView);
        } else {
            logout();
        }

//        CircleImageView circleImageView = navigationView.getHeaderView(0).findViewById(R.id.circleImageView);
//
//        circleImageView.setBorderWidth(20);

        new SetUserImageAsyncTask(this, "worker").execute();

        if (mUser.isOnline()) {
            mUser.setOnline(false);
            FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid()).set(WorkerMain.mUser);
        }

        Frag_Worker_Home frag_worker_home = new Frag_Worker_Home();
        fragmentController.setupWorkerFragment(frag_worker_home, false);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa onLocationChanged");

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            live_location = new HashMap<>();
                            live_location.put("latitude", location.getLatitude());
                            live_location.put("longitude", location.getLongitude());

                            geocoder = new Geocoder(WorkerMain.this);
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            mUser.setLive_location(live_location);
                            if (addresses.size() != 0) {
                                System.out.println(addresses.get(0).toString());
                                if (addresses.get(0).getLocality() != null) {
                                    mUser.setLive_loc_city(addresses.get(0).getLocality());
                                } else if (addresses.get(0).getSubAdminArea() != null) {
                                    mUser.setLive_loc_city(addresses.get(0).getSubAdminArea());
                                } else if (addresses.get(0).getFeatureName() != null) {
                                    mUser.setLive_loc_city(addresses.get(0).getFeatureName());
                                }
                            }
                            FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid()).set(WorkerMain.mUser);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


//                Toasty.info(WorkerMain.this, "Location: Lat-" + location.getLatitude() + " Lng-" + location.getLongitude()).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa onProviderEnabled");

            }

            @Override
            public void onProviderDisabled(String provider) {
                System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa onProviderDisabled");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("menuItemId", menuItemId + "");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        menuItemId = Integer.parseInt(savedInstanceState.getString("menuItemId"));

        if (menuItemId == R.id.it_w_v_a_j) {
            fragment = new Frag_Worker_View_All_Jobs();
            fragmentController.setupWorkerFragment(fragment, false);
        } else if (menuItemId == R.id.it_w_p) {
            fragment = new Frag_Worker_Profile();
            fragmentController.setupWorkerFragment(fragment, false);
        } else if (menuItemId == R.id.it_w_p_s) {
            fragment = new Frag_Worker_Profile_Settings();
            fragmentController.setupWorkerFragment(fragment, false);
        } else if (menuItemId == R.id.it_w_nor) {
            fragment = new Frag_Notifications("User");
            fragmentController.setupWorkerFragment(fragment, false);
        } else {
            fragment = new Frag_Worker_Home();
            fragmentController.setupWorkerFragment(fragment, false);
        }
    }

    public static void setUserData(NavigationView navigationView) {
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_cname)).setText(mUser.getFname() + " " + mUser.getLname());
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_cemail)).setText(firebaseUser.getEmail());
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_ccon)).setText(mUser.getCno());

    }

    public void ala() {
    }

    @Override
    public void onBackPressed() {
        System.out.println("Wooker Main onBackPressed <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (MainActivity.where.equals("Settings Worker")) {
//            super.onBackPressed();
//        } else {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                toasty_exit.cancel();
                this.finish();
                System.exit(0);
                return;
            } else {
                toasty_exit = Toasty.info(this, "Press back again to exit.!", 2000);
                toasty_exit.show();
            }

            backPressedTime = System.currentTimeMillis();

        }
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.worker_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            navigationView.getCheckedItem().setChecked(false);
            fragmentController.setupWorkerFragment(new Frag_Settings("worker"), true);
        }

        if (id == R.id.action_Reports) {
            navigationView.getCheckedItem().setChecked(false);
            fragmentController.setupWorkerFragment(new Frag_Reports(), true);
        }
        if (id == R.id.action_Bluetooth) {
            navigationView.getCheckedItem().setChecked(false);
            fragmentController.setupWorkerFragment(new Frag_Bluetooth(), true);
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        menuItemId = item.getItemId();
        System.out.println("onNavigationItemSelected  AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        if (menuItemId == R.id.it_w_h) {
            // Handle the camera action
            fragment = new Frag_Worker_Home();
            fragmentController.setupWorkerFragment(fragment, false);
        } else if (menuItemId == R.id.it_w_v_a_j) {
            fragment = new Frag_Worker_View_All_Jobs();
            fragmentController.setupWorkerFragment(fragment, false);
        } else if (menuItemId == R.id.it_w_p) {
            fragment = new Frag_Worker_Profile();
            fragmentController.setupWorkerFragment(fragment, false);
        } else if (menuItemId == R.id.it_w_p_s) {
            fragment = new Frag_Worker_Profile_Settings();
            fragmentController.setupWorkerFragment(fragment, false);
        } else if (menuItemId == R.id.it_w_nor) {
            fragment = new Frag_Notifications("User");
            fragmentController.setupWorkerFragment(fragment, false);
        } else if (menuItemId == R.id.it_w_logout) {

            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        final Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("Processing.!", "Signing Out..");
        if (mUser.isOnline()) {
            WorkerMain.mUser.setOnline(false);
            FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid()).set(WorkerMain.mUser);
            setLocationListener(false, this);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                    if
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("facebook.com")) {

                        AccessToken.setCurrentAccessToken(null);
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();

                    } else if (FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("google.com")) {
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();
                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                        googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseAuth.getInstance().signOut();
                            }
                        });
                    }

                }

//                Intent alarmReceiverIntent = new Intent(getApplicationContext(), ReminderReceiver.class);
//
//                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//
//                PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, alarmReceiverIntent, PendingIntent.FLAG_NO_CREATE);
//                if (alarmIntent != null) {
//                    alarmManager.cancel(alarmIntent);
//                }

                FirebaseAuth.getInstance().signOut();


//            AuthUI.getInstance()
//                    .signOut(this)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(WorkerMain.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                Toasty.success(WorkerMain.this, "Sign Out Successfuly.!");
            }

        });

        mUser = null;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mUser.isOnline()) {
//            WorkerMain.mUser.setOnline(false);
//            FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid()).set(WorkerMain.mUser);
//            setLocationListener(false,this);
//            setLocationListener(mUser.isOnline(), WorkerMain.this);
        }

        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        notificationsViewModel.startListener();
        notificationsViewModel.getNotifications().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(@Nullable List<Notification> notifications) {

                if (channel == null) {
                    createNotificationChannel(WorkerMain.this);
                }
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(WorkerMain.this, "44");
                final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, new Intent(getApplicationContext(), Frag_Notifications.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

                System.out.println("AWAAAAAAAAAAAAAAAAAAAA Notification >>>>>>>>> : " + notifications.size());

                nortiList = new ArrayList<>();

                int i = 0;
                for (Notification noti : notifications) {
                    i++;
                    if (noti.getStatus().equals("1") && noti.getNotid() != null) {
                        System.out.println("NEW Notification _____________________ >>>>>>>>>>>>>>>>>>>> " + noti.getNotid());
                        builder.setSmallIcon(R.drawable.wooker)
                                .setContentTitle(noti.getTitle())
                                .setContentText(noti.getMessage())
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(WorkerMain.this);
                        notificationManager.notify(i, builder.build());
                        nortiList.add(noti);
                    } else {
                        System.out.println("NOT A Notification _____________________ >>>>>>>>>>>>>>>>>>>> " + noti.getNotid());

                    }
                }

                System.out.println("New Notification Size >>>>>>>>>>>>>>>>>>>>> : " + nortiList.size());
                for (Notification no : nortiList) {
                    no.setStatus("2");
                    if (no.getNotid() != null) {
                        FirebaseFirestore.getInstance().collection("notifications").document(no.getNotid()).set(no)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull final Exception e) {
                                        RunOnUIThread.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toasty.error(getApplicationContext(), e.getMessage()).show();
                                            }
                                        });
                                    }
                                });
                    }
                }


            }
        });

    }

    private void createNotificationChannel(WorkerMain workerMain) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notifications";
            String description = "Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel("44", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = workerMain.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void setLocationListener(boolean isOnline, Activity activity) {
        System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa setLocationListener " + isOnline + " user- " + mUser.isOnline());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, MainActivity.LOCATION_PERMITION_AND_INTERNET_ONLY_CODE);
                return;
            }
        }

        if (isOnline) {
            System.out.println("User Online");
            locationManager.requestLocationUpdates("gps", 3000, 5, locationListener);
        } else {
            System.out.println("User Offline");
            locationManager.removeUpdates(locationListener);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MainActivity.LOCATION_PERMITION_AND_INTERNET_ONLY_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocationListener(mUser.isOnline(), WorkerMain.this);
                    return;
                }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }

//        Frag_Worker_Home frag_worker_home = new Frag_Worker_Home();
//        fragmentController.setupWorkerFragment(frag_worker_home, false);

        DialogMap.location = null;

    }
}
