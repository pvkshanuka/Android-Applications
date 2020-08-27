package com.app.wooker;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.support.v7.widget.RecyclerView.Adapter;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.Notification;
import com.app.wooker.DBClasses.User;
import com.app.wooker.ViewModels.JobsViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import CustomClasses.DatePickerFragment;
import CustomClasses.RunOnUIThread;
import CustomClasses.Test;
import CustomClasses.Validations;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Frag_Client_View_All_Jobs extends Fragment {

    Spinner spinner_type, spinner_status;
    Button btn_s_date, btn_reset;
    RecyclerView all_jobs_view;
    ArrayList<Job> jobs;
    ArrayList<Job> client_jobs;
    ProgressBar progressBar;
    JobsViewModel jobsViewModel;
    AllJobsViewAdapter allJobsViewAdapter;
    FirebaseFirestore db;
    boolean b = false;
    ArrayList<String> list_worktypes;
    ArrayList<String> list_jobstatus;
    DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Calendar calendar;
    String date = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("AWA 2");
        View view = inflater.inflate(R.layout.client_view_all_jobs, container, false);

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date = Validations.dateObjToString(calendar.getTime(), "yyyy-MM-dd");
                btn_s_date.setText(date);
                loadJobs();
            }
        };

        init(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_s_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDatePicker(v);
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_type.setSelection(0);
                spinner_status.setSelection(0);
                date = null;
                btn_s_date.setText("Job Date");
                loadJobs();
            }
        });

    }


    private void init(View view) {
        spinner_type = view.findViewById(R.id.sp_c_v_a_j_select_w_type);
        spinner_status = view.findViewById(R.id.sp_c_v_a_j_select_j_status);
        btn_s_date = view.findViewById(R.id.btn_c_v_a_j_s_date);
        btn_reset = view.findViewById(R.id.btn_reset_c_v_a_j);
        all_jobs_view = view.findViewById(R.id.rv_recent_c_v_a_jobs);
        progressBar = view.findViewById(R.id.progressBarc_v_a_jobs);
        datePickerDialog = new DatePickerDialog(
                getContext(), onDateSetListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    private void viewDatePicker(View v) {
        datePickerDialog.show();
    }


    private void addStatusToSpinner(ArrayList<String> status) {
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select Status");

        for (String s : status) {
            if (s.equals("1")) {
                spinnerArray.add("Not Confirmed");
            } else if (s.equals("2")) {
                spinnerArray.add("Confirmed");
            } else if (s.equals("3")) {
                spinnerArray.add("On the way");
            } else if (s.equals("4")) {
                spinnerArray.add("Started");
            } else if (s.equals("5")) {
                spinnerArray.add("Finished");
            } else if (s.equals("6")) {
                spinnerArray.add("Declined by you");
            } else if (s.equals("7")) {
                spinnerArray.add("Declined by worker");
            }

        }

        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    ((TextView) view.findViewById(R.id.mysimplelist)).setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
                    ((TextView) view.findViewById(R.id.mysimplelist)).setTextSize(13);
                }
                loadJobs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_list_item1, spinnerArray);
        adapter.setDropDownViewResource(R.layout.my_spinner);
        RunOnUIThread.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner_status.setAdapter(adapter);

            }
        });
    }

    private void addTypesToSpinner(ArrayList<String> job_types) {

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position != 0) {
                    ((TextView) view.findViewById(R.id.mysimplelist)).setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
                    ((TextView) view.findViewById(R.id.mysimplelist)).setTextSize(13);
                }
                loadJobs();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_list_item1, job_types);
        adapter.setDropDownViewResource(R.layout.my_spinner);
        RunOnUIThread.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner_type.setAdapter(adapter);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadJobs();
    }

    private void loadJobs() {
        try {

            jobsViewModel = ViewModelProviders.of(this).get(JobsViewModel.class);
            jobsViewModel.startListener();
            jobsViewModel.getJobs().observe(this, new Observer<List<Job>>() {
                @Override
                public void onChanged(@Nullable List<Job> jobs2) {
                    jobs = new ArrayList<>();
                    client_jobs = new ArrayList<>();

                    ArrayList<Job> list1 = new ArrayList<>();
                    ArrayList<Job> list2 = new ArrayList<>();
                    ArrayList<Job> listf = new ArrayList<>();

                    for (Job job : jobs2) {
                        client_jobs.add(job);
                        jobs.add(job);
                    }

                    if (spinner_status.getSelectedItemPosition() > 0) {
                        for (Job job : jobs) {
                            String status = spinner_status.getSelectedItem().toString();
                            String statusno = null;

                            if (status.equals("Not Confirmed")) {
                                statusno = "1";
                            } else if (status.equals("Confirmed")) {
                                statusno = "2";
                            } else if (status.equals("On the way")) {
                                statusno = "3";
                            } else if (status.equals("Started")) {
                                statusno = "4";
                            } else if (status.equals("Finished")) {
                                statusno = "5";
                            } else if (status.equals("Declined by you")) {
                                statusno = "6";
                            } else if (status.equals("Declined by worker")) {
                                statusno = "7";
                            }

                            if (job.getStatus().equals(statusno)) {
                                listf.add(job);
                            }
                        }
                    } else {
                        listf.addAll(jobs);
                    }

                    if (spinner_type.getSelectedItemPosition() > 0) {
                        for (Job job : listf) {
                            if (job.getJtype().equals(spinner_type.getSelectedItem().toString())) {
                                list1.add(job);
                            }
                        }
                        listf.removeAll(listf);
                        listf.addAll(list1);
                        list1.removeAll(list1);
                    }

                    if (date != null) {
                        for (Job job : listf) {
                            if (date.equals(Validations.dateObjToString(job.getJob_date(), "yyyy-MM-dd"))) {
                                list1.add(job);
                            }
                        }

                        listf.removeAll(listf);
                        listf.addAll(list1);
                        list1.removeAll(list1);

                    }

                    if (allJobsViewAdapter == null) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        all_jobs_view.setLayoutManager(linearLayoutManager);
                        allJobsViewAdapter = new AllJobsViewAdapter(listf, getActivity());
                        all_jobs_view.setAdapter(allJobsViewAdapter);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        allJobsViewAdapter.setJobs(listf);
                        allJobsViewAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                    if (b) {

                        Toasty.info(getActivity(), "Jobs Loaded.!").show();
                    } else {
                        b = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (client_jobs == null || client_jobs.isEmpty()) {
                                } else {
                                    list_jobstatus = new ArrayList<>();
                                    list_worktypes = new ArrayList<>();
                                    list_worktypes.add("Select job type");

                                    for (Job job : client_jobs) {
                                        if (!list_jobstatus.contains(job.getStatus())) {
                                            list_jobstatus.add(job.getStatus());
                                        }
                                        if (!list_worktypes.contains(job.getJtype())) {
                                            list_worktypes.add(job.getJtype());
                                        }
                                    }
                                    addStatusToSpinner(list_jobstatus);
                                    addTypesToSpinner(list_worktypes);
                                }
                            }
                        }).start();
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
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class AllJobsViewAdapter extends RecyclerView.Adapter {

    ArrayList<Job> jobs;
    Activity activity;
    User worker;
    FirebaseFirestore db;
    Dialog_Lodaing dialogLodaing;
    Notification notification;
    DocumentReference document;

    public AllJobsViewAdapter(ArrayList<Job> jobs, Activity activity) {
        this.jobs = jobs;
        this.activity = activity;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.job_view_client, viewGroup, false);
        AllJobsViewHolder jobsViewHolder = new AllJobsViewHolder(view);
        return jobsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

        final Job job = jobs.get(position);

        db.collection("users").document(job.getWid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            try {

                                worker = documentSnapshot.toObject(User.class);
                                final AllJobsViewHolder jobsViewHolder = (AllJobsViewHolder) viewHolder;
                                new Thread(new Runnable() {
                                    User uWorker = worker;

                                    @Override
                                    public void run() {
                                        RunOnUIThread.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                System.out.println(uWorker.getFname() + " " + uWorker.getLname() + " : " + uWorker.getImageUrl());
                                                if (jobsViewHolder.worker_img != null) {
                                                    Picasso.with(activity)
                                                            .load(uWorker.getImageUrl())
                                                            .into(jobsViewHolder.worker_img);
                                                } else {
                                                }
                                            }
                                        });
                                    }
                                }).start();

                                jobsViewHolder.tv_type.setText(job.getJtype());
                                jobsViewHolder.tv_addeddate.setText(Validations.dateObjToString(job.getAdded_date(), "yyyy-MM-dd"));
                                jobsViewHolder.tv_reqdate.setText(Validations.dateObjToString(job.getJob_date(), "yyyy-MM-dd"));
                                jobsViewHolder.tv_name.setText(worker.getFname() + " " + worker.getLname());
                                jobsViewHolder.tv_cno.setText(worker.getCno());

                                if (job.getStatus().equals("1")) {
                                    jobsViewHolder.tv_status.setText("Not Confirmed");
                                    jobsViewHolder.btn_finjob.setVisibility(View.GONE);
                                    jobsViewHolder.btn_decjob.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_jv_message.setVisibility(View.GONE);

                                    jobsViewHolder.tv_l_con_date.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_s_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_payment.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_jobh.setVisibility(View.GONE);

                                    jobsViewHolder.tv_con_date.setVisibility(View.GONE);
                                    jobsViewHolder.tv_s_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_payment.setVisibility(View.GONE);
                                    jobsViewHolder.tv_jobh.setVisibility(View.GONE);
                                } else if (job.getStatus().equals("2")) {
                                    jobsViewHolder.tv_status.setText("Confirmed");
                                    jobsViewHolder.btn_finjob.setVisibility(View.GONE);
                                    jobsViewHolder.btn_decjob.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_jv_message.setVisibility(View.GONE);

                                    jobsViewHolder.tv_con_date.setText(Validations.dateObjToString(job.getConfirmed_date(), "yyyy-MM-dd"));

                                    jobsViewHolder.tv_l_con_date.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_l_s_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_payment.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_jobh.setVisibility(View.GONE);

                                    jobsViewHolder.tv_con_date.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_s_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_payment.setVisibility(View.GONE);
                                    jobsViewHolder.tv_jobh.setVisibility(View.GONE);
                                } else if (job.getStatus().equals("3")) {
                                    jobsViewHolder.tv_status.setText("On the way");
                                    jobsViewHolder.btn_finjob.setVisibility(View.GONE);
                                    jobsViewHolder.btn_decjob.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_jv_message.setVisibility(View.GONE);

                                    jobsViewHolder.tv_con_date.setText(Validations.dateObjToString(job.getConfirmed_date(), "yyyy-MM-dd"));

                                    jobsViewHolder.tv_l_con_date.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_l_s_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_payment.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_jobh.setVisibility(View.GONE);

                                    jobsViewHolder.tv_con_date.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_s_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_payment.setVisibility(View.GONE);
                                    jobsViewHolder.tv_jobh.setVisibility(View.GONE);
                                } else if (job.getStatus().equals("4")) {
                                    jobsViewHolder.tv_status.setText("Job Started");
                                    jobsViewHolder.btn_decjob.setVisibility(View.GONE);
                                    jobsViewHolder.btn_finjob.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_jv_message.setVisibility(View.GONE);

                                    jobsViewHolder.tv_con_date.setText(Validations.dateObjToString(job.getConfirmed_date(), "yyyy-MM-dd"));
                                    jobsViewHolder.tv_s_time.setText(Validations.dateObjToString(job.getStart_time(), "hh:mm a"));

                                    jobsViewHolder.tv_l_con_date.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_l_s_time.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_payment.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_jobh.setVisibility(View.GONE);

                                    jobsViewHolder.tv_con_date.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_s_time.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_payment.setVisibility(View.GONE);
                                    jobsViewHolder.tv_jobh.setVisibility(View.GONE);
                                } else if (job.getStatus().equals("5")) {
                                    jobsViewHolder.tv_status.setText("Job Finished");
                                    jobsViewHolder.btn_decjob.setVisibility(View.GONE);
                                    jobsViewHolder.btn_finjob.setVisibility(View.GONE);
                                    jobsViewHolder.tv_jv_message.setText("FINISHED JOB");
                                    jobsViewHolder.tv_jv_message.setTextColor(ContextCompat.getColor(activity, R.color.color_green));
                                    jobsViewHolder.tv_jv_message.setVisibility(View.VISIBLE);

                                    jobsViewHolder.tv_con_date.setText(Validations.dateObjToString(job.getConfirmed_date(), "yyyy-MM-dd"));
                                    jobsViewHolder.tv_s_time.setText(Validations.dateObjToString(job.getStart_time(), "hh:mm a"));
                                    jobsViewHolder.tv_e_time.setText(Validations.dateObjToString(job.getEnd_time(), "hh:mm a"));
                                    jobsViewHolder.tv_payment.setText(job.getPayment() + "");
                                    jobsViewHolder.tv_jobh.setText(((((job.getEnd_time().getTime() - job.getStart_time().getTime()) / 1000) / 60) / 60) + "h" + ((((job.getEnd_time().getTime() - job.getStart_time().getTime()) / 1000) / 60) % 60) + "m");

                                    jobsViewHolder.tv_l_con_date.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_l_s_time.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_l_e_time.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_l_payment.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_l_jobh.setVisibility(View.VISIBLE);

                                    jobsViewHolder.tv_con_date.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_s_time.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_e_time.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_payment.setVisibility(View.VISIBLE);
                                    jobsViewHolder.tv_jobh.setVisibility(View.VISIBLE);
                                } else if (job.getStatus().equals("6")) {
                                    jobsViewHolder.tv_status.setText("Job Declined");
                                    jobsViewHolder.btn_decjob.setVisibility(View.GONE);
                                    jobsViewHolder.btn_finjob.setVisibility(View.GONE);
                                    jobsViewHolder.tv_jv_message.setText("JOB DECLINED BY YOU");
                                    jobsViewHolder.tv_jv_message.setTextColor(ContextCompat.getColor(activity, R.color.warning_red));
                                    jobsViewHolder.tv_jv_message.setVisibility(View.VISIBLE);

                                    jobsViewHolder.tv_l_con_date.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_s_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_payment.setVisibility(View.GONE);

                                    jobsViewHolder.tv_con_date.setVisibility(View.GONE);
                                    jobsViewHolder.tv_s_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_payment.setVisibility(View.GONE);
                                } else if (job.getStatus().equals("7")) {
                                    jobsViewHolder.tv_status.setText("Job Declined");
                                    jobsViewHolder.btn_decjob.setVisibility(View.GONE);
                                    jobsViewHolder.btn_finjob.setVisibility(View.GONE);
                                    jobsViewHolder.tv_jv_message.setText("JOB DECLINED BY WORKER");
                                    jobsViewHolder.tv_jv_message.setTextColor(ContextCompat.getColor(activity, R.color.warning_red));
                                    jobsViewHolder.tv_jv_message.setVisibility(View.VISIBLE);

                                    jobsViewHolder.tv_l_con_date.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_s_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_l_payment.setVisibility(View.GONE);

                                    jobsViewHolder.tv_con_date.setVisibility(View.GONE);
                                    jobsViewHolder.tv_s_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_e_time.setVisibility(View.GONE);
                                    jobsViewHolder.tv_payment.setVisibility(View.GONE);
                                }

                                jobsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TransitionManager.beginDelayedTransition(jobsViewHolder.cons_lay_worker_details);
                                        if (jobsViewHolder.cons_lay_worker_details.getVisibility() == View.GONE) {
                                            jobsViewHolder.cons_lay_worker_details.setVisibility(View.VISIBLE);
                                            jobsViewHolder.tv_view_more.setText("Hide More Details");
                                        } else {
                                            jobsViewHolder.cons_lay_worker_details.setVisibility(View.GONE);
                                            jobsViewHolder.tv_view_more.setText("View More Details");
                                        }
                                    }
                                });

                                jobsViewHolder.btn_call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        makeCall(worker.getCno());
                                    }
                                });

                                jobsViewHolder.btn_decjob.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (job.getStatus().equals("1") || job.getStatus().equals("2") || job.getStatus().equals("3")) {


                                            new AlertDialog.Builder(activity)
                                                    .setTitle("Confirm message.!")
                                                    .setMessage("Are you suer you want to DECLINE this job.?")
                                                    .setPositiveButton("Decline Job", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialogLodaing = new Dialog_Lodaing("Declineing Job.!");
                                                            ClientMain.fragmentController.setupDialog(dialogLodaing);
                                                            job.setStatus("6");
                                                            db.collection("jobs").document(job.getJid()).set(job)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toasty.success(activity, "Job declined successfully.!").show();

                                                                            notification = new Notification(job.getWid(), "Job Declined", job.getJtype() + " Job Declined", "6", job.getJid(), "1", new Date());
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

                                jobsViewHolder.btn_finjob.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (job.getStatus().equals("4")) {

                                            Dialog_Rate_Worker dialog_rate_worker = new Dialog_Rate_Worker(job, activity, AllJobsViewAdapter.this, "ajva", position);
                                            ClientMain.fragmentController.setupDialog(dialog_rate_worker);

                                        } else {
                                            Toasty.error(activity, "Invalid job status.!").show();
                                        }
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
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

class AllJobsViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_type, tv_status, tv_view_more, tv_message, tv_cno, tv_name, tv_addeddate, tv_reqdate, tv_lbl_addeddate, tv_jv_message, tv_l_con_date, tv_con_date, tv_l_s_time, tv_s_time, tv_l_e_time, tv_e_time, tv_l_payment, tv_payment, tv_l_jobh, tv_jobh;
    public CardView cardView;
    public ConstraintLayout cons_lay_worker_details;
    public Button btn_decjob, btn_finjob, btn_call;
    public CircleImageView worker_img;
    public ConstraintLayout mainlay;

    public AllJobsViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_type = itemView.findViewById(R.id.tv_c_job_type);
        tv_jv_message = itemView.findViewById(R.id.tv_message_jv_c);
        tv_status = itemView.findViewById(R.id.tv_c_status);
        tv_message = itemView.findViewById(R.id.tv_message_jv_c);
        tv_cno = itemView.findViewById(R.id.tv_c_w_cno);
        tv_name = itemView.findViewById(R.id.tv_c_w_name);
        tv_reqdate = itemView.findViewById(R.id.tv_c_reqdate);
        tv_addeddate = itemView.findViewById(R.id.tv_c_addeddate);
        tv_lbl_addeddate = itemView.findViewById(R.id.tv_lbl_addeddate_c);

        tv_l_con_date = itemView.findViewById(R.id.tv_l_con_date2);
        tv_con_date = itemView.findViewById(R.id.tv_con_date2);
        tv_l_s_time = itemView.findViewById(R.id.tv_l_s_time2);
        tv_s_time = itemView.findViewById(R.id.tv_s_time2);
        tv_l_e_time = itemView.findViewById(R.id.tv_l_e_time2);
        tv_e_time = itemView.findViewById(R.id.tv_e_time2);
        tv_l_payment = itemView.findViewById(R.id.tv_l_payment2);
        tv_payment = itemView.findViewById(R.id.tv_payment2);
        tv_l_jobh = itemView.findViewById(R.id.tv_l_jobh2);
        tv_jobh = itemView.findViewById(R.id.tv_jobh2);

        tv_view_more = itemView.findViewById(R.id.c_viewmore);

        cardView = itemView.findViewById(R.id.c_job_view_cardView);
        cons_lay_worker_details = itemView.findViewById(R.id.worker_details_c_job_view);
        btn_decjob = itemView.findViewById(R.id.btn_decline_job_c);
        btn_finjob = itemView.findViewById(R.id.btn_finishjob_c);
        btn_call = itemView.findViewById(R.id.btn_c_call_w);
        worker_img = itemView.findViewById(R.id.circleImageView_c_w_img);
        mainlay = itemView.findViewById(R.id.cons_lay_c_job_view);

    }
}
