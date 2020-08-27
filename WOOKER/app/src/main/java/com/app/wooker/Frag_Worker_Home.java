package com.app.wooker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wooker.AsyncTask.DialogMapLoadAsyncTask;
import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.Notification;
import com.app.wooker.DBClasses.User;
import com.app.wooker.ViewModels.JobsViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import CustomClasses.DatePickerFragment;
import CustomClasses.RunOnUIThread;
import CustomClasses.Test;
import CustomClasses.Validations;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Frag_Worker_Home extends Fragment {

    ArrayList<Job> jobs;
    RecyclerView job_requests_view;
    ProgressBar progressBar;
    Job job;
    TextView tv_no_jobs;
    JobsViewModel jobsViewModel;
    JobRequestsViewAdapter jobRequestsViewAdapter;
    boolean b = false;
    Button btn_online;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.worker_home, container, false);

        init(view);


        return view;
    }

    private void init(View view) {
        job_requests_view = view.findViewById(R.id.rv_w_job_requests);
        btn_online = view.findViewById(R.id.btn_online);
        progressBar = view.findViewById(R.id.progressBarwh);
        tv_no_jobs = view.findViewById(R.id.tv_no_jobs_wh);

        System.out.println("Awagaman meya mokOOOOOOOOOOOOOOOOOOOOOOOOOOO  " + WorkerMain.mUser.isOnline());

        if (WorkerMain.mUser.isOnline()) {
            btn_online.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.editTextBG));
            btn_online.setText("Go Offline");
        } else {
            btn_online.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_green));
            btn_online.setText("Go Online");
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btn_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WorkerMain.mUser.isOnline()) {
                    //Online nm do offline
                    btn_online.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_green));
                    btn_online.setText("Go Online");


                    WorkerMain.mUser.setOnline(false);

                    WorkerMain.setLocationListener(false, getActivity());
                } else {
//                    Offline nm od online
                    btn_online.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.editTextBG));
                    btn_online.setText("Go Offline");
                    WorkerMain.mUser.setOnline(true);
                    WorkerMain.setLocationListener(true, getActivity());
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseFirestore.getInstance().collection("users").document(WorkerMain.firebaseUser.getUid()).set(WorkerMain.mUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
//                                        Toasty.success(getActivity(), "You are online now.!").show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toasty.error(getActivity(), e.getMessage()).show();
                                    }
                                });

                    }
                }).start();

            }
        });


//        loadJobs();

    }

    public void loadJobs() {
        jobs = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                FirebaseFirestore.getInstance().collection("jobs").whereEqualTo("wid", WorkerMain.mUser.getUid())
//                        .orderBy("job_date")
                System.out.println(WorkerMain.mUser.getUid() + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                FirebaseFirestore.getInstance().collection("jobs")
                        .orderBy("status")
                        .orderBy("job_date")
                        .whereEqualTo("wid", WorkerMain.mUser.getUid())
                        .whereLessThanOrEqualTo("status", "4")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    System.out.println("Empty");
                                    Toasty.info(getActivity(), "No job requests.!").show();
                                    tv_no_jobs.setVisibility(View.VISIBLE);
                                } else {

                                    System.out.println("Empty Na" + queryDocumentSnapshots.size());
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        job = documentSnapshot.toObject(Job.class);
                                        job.setJid(documentSnapshot.getId());
                                        jobs.add(job);
                                    }
                                    System.out.println("JOBS COUNT- " + jobs.size());
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                                    job_requests_view.setLayoutManager(linearLayoutManager);

                                    JobRequestsViewAdapter jobRequestsViewAdapter = new JobRequestsViewAdapter(jobs, getActivity());
                                    job_requests_view.setAdapter(jobRequestsViewAdapter);
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(getActivity(), e.getMessage()).show();
                                e.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                            }
                        });

            }
        }).start();

    }

    @Override
    public void onResume() {
        System.out.println("onResume");
        super.onResume();
        if (WorkerMain.mUser.isOnline()) {
            btn_online.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.editTextBG));
            btn_online.setText("Go Offline");
        } else {
            btn_online.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_green));
            btn_online.setText("Go Online");
        }


        jobsViewModel = ViewModelProviders.of(this).get(JobsViewModel.class);
        jobsViewModel.startListener();
        jobsViewModel.getJobs().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(@Nullable List<Job> jobs2) {
                System.out.println("notifyDataSetChanged calling");
                jobs = new ArrayList<>();
                for (Job job : jobs2) {
//                    orderBy("status")
//                            .whereEqualTo("cid", ClientMain.mUser.getUid())
//                            .whereLessThanOrEqualTo("status", "4")
//                            .orderBy("job_date")
                    if (Integer.parseInt(job.getStatus()) <= 4) {
                        jobs.add(job);
                    } else {
                        System.out.println("Other Job : " + job.getJid());
                    }
                }

                if (jobRequestsViewAdapter == null) {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    job_requests_view.setLayoutManager(linearLayoutManager);
                    jobRequestsViewAdapter = new JobRequestsViewAdapter(jobs, getActivity());
                    job_requests_view.setAdapter(jobRequestsViewAdapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    jobRequestsViewAdapter.setJobs(jobs);
                    jobRequestsViewAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
                if (b) {

                    Toasty.info(getActivity(), "Jobs Updated.!").show();
                } else {
                    b = true;
                }

            }
        });

        jobsViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
