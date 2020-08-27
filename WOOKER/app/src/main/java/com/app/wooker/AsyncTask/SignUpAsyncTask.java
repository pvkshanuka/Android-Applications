package com.app.wooker.AsyncTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.app.wooker.ClientMain;
import com.app.wooker.DBClasses.User;
import com.app.wooker.DBClasses.WorkerTypes;
import com.app.wooker.Dialog_Lodaing;
import com.app.wooker.MainActivity;
import com.app.wooker.WorkerMain;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import es.dmoral.toasty.Toasty;

public class SignUpAsyncTask extends AsyncTask {

    Dialog_Lodaing dialog_lodaing;

    Activity activity;
    User user;
    String from;
    String wtype = "";


    public SignUpAsyncTask(Activity activity, User user, String from) {
        this.activity = activity;
        this.user = user;
        this.from = from;
    }

    public SignUpAsyncTask(FragmentActivity activity, User user, String from, String wtype) {
        this.activity = activity;
        this.user = user;
        this.from = from;
        this.wtype = wtype;
        System.out.println(wtype + "   <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    @Override
    protected void onPreExecute() {
        dialog_lodaing = new Dialog_Lodaing("Processing..!");
        if (from.equals("client")) {
            ClientMain.fragmentController.setupDialog(dialog_lodaing);
        } else if (from.equals("worker")) {
            WorkerMain.fragmentController.setupDialog(dialog_lodaing);
        } else if (from.equals("client_main")) {
            MainActivity.fragmentController.setupDialog(dialog_lodaing);
        } else if (from.equals("worker_main")) {
            MainActivity.fragmentController.setupDialog(dialog_lodaing);
        } else {
            System.out.println("Error : Invalid Type In SignUpAsyncTask               >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toasty.error(activity, e.getMessage(), Toasty.LENGTH_LONG).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(activity, "User Registered Successfully.!");

                        if (from.equals("client")) {
                            ClientMain.mUser = user;
                            ClientMain.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            Intent intent = new Intent(activity, ClientMain.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.finish();
                        } else if (from.equals("worker")) {
                            WorkerMain.mUser = user;
                            WorkerMain.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            Intent intent = new Intent(activity, WorkerMain.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.finish();
                        } else if (from.equals("client_main")) {
                            ClientMain.mUser = user;
                            Intent intent = new Intent(activity, ClientMain.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.finish();
                        } else if (from.equals("worker_main")) {
                            WorkerMain.mUser = user;
                            Intent intent = new Intent(activity, WorkerMain.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    }
                });

        if (wtype != "") {

            FirebaseFirestore.getInstance().collection("work_cats")
                    .whereEqualTo("type", wtype).
                    get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!(queryDocumentSnapshots.isEmpty())) {
                        final String typeid = queryDocumentSnapshots.getDocuments().get(0).getId();

                        FirebaseFirestore.getInstance().collection("worker_types")
                                .whereEqualTo("typeid", typeid)
                                .whereEqualTo("workerid", FirebaseAuth.getInstance().getUid())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (queryDocumentSnapshots.isEmpty()) {
                                            FirebaseFirestore.getInstance().collection("worker_types").document().set(new WorkerTypes(typeid, FirebaseAuth.getInstance().getUid()))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
//                                                        Toasty.success(activity, "Category added.!").show();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            Toasty.error(activity, e.getMessage(), Toasty.LENGTH_LONG).show();
                                                        }
                                                    });
                                        } else {

                                            Toasty.error(activity, "Category already added", Toasty.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toasty.error(activity, e.getMessage(), Toasty.LENGTH_LONG).show();
                                    }
                                });


                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(activity, e.getMessage()).show();

                        }
                    });
        }


        return null;
    }
}
