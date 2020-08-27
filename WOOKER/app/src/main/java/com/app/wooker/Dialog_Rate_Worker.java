package com.app.wooker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.Notification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import CustomClasses.RunOnUIThread;
import es.dmoral.toasty.Toasty;

public class Dialog_Rate_Worker extends AppCompatDialogFragment {

    RatingBar ratingBar;
    Job job;
    Activity activity;
    JobsViewAdapter jobsViewAdapter;
    AllJobsViewAdapter allJobsViewAdapter;
    String from;
    EditText et_job_p;
    Notification notification;
    DocumentReference document;
    int position;

    public Dialog_Rate_Worker() {
    }
//
//    @SuppressLint("ValidFragment")
//    public Dialog_Rate_Worker(Job job, Activity activity) {
//        this.job = job;
//        this.activity = activity;
//    }

    @SuppressLint("ValidFragment")
    public Dialog_Rate_Worker(Job job, Activity activity, JobsViewAdapter jobsViewAdapter, String from, int position) {
        this.job = job;
        this.activity = activity;
        this.jobsViewAdapter = jobsViewAdapter;
        this.from = from;
        this.position = position;
    }


    @SuppressLint("ValidFragment")
    public Dialog_Rate_Worker(Job job, Activity activity, AllJobsViewAdapter allJobsViewAdapter, String from, int position) {
        this.job = job;
        this.activity = activity;
        this.allJobsViewAdapter = allJobsViewAdapter;
        this.from = from;
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_rate_worker, null);

        init(view);

        builder.setView(view)
                .setTitle("Please rate worker to continue.")
                .setPositiveButton("Finish Job", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        System.out.println(et_job_p.getText().toString().equals(""));
                        if (ratingBar.getRating() > 0 && !(et_job_p.getText().toString().equals(""))) {

                            boolean bol = false;
                            try {

                                if (Integer.parseInt(et_job_p.getText().toString()) > 49) {
                                    bol = true;
                                } else {
                                    bol = false;
                                }
                            } catch (Exception e) {
                                bol = false;
                            }
                            if (bol) {

//                            Toasty.info(getActivity(), "Rating is - " + ratingBar.getRating()).show();
                                job.setRating(ratingBar.getRating());
                                job.setStatus("5");
                                job.setPayment(Double.parseDouble(et_job_p.getText().toString()));
                                job.setEnd_time(new Date());
                                FirebaseFirestore.getInstance().collection("jobs").document(job.getJid()).set(job)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

//                                            dialog.cancel();
//                                            RunOnUIThread.runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                            if (from.equals("jva")) {
//                                                jobsViewAdapter.jobs.remove(position);
//                                                jobsViewAdapter.notifyItemRemoved(position);
//                                                jobsViewAdapter.notifyItemRangeChanged(position, jobsViewAdapter.jobs.size());
//                                            } else {
//                                                allJobsViewAdapter.jobs.remove(position);
//                                                allJobsViewAdapter.notifyItemRemoved(position);
//                                                allJobsViewAdapter.notifyItemRangeChanged(position, allJobsViewAdapter.jobs.size());
//                                            }
                                                Toasty.success(activity, "Job finished successfully.!").show();

                                                notification = new Notification(job.getWid(), "Job Finished", job.getJtype() + " Job Finished", "7", job.getJid(), "1", new Date());
                                                sendNotification(notification);

//                                                }
//                                            });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toasty.error(activity, e.getMessage()).show();
                                            }
                                        });
                            } else {
                                Toasty.error(activity, "Invalid payment amount.!").show();
                            }

                        } else {
                            Toasty.error(activity, "Some details are empty.!").show();
                        }
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    private void sendNotification(Notification notification) {

        document = FirebaseFirestore.getInstance().collection("notifications").document();
        notification.setNotid(this.document.getId());
        document.set(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        ClientMain.fragmentController.setupClientFragment(new Frag_Client_Home(),false);
//                        Toasty.success(activity, "Job request added successfully.!").show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(activity, "Unable to send notification : " + e.getMessage()).show();
            }
        });

    }

    private void init(View view) {
        ratingBar = view.findViewById(R.id.rate_c_w);
        et_job_p = view.findViewById(R.id.et_job_p);
    }
}
