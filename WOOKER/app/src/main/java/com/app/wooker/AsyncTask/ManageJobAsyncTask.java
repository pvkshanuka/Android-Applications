package com.app.wooker.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.app.wooker.ClientMain;
import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.Notification;
import com.app.wooker.DBClasses.User;
import com.app.wooker.DialogMap;
import com.app.wooker.Dialog_Lodaing;
import com.app.wooker.Frag_Client_Home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import es.dmoral.toasty.Toasty;

public class ManageJobAsyncTask extends AsyncTask {


    Activity activity;
    String from;
    Job job;
    Dialog_Lodaing dialog_lodaing;
    DocumentReference document;


    public ManageJobAsyncTask(Activity activity, String from, Job job, Dialog_Lodaing dialog_lodaing) {
        this.activity = activity;
        this.from = from;
        this.job = job;
        this.dialog_lodaing = dialog_lodaing;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        document = FirebaseFirestore.getInstance().collection("jobs").document();
        job.setJid(document.getId());
        document.set(job)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Notification notification = new Notification(job.getWid(),"New Job Request", "New " + job.getJtype() + " Job Request", "1", document.getId(), "1",new Date());
                        document = FirebaseFirestore.getInstance().collection("notifications").document();
                        notification.setNotid(document.getId());
                                document.set(notification)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        DialogMap.location=null;
                                        ClientMain.fragmentController.setupClientFragment(new Frag_Client_Home(),false);
                                        Toasty.success(activity, "Job request added successfully.!").show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                document.delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toasty.error(activity, "Unable to send Job Requst.!").show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toasty.error(activity, e.getMessage()).show();
                                    }
                                });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(activity, e.getMessage()).show();
                    }
                });

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        dialog_lodaing.dismiss();
    }
}