//                    jobs_view.smoothScrollToPosition(jobs.size() - 1);
                }
            }
        });


    }
}

class JobRequestsViewAdapter extends RecyclerView.Adapter {

    ArrayList<Job> jobs;
    User client;

    Activity activity;
    Map<String, Double> job_location;
    FirebaseFirestore db;
    Dialog_Lodaing dialogLodaing;
    Notification notification;
    DocumentReference document;

    public JobRequestsViewAdapter(ArrayList<Job> jobs, Activity activity) {
        this.jobs = jobs;
        this.activity = activity;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.job_request_view, viewGroup, false);
        JobRequestsViewHolder jobRequestsViewHolder = new JobRequestsViewHolder(view);
        return jobRequestsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

        final Job job = jobs.get(position);


        db.collection("users").document(job.getCid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            System.out.println("IF AWAAAAAAAAAA");
                            client = documentSnapshot.toObject(User.class);


                            final JobRequestsViewHolder jobRequestsViewHolder = (JobRequestsViewHolder) viewHolder;


                            if (job.getStatus().equals("1")) {
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.GONE);
                            } else if (job.getStatus().equals("2")) {
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.GONE);
                            } else if (job.getStatus().equals("3")) {
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.GONE);
                            } else if (job.getStatus().equals("4")) {
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.GONE);
                            }

                            jobRequestsViewHolder.type.setText(job.getJtype());
                            jobRequestsViewHolder.tv_job_id.setText(documentSnapshot.getId());

                            jobRequestsViewHolder.tv_req_date.setText(Validations.dateObjToString(job.getJob_date(), "yyyy-MM-dd"));
                            jobRequestsViewHolder.tv_added_date.setText(Validations.dateObjToString(job.getAdded_date(), "yyyy-MM-dd"));
                            if (client != null) {
                                System.out.println("CLIENT NULL NA");
                                jobRequestsViewHolder.tv_cname.setText(client.getFname() + " " + client.getLname());
                                jobRequestsViewHolder.tv_ccno.setText(client.getCno());

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        RunOnUIThread.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (jobRequestsViewHolder.circleImageView_c_img != null) {
                                                    Picasso.with(activity)
                                                            .load(client.getImageUrl())
                                                            .into(jobRequestsViewHolder.circleImageView_c_img);
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            }
                            jobRequestsViewHolder.viewmore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TransitionManager.beginDelayedTransition(jobRequestsViewHolder.moredetails);
                                    if (jobRequestsViewHolder.moredetails.getVisibility() == View.GONE) {
                                        jobRequestsViewHolder.moredetails.setVisibility(View.VISIBLE);
                                        jobRequestsViewHolder.viewmore.setText("Hide Client Details");


                                    } else {
                                        jobRequestsViewHolder.moredetails.setVisibility(View.GONE);
                                        jobRequestsViewHolder.viewmore.setText("View Client Details");

                                    }

                                }
                            });

                            jobRequestsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TransitionManager.beginDelayedTransition(jobRequestsViewHolder.moredetails);
                                    if (jobRequestsViewHolder.moredetails.getVisibility() == View.GONE) {
                                        jobRequestsViewHolder.moredetails.setVisibility(View.VISIBLE);
                                        jobRequestsViewHolder.viewmore.setText("Hide Client Details");
                                    } else {
                                        jobRequestsViewHolder.moredetails.setVisibility(View.GONE);
                                        jobRequestsViewHolder.viewmore.setText("View Client Details");
                                    }
                                    Snackbar.make(v.getRootView().findViewById(R.id.worker_coor_lay), ((TextView) v.findViewById(R.id.tv_w_job_type)).getText().toString(), Snackbar.LENGTH_LONG).show();
                                }
                            });

                            jobRequestsViewHolder.btn_callc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    makeCall(jobRequestsViewHolder.tv_ccno.getText().toString());
                                }
                            });

                            jobRequestsViewHolder.btn_view_location.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    job_location = job.getJob_location();
                                    DialogMap.location = new LatLng(job_location.get("latitude"), job_location.get("longitude"));
                                    new DialogMapLoadAsyncTask(activity, "worker", true).execute();
                                    System.out.println("onClick END");
                                }
                            });

                            jobRequestsViewHolder.btn_dec_job.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (job.getStatus().equals("1")) {


                                        new AlertDialog.Builder(activity)
                                                .setTitle("Confirm message.!")
                                                .setMessage("Are you suer you want to DECLINE this job.?")
                                                .setPositiveButton("Decline Job", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialogLodaing = new Dialog_Lodaing("Declineing Job.!");
                                                        WorkerMain.fragmentController.setupDialog(dialogLodaing);
                                                        job.setStatus("7");
                                                        db.collection("jobs").document(job.getJid()).set(job)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

//                                                                        jobRequestsViewHolder.mainlay.setVisibility(View.GONE);
//                                                                        jobs.remove(position);
//                                                                        notifyItemRemoved(position);
//                                                                        notifyItemRangeChanged(position,jobs.size());
                                                                        Toasty.success(activity, "Job declined successfully.!").show();

                                                                        notification = new Notification(job.getCid(), "Job Request Declined", job.getJtype() + " Job Request Declined", "5", job.getJid(), "1", new Date());
                                                                        sendNotification(notification);

                                                                        dialogLodaing.dismiss();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toasty.error(activity, e.getMessage()).show();
                                                                        dialogLodaing.dismiss();
                                                                    }
                                                                });
                                                    }
                                                })
                                                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .create().show();
                                    } else {
                                        Toasty.error(activity, "Invalid job status.!").show();
                                    }
                                }
                            });

                            jobRequestsViewHolder.btn_acc_job.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (job.getStatus().equals("1")) {

                                        dialogLodaing = new Dialog_Lodaing("Accepting Job.!");
                                        WorkerMain.fragmentController.setupDialog(dialogLodaing);

                                        job.setStatus("2");
                                        job.setConfirmed_date(new Date());
                                        db.collection("jobs").document(job.getJid()).set(job)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        jobRequestsViewHolder.btn_acc_job.setVisibility(View.GONE);
                                                        jobRequestsViewHolder.btn_dec_job.setVisibility(View.GONE);
                                                        jobRequestsViewHolder.btn_gotojob.setVisibility(View.VISIBLE);

//                                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
//                                                    String wajobs = sharedPreferences.getString("wajobs", null);
//                                                    ArrayList<Job> jobs_list;
//                                                    Gson gson = new Gson();
//                                                    if (wajobs == null) {
//                                                        System.out.println("wajobs - null    |||||||||||||||||||||||||||||||||||||||||||||||||");
//
//                                                        jobs_list = new ArrayList<>();
//                                                        jobs_list.add(job);
//                                                        System.out.println(jobs_list.size());
//
//
//                                                    } else {
//                                                        System.out.println("wajobs - null Na    |||||||||||||||||||||||||||||||||||||||||||||||||");
//
//                                                        TypeToken<ArrayList<Job>> typeToken = new TypeToken<ArrayList<Job>>() {
//                                                        };
//
//                                                        jobs_list = gson.fromJson(wajobs, typeToken.getType());
//                                                        System.out.println(jobs_list.size());
//                                                        jobs_list.add(job);
//                                                    }
//
//                                                    String jsonString = gson.toJson(jobs_list);
//
//                                                    sharedPreferences.edit()
//                                                            .putString("wajobs", jsonString)
//                                                            .apply();

                                                        Toasty.success(activity, "Job accepted successfully.!").show();

                                                        notification = new Notification(job.getCid(), "Job Request Accepted", job.getJtype() + " Job Request Accepted", "2", job.getJid(), "1", new Date());
                                                        sendNotification(notification);

                                                        dialogLodaing.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toasty.error(activity, e.getMessage()).show();
                                                        dialogLodaing.dismiss();
                                                    }
                                                });
                                    } else {
                                        Toasty.error(activity, "Invalid job status.!").show();
                                    }
                                }

                            });

                            jobRequestsViewHolder.btn_gotojob.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (job.getStatus().equals("2")) {

//                                        dialogLodaing = new Dialog_Lodaing("Updating Job.!");
//                                        WorkerMain.fragmentController.setupDialog(dialogLodaing);
//
//                                        job.setStatus("3");
//                                        job.setConfirmed_date(new Date());
//                                        db.collection("jobs").document(job.getJid()).set(job)
//                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//                                                        jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);
//                                                        jobRequestsViewHolder.btn_stjob.setVisibility(View.VISIBLE);
//                                                        Toasty.success(activity, "Job updated successfully.!").show();
//                                                        Toasty.warning(activity, "Send Notification.!").show();
//                                                        dialogLodaing.dismiss();
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        Toasty.error(activity, e.getMessage()).show();
//                                                        dialogLodaing.dismiss();
//                                                    }
//                                                });


                                        dialogLodaing = new Dialog_Lodaing("Updating Job.!");
                                        WorkerMain.fragmentController.setupDialog(dialogLodaing);
                                        db.collection("jobs")
                                                .whereEqualTo("wid", WorkerMain.mUser.getUid())
                                                .whereEqualTo("status", "4")
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        if (queryDocumentSnapshots.isEmpty()) {
                                                            job.setStatus("3");
                                                            job.setConfirmed_date(new Date());
                                                            db.collection("jobs").document(job.getJid()).set(job)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);
                                                                            jobRequestsViewHolder.btn_stjob.setVisibility(View.VISIBLE);
                                                                            Toasty.success(activity, "Job updated successfully.!").show();

                                                                            notification = new Notification(job.getCid(), "Worker is Coming", " Worker is coming for " + job.getJtype() + " Job", "3", job.getJid(), "1", new Date());
                                                                            sendNotification(notification);

                                                                            dialogLodaing.dismiss();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toasty.error(activity, e.getMessage()).show();
                                                                            dialogLodaing.dismiss();
                                                                        }
                                                                    });
                                                        } else {
                                                            Toasty.error(activity, "Already started job found").show();
                                                            dialogLodaing.dismiss();
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toasty.error(activity, e.getMessage()).show();
                                                        dialogLodaing.dismiss();
                                                    }
                                                });


                                    } else {
                                        Toasty.error(activity, "Invalid job status.!").show();
                                    }
                                }
                            });


                            jobRequestsViewHolder.btn_stjob.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (job.getStatus().equals("3")) {

                                        dialogLodaing = new Dialog_Lodaing("Starting Job.!");
                                        WorkerMain.fragmentController.setupDialog(dialogLodaing);
                                        db.collection("jobs")
                                                .whereEqualTo("wid", WorkerMain.mUser.getUid())
                                                .whereEqualTo("status", "4")
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        if (queryDocumentSnapshots.isEmpty()) {
                                                            job.setStatus("4");
                                                            job.setStart_time(new Date());
                                                            db.collection("jobs").document(job.getJid()).set(job)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            jobRequestsViewHolder.btn_stjob.setVisibility(View.GONE);
                                                                            jobRequestsViewHolder.tv_jobongoing.setVisibility(View.VISIBLE);
                                                                            Toasty.success(activity, "Job Started successfully.!").show();

                                                                            notification = new Notification(job.getCid(), "Worker Started Job", " Worker is started " + job.getJtype() + " Job", "4", job.getJid(), "1", new Date());
                                                                            sendNotification(notification);

                                                                            dialogLodaing.dismiss();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toasty.error(activity, e.getMessage()).show();
                                                                            dialogLodaing.dismiss();
                                                                        }
                                                                    });
                                                        } else {
                                                            Toasty.error(activity, "Already started job found").show();
                                                            dialogLodaing.dismiss();
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toasty.error(activity, e.getMessage()).show();
                                                        dialogLodaing.dismiss();
                                                    }
                                                });
                                    } else {
                                        Toasty.error(activity, "Invalid job status.!").show();
                                    }
                                }
                            });


                        } else {
                            System.out.println("ELSE AWAAAAAAA");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });


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

    private void makeCall(String number) {
        if (Validations.conValidation(number)) {

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, MainActivity.CALL_PERMISSION_CODE);

            } else {
                activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
            }

        } else {
            Toasty.error(activity, "Invalid number.!").show();
        }
    }

    public void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
}

