package com.app.wooker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wooker.AsyncTask.DialogMapLoadAsyncTask;
import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.Notification;
import com.app.wooker.DBClasses.User;
import com.app.wooker.DBClasses.WorkCats;
import com.app.wooker.DBClasses.WorkerTypes;
import com.app.wooker.ViewModels.JobsViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import CustomClasses.DatePickerFragment;
import CustomClasses.RunOnUIThread;
import CustomClasses.Test;
import CustomClasses.Validations;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Frag_Client_Home extends Fragment {

    Spinner spinner_type;
    ArrayList<Job> jobs;
    RecyclerView jobs_view;
    JobsViewAdapter jobsViewAdapter;
    Job job;
    boolean b = false;

    Button btn_addlocation, btn_c_w_select_date, btn_select_worker;

    TextView tv_no_jobs;

    ProgressBar progressBar;

    FirebaseFirestore db;

    ArrayList<String> worker_types_list;

    WorkerTypes workerTypes;

    String permissions[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
    String[] deniedPermissionsAmongLocationAccess;
    List<String> deniedList;
    boolean allPermissionGranted = true;

    public String worker_type = "";

    JobsViewModel jobsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.client_home, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        init(view);


        return view;
    }

    private void init(View view) {

        spinner_type = view.findViewById(R.id.spinner_s_type);
        btn_addlocation = view.findViewById(R.id.btn_c_w_addlocation);
        btn_c_w_select_date = view.findViewById(R.id.btn_c_w_select_date);
        btn_select_worker = view.findViewById(R.id.btn_c_w_s_worker);
        progressBar = view.findViewById(R.id.progressBarchch);
        tv_no_jobs = view.findViewById(R.id.tv_no_jobs_ch);
        jobs_view = view.findViewById(R.id.rv_recent_jobs);

        db = FirebaseFirestore.getInstance();

        worker_types_list = new ArrayList<>();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (MainActivity.isConnectedUseThread(getActivity())) {
                    System.out.println("CONNECTED_CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
                } else {
                    System.out.println("NOT CONNECTED_NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
                }

            }
        }).start();

        loadCats();
        loadJobs();

        btn_select_worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        openMap();
                    }
                }).start();
            }
        });


        btn_addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogMapLoadAsyncTask(getActivity(), "client").execute();
            }
        });

        btn_c_w_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDatePicker(v);
            }
        });

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                worker_type = spinner_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void loadJobs() {
    }


    private void openMap() {
        if (checkLocationPermissionsAreGranted()) {
            openDialogMap();
        } else {
            deniedPermissionsAmongLocationAccess = new String[deniedList.size()];
            deniedPermissionsAmongLocationAccess = deniedList.toArray(deniedPermissionsAmongLocationAccess);

            Toasty.warning(getActivity(), "Need permissions to continue").show();
//                    .makeText(activity, "Need permissions to continue", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), deniedPermissionsAmongLocationAccess[0])) {


                Snackbar.make(getActivity().findViewById(R.id.client_coor_lay), "App needs permissions to access your location & connect internet", Snackbar.LENGTH_LONG)
                        .setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(getActivity(), deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
                                    }
                                })
                        .setActionTextColor((ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary))).show();


            } else {
                ActivityCompat.requestPermissions(getActivity(), deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
            }
        }

    }

    private void loadCats() {

        new Thread(new Runnable() {

            @Override
            public void run() {


                worker_types_list.add("Worker Types");

                db.collection("worker_types").orderBy("type").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                    workerTypes = documentSnapshot.toObject(WorkerTypes.class);

                                    if (!(worker_types_list.contains(workerTypes.getType()))) {
                                        worker_types_list.add(workerTypes.getType());
                                    }

                                }


                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                RunOnUIThread.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_list_item1, worker_types_list);
                        adapter.setDropDownViewResource(R.layout.my_spinner);
//
                        spinner_type.setAdapter(adapter);

                    }
                });


            }
        }).start();


    }

    private void viewDatePicker(View v) {
        DatePickerFragment fragment = new DatePickerFragment("Client_Home");
        ClientMain.fragmentController.setupDialog(fragment);
    }


    private boolean checkLocationPermissionsAreGranted() {
        deniedList = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
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

        if (MainActivity.isServicesOK(getActivity())) {

            if (MainActivity.isConnectedUseThread(getActivity())) {

                if (DialogMap.location != null) {
                    if (spinner_type.getSelectedItemPosition() == 0) {
                        RunOnUIThread.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toasty.error(getActivity(), "Please select worker type").show();
                            }
                        });
                    } else {
                        if (btn_c_w_select_date.getText().toString().equals("Select Date")) {
                            RunOnUIThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toasty.error(getActivity(), "Please select date").show();
                                }
                            });

                        } else {

                            RunOnUIThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    spinner_type.setSelection(0);
                                }
                            });
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                    .replace(R.id.cons_lay_client, new Frag_Dialog_Map(worker_type, btn_c_w_select_date.getText().toString()))
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                } else {
                    RunOnUIThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasty.error(getActivity(), "Please add job location.!").show();
                        }
                    });
                }
            } else {
                Snackbar.make(getActivity().findViewById(R.id.client_coor_lay), "Please connect to internet", Snackbar.LENGTH_LONG).setAction("Connect",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                                getActivity().startActivity(intent);
                            }
                        })
                        .setActionTextColor((ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary))).show();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        jobsViewModel = ViewModelProviders.of(this).get(JobsViewModel.class);
        jobsViewModel.startListener();
        jobsViewModel.getJobs().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(@Nullable List<Job> jobs2) {
                System.out.println("notifyDataSetChanged calling");
                jobs = new ArrayList<>();
                for (Job job : jobs2) {
                    if (Integer.parseInt(job.getStatus()) <= 4) {
                        jobs.add(job);
                    } else {
                        System.out.println("Other Job : " + job.getJid());
                    }
                }

                if (jobsViewAdapter == null) {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    jobs_view.setLayoutManager(linearLayoutManager);
                    jobsViewAdapter = new JobsViewAdapter(jobs, getActivity());
                    jobs_view.setAdapter(jobsViewAdapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    jobsViewAdapter.setJobs(jobs);
                    jobsViewAdapter.notifyDataSetChanged();
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
                }
            }
        });
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
                Snackbar.make(getActivity().findViewById(R.id.client_coor_lay), "App needs permissions to continue", Snackbar.LENGTH_LONG).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(getActivity(), deniedPermissionsAmongLocationAccess, MainActivity.LOCATION_PERMITION_CODE);
                            }
                        })
                        .setActionTextColor((ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary))).show();
            }
        }
    }
}

