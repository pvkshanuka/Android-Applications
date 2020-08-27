package com.app.wooker;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.app.wooker.DBClasses.Notification;
import com.app.wooker.DBClasses.User;
import com.app.wooker.ViewModels.NotificationsViewModel;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import CustomClasses.CustomFragmentController;
import CustomClasses.RunOnUIThread;
import es.dmoral.toasty.Toasty;

public class ClientMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static User mUser = null;
    public static FirebaseUser firebaseUser;

    private long backPressedTime;
    Toast toasty_exit;

    NavigationView navigationView;

    DrawerLayout drawer;

    Fragment frag_current;

    List<Notification> nortiList;

    NotificationsViewModel notificationsViewModel;
    NotificationChannel channel;

    public static CustomFragmentController fragmentController;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("WOOKER - Client");

        setSupportActionBar(toolbar);

        fragmentController = new CustomFragmentController(getSupportFragmentManager());

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.getMenu().getItem(0).setChecked(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        setUserData(navigationView);

//        CircleImageView circleImageView = navigationView.getHeaderView(0).findViewById(R.id.circleImageView);
//        circleImageView.setBorderWidth(20);

        new SetUserImageAsyncTask(this, "client").execute();

        Frag_Client_Home frag_client_home = new Frag_Client_Home();
        fragmentController.setupClientFragment(frag_client_home, false);
//        List<? extends UserInfo> providerData = FirebaseAuth.getInstance().getCurrentUser().getProviderData();
//
//        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId() + "  hi hi hi>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//
//
//        for (UserInfo userInfo : providerData) {
//
//            System.out.println(userInfo.getProviderId() + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        }
//
//        Toasty.warning(getApplicationContext(), FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    public static void setUserData(NavigationView navigationView) {
//        System.out.println("Photo Url - "+firebaseUser.getPhotoUrl().toString());
        if (ClientMain.firebaseUser != null) {
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_cname)).setText(mUser.getFname() + " " + mUser.getLname());
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_cemail)).setText(ClientMain.firebaseUser.getEmail());
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_ccon)).setText(mUser.getCno());
        }
    }

    @Override
    public void onBackPressed() {

//        if (MainActivity.where.equals("Settings Client")) {
//            super.onBackPressed();
//        } else {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            frag_current = getSupportFragmentManager().findFragmentById(R.id.cons_lay_client);

//            Frag_Dialog_Map frag_dialog_map = new Frag_Dialog_Map();

            if (frag_current instanceof Frag_Dialog_Map) {
                super.onBackPressed();
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
        }
//            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_home, menu);
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
            fragmentController.setupClientFragment(new Frag_Settings("client"), true);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.it_c_h) {
            Frag_Client_Home frag_client_home = new Frag_Client_Home();
            fragmentController.setupClientFragment(frag_client_home, false);
        } else if (id == R.id.it_c_v_a_j) {
            System.out.println("AWA 1");
            Frag_Client_View_All_Jobs frag_client_view_all_jobs = new Frag_Client_View_All_Jobs();
            fragmentController.setupClientFragment(frag_client_view_all_jobs, false);
        } else if (id == R.id.it_c_p_s) {
            Frag_Client_Profile_Settings fragClientProfileSettings = new Frag_Client_Profile_Settings();
            fragmentController.setupClientFragment(fragClientProfileSettings, false);
        } else if (id == R.id.it_c_nor) {
            Frag_Notifications frag_notifications = new Frag_Notifications("Client");
            fragmentController.setupClientFragment(frag_notifications, false);
        } else if (id == R.id.it_c_logout) {

            final Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("Processing.!", "Signing Out..");

            new Thread(new Runnable() {


                @Override
                public void run() {

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
                    } else {
                        FirebaseAuth.getInstance().signOut();
                    }

                    Intent intent = new Intent(ClientMain.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


//                    if
                        }

                    });

                }
            }).start();
            mUser = null;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == MainActivity.LOCATION_PERMITION_CODE) {
//
//            allPermissionGranted = true;
//
//            for (int i = 0; i < grantResults.length; i++) {
//
//                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                    allPermissionGranted = false;
//                }
//            }
//
//            if (allPermissionGranted) {
//                openDialogMap();
////                DialogMap dialogMap = new DialogMap();
////                dialogMap.show(getActivity().getSupportFragmentManager(), "DialogMap");
//            } else {
//                Snackbar.make(getActivity().findViewById(R.id.main_coor_lay), "App needs permissions to continue", Snackbar.LENGTH_LONG).setAction("ENABLE",
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ActivityCompat.requestPermissions(getActivity(), deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
//                            }
//                        })
//                        .setActionTextColor((ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary))).show();
//            }
//
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();

        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        notificationsViewModel.startListener();
        notificationsViewModel.getNotifications().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(@Nullable List<Notification> notifications) {

                if (channel == null) {
                    createNotificationChannel(ClientMain.this);
                }
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(ClientMain.this, "44");
                final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, new Intent(getApplicationContext(), Frag_Notifications.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

                System.out.println("AWAAAAAAAAAAAAAAAAAAAA Notification Client MAin >>>>>>>>> : " + notifications.size());

                nortiList = new ArrayList<>();

                int i = 0;
                for (Notification noti : notifications) {
                    i++;
                    if (noti.getStatus().equals("1") && noti.getNotid() != null) {

                        builder.setSmallIcon(R.drawable.wooker)
                                .setContentTitle(noti.getTitle())
                                .setContentText(noti.getMessage())
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ClientMain.this);
                        notificationManager.notify(i, builder.build());
                        nortiList.add(noti);
                    }
                }


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

    private void createNotificationChannel(ClientMain clientMain) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notifications";
            String description = "Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel("44", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = clientMain.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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

        DialogMap.location = null;

    }

}