class JobRequestsViewHolder extends RecyclerView.ViewHolder {

    public TextView type, viewmore, tv_added_date, tv_req_date, tv_cname, tv_ccno, tv_job_id, tv_jobongoing;
    public CircleImageView circleImageView_c_img;
    Button btn_view_location, btn_dec_job, btn_acc_job, btn_callc, btn_gotojob, btn_stjob;
    public CardView cardView;
    public ConstraintLayout moredetails;
    public ConstraintLayout mainlay;

    public JobRequestsViewHolder(@NonNull View itemView) {
        super(itemView);

        type = itemView.findViewById(R.id.tv_w_job_type);
        viewmore = itemView.findViewById(R.id.w_viewmore);
        moredetails = itemView.findViewById(R.id.client_details);
        cardView = itemView.findViewById(R.id.wcardView);

        tv_added_date = itemView.findViewById(R.id.tv_w_addeddate);
        tv_req_date = itemView.findViewById(R.id.tv_w_reqdate);
        tv_cname = itemView.findViewById(R.id.tv_w_c_name);
        tv_ccno = itemView.findViewById(R.id.tv_w_c_cno);
        tv_job_id = itemView.findViewById(R.id.tv_job_id);
        tv_jobongoing = itemView.findViewById(R.id.tv_jobongoing);
        circleImageView_c_img = itemView.findViewById(R.id.circleImageView_w_c_img);

        btn_view_location = itemView.findViewById(R.id.btn_w_view_job_location);
        btn_dec_job = itemView.findViewById(R.id.btn_w_decjob);
        btn_acc_job = itemView.findViewById(R.id.btn_w_accjob);
        btn_callc = itemView.findViewById(R.id.btn_w_call_c);
        btn_gotojob = itemView.findViewById(R.id.btn_w_gotojob);
        btn_stjob = itemView.findViewById(R.id.btn_w_startjob);
        mainlay = itemView.findViewById(R.id.cons_lay_job_req_view);
    }

}