class JobsViewAdapter extends RecyclerView.Adapter {

    ArrayList<Job> jobs;
    FirebaseFirestore db;
    Activity activity;
    User worker;
    Dialog_Lodaing dialogLodaing;
    Notification notification;
    DocumentReference document;

    public JobsViewAdapter(ArrayList<Job> jobs, Activity activity) {
        this.jobs = jobs;
        this.activity = activity;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.job_view_client, viewGroup, false);
        JobsViewHolder jobsViewHolder = new JobsViewHolder(view);
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
                                final JobsViewHolder jobsViewHolder = (JobsViewHolder) viewHolder;
                                new Thread(new Runnable() {
                                    User uWorker = worker;

                                    @Override
                                    public void run() {
                                        RunOnUIThread.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                System.out.println(uWorker.getFname() + " " + uWorker.getLname() + " : " + uWorker.getImageUrl());
                                                System.out.println(uWorker.getImageUrl().equals(""));

                                                System.out.println(uWorker.getImageUrl().length() < 1);
//                                                System.out.println(jobsViewHolder.worker_img == null && uWorker.getImageUrl().equals("")));
                                                try {

                                                if (jobsViewHolder.worker_img != null || (!uWorker.getImageUrl().equals(""))) {
                                                    System.out.println("Setting Worker image.!");
                                                    Picasso.with(activity)
                                                            .load(uWorker.getImageUrl())
                                                            .into(jobsViewHolder.worker_img);
                                                } else {
                                                    System.out.println("worker img Null.!");
                                                }
                                                }catch (Exception e){
                                                    System.out.println("Image Null");
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
                                } else if (job.getStatus().equals("2")) {
                                    jobsViewHolder.tv_status.setText("Confirmed");
                                    jobsViewHolder.btn_finjob.setVisibility(View.GONE);
                                    jobsViewHolder.btn_decjob.setVisibility(View.VISIBLE);
                                } else if (job.getStatus().equals("3")) {
                                    jobsViewHolder.tv_status.setText("On the way");
                                    jobsViewHolder.btn_finjob.setVisibility(View.GONE);
                                    jobsViewHolder.btn_decjob.setVisibility(View.VISIBLE);
                                } else if (job.getStatus().equals("4")) {
                                    jobsViewHolder.tv_status.setText("Job Started");
                                    jobsViewHolder.btn_decjob.setVisibility(View.GONE);
                                    jobsViewHolder.btn_finjob.setVisibility(View.VISIBLE);
                                }

                                jobsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TransitionManager.beginDelayedTransition(jobsViewHolder.cons_lay_worker_details);
                                        if (jobsViewHolder.cons_lay_worker_details.getVisibility() == View.GONE) {
                                            jobsViewHolder.cons_lay_worker_details.setVisibility(View.VISIBLE);
                                            jobsViewHolder.tv_view_more.setText("Hide Worker Details");
                                        } else {
                                            jobsViewHolder.cons_lay_worker_details.setVisibility(View.GONE);
                                            jobsViewHolder.tv_view_more.setText("View Worker Details");
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
//                                                                            jobs.remove(position);
//                                                                            notifyItemRemoved(position);
//                                                                            notifyItemRangeChanged(position, jobs.size());
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

                                            Dialog_Rate_Worker dialog_rate_worker = new Dialog_Rate_Worker(job, activity, JobsViewAdapter.this, "jva", position);

                                            ClientMain.fragmentController.setupDialog(dialog_rate_worker);
                                            System.out.println("Awa ita passe ekataaa.......................................!!");

                                        } else {
                                            Toasty.error(activity, "Invalid job status.!").show();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("ERROR ERROR ERROR ERROR ERROR ERROR ERROR");
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

class JobsViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_type, tv_status, tv_view_more, tv_message, tv_cno, tv_name, tv_addeddate, tv_reqdate, tv_lbl_addeddate;
    public CardView cardView;
    public ConstraintLayout cons_lay_worker_details;
    public Button btn_decjob, btn_finjob, btn_call;
    public CircleImageView worker_img;
    public ConstraintLayout mainlay;

    public JobsViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_type = itemView.findViewById(R.id.tv_c_job_type);
        tv_status = itemView.findViewById(R.id.tv_c_status);
        tv_message = itemView.findViewById(R.id.tv_message_jv_c);
        tv_cno = itemView.findViewById(R.id.tv_c_w_cno);
        tv_name = itemView.findViewById(R.id.tv_c_w_name);
        tv_reqdate = itemView.findViewById(R.id.tv_c_reqdate);
        tv_addeddate = itemView.findViewById(R.id.tv_c_addeddate);
        tv_lbl_addeddate = itemView.findViewById(R.id.tv_lbl_addeddate_c);
        cardView = itemView.findViewById(R.id.c_job_view_cardView);
        cons_lay_worker_details = itemView.findViewById(R.id.worker_details_c_job_view);
        tv_view_more = itemView.findViewById(R.id.c_viewmore);
        btn_decjob = itemView.findViewById(R.id.btn_decline_job_c);
        btn_finjob = itemView.findViewById(R.id.btn_finishjob_c);
        btn_call = itemView.findViewById(R.id.btn_c_call_w);
        worker_img = itemView.findViewById(R.id.circleImageView_c_w_img);
        mainlay = itemView.findViewById(R.id.cons_lay_c_job_view);
    }
}
