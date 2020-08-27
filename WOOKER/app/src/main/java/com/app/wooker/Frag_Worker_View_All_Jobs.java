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

import com.app.wooker.AsyncTask.DialogMapLoadAsyncTask;
import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.Notification;
import com.app.wooker.DBClasses.User;
import com.app.wooker.ViewModels.JobsViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import CustomClasses.DatePickerFragment;
import CustomClasses.RunOnUIThread;
import CustomClasses.Test;
import CustomClasses.Validations;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Frag_Worker_View_All_Jobs extends Fragment {


    Spinner spinner_type, spinner_worker, spinner_status;
    Button btn_s_date, btn_reset;
    RecyclerView all_jobs_view;
    ArrayList<Job> jobs;
    VAllJobsViewAdapter vjobsViewAdapter;
    JobsViewModel jobsViewModel;
    ProgressBar progressBar;
    ArrayList<Job> worker_jobs;
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
        View view = inflater.inflate(R.layout.worker_view_all_jobs, container, false);

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                System.out.println("AWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> onDateSet");
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
        System.out.println("AWA 3");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("AWA 4");
//        addTypesToSpinner();
//        addStatusToSpinner();
//        addWorkersToSpinner();

        System.out.println("AWA 5");

        btn_s_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
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

        System.out.println("AWA 6");
//        tests = new ArrayList<>();
//
//        tests.add(new Test("Carpainter", "On Going"));
//        tests.add(new Test("Electrition", "Pending"));
//        tests.add(new Test("Dog Trainer", "Finihed"));
//        tests.add(new Test("Carpainter", "Canceled"));
//        tests.add(new Test("Micanican", "Finihed"));
//        tests.add(new Test("Electrition", "Finihed"));
//        tests.add(new Test("Painter", "Pendingg"));
//        tests.add(new Test("Carpainter", "Finihed"));
//        tests.add(new Test("Cleaner", "Canceled"));
//        tests.add(new Test("Carpainter", "Pending"));
//        tests.add(new Test("Plumer", "Pending"));
//        tests.add(new Test("Servent", "Finihed"));
//        tests.add(new Test("Carpainter", "Pending"));
//        tests.add(new Test("Micanican", "Pending"));
//
//        all_jobs_view = view.findViewById(R.id.rv_w_v_a_jobs);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        all_jobs_view.setLayoutManager(linearLayoutManager);
//
//        vjobsViewAdapter = new VAllJobsViewAdapter(tests);
//        all_jobs_view.setAdapter(vjobsViewAdapter);

        System.out.println("AWA 7");

    }


    private void init(View view) {
        System.out.println("AWA 2.1");
        spinner_type = view.findViewById(R.id.sp_c_v_a_j_select_w_type);
        System.out.println("AWA 2.2");
        spinner_status = view.findViewById(R.id.sp_c_v_a_j_select_j_status);
        System.out.println("AWA 2.3");
        spinner_worker = view.findViewById(R.id.sp_c_v_a_j_select_worker);
        System.out.println("AWA 2.4");
        btn_s_date = view.findViewById(R.id.btn_c_v_a_j_s_date);
        System.out.println("AWA 2.5");
        progressBar = view.findViewById(R.id.progressBar_w_v_a_j);
        all_jobs_view = view.findViewById(R.id.rv_w_v_a_jobs);
        btn_reset = view.findViewById(R.id.btn_reset_w_v_a_j);
        datePickerDialog = new DatePickerDialog(
                getContext(), onDateSetListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    private void viewDatePicker(View v) {
        DatePickerFragment fragment = new DatePickerFragment("Client_View_All_Jobs");
        WorkerMain.fragmentController.setupDialog(fragment);
    }
//
//    private void addWorkersToSpinner() {
//
//
//        List<String> spinnerArray = new ArrayList<>();
//        spinnerArray.add("Client");
//        spinnerArray.add("Kusal Shanuka");
//        spinnerArray.add("Damith Chamara");
//        spinnerArray.add("Thusitha Fernando");
//        spinnerArray.add("Avishka Chammika Chammika Chammika");
//        spinnerArray.add("Kasun Asanka");
//
//        spinner_worker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//                if (position != 0) {
//                    ((TextView) view.findViewById(R.id.mysimplelist)).setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
//                    ((TextView) view.findViewById(R.id.mysimplelist)).setTextSize(13);
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_list_item1, spinnerArray);
//        adapter.setDropDownViewResource(R.layout.my_spinner);
//
//        spinner_worker.setAdapter(adapter);
//
//    }

    private void addStatusToSpinner(ArrayList<String> status) {
        System.out.println("AWAAAAA addStatusToSpinner : " + status.size());
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
                spinnerArray.add("Declined by client");
            } else if (s.equals("7")) {
                spinnerArray.add("Declined by you");
            }

//            s
        }

//        spinnerArray.add("Finish");
//        spinnerArray.add("Pending");
//        spinnerArray.add("Confirmed");
//        spinnerArray.add("Deleted");

        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("onItemSelected POSITION : " + position);
                if (position != 0) {
                    ((TextView) view.findViewById(R.id.mysimplelist)).setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
                    ((TextView) view.findViewById(R.id.mysimplelist)).setTextSize(13);
//                    jobsViewModel.listenerRegistration.remove();
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

//        List<String> spinnerArray = new ArrayList<>();
//        spinnerArray.add("User Type");
//        spinnerArray.add("English");
//        spinnerArray.add("Sinhala");
//        spinnerArray.add("English");
//        spinnerArray.add("Sinhala");
//        spinnerArray.add("English");
//        spinnerArray.add("Sinhala");

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position != 0) {
                    ((TextView) view.findViewById(R.id.mysimplelist)).setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
                    ((TextView) view.findViewById(R.id.mysimplelist)).setTextSize(13);
//                    jobsViewModel.listenerRegistration.remove();
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
//        jobsViewModel = ViewModelProviders.of(this).get(JobsViewModel.class);
//        jobsViewModel.startListener();
//        jobsViewModel.getJobs().observe(this, new Observer<List<Job>>() {
//            @Override
//            public void onChanged(@Nullable List<Job> jobs2) {
//                System.out.println("notifyDataSetChanged calling");
//                jobs = new ArrayList<>();
//                for (Job job : jobs2) {
////                    orderBy("status")
////                            .whereEqualTo("cid", ClientMain.mUser.getUid())
////                            .whereLessThanOrEqualTo("status", "4")
////                            .orderBy("job_date")
//                    if (job.getWid().equals(WorkerMain.mUser.getUid())) {
//                        jobs.add(job);
//                    } else {
//                        System.out.println("Other Job : " + job.getJid());
//                    }
//                }
//
//                if (vjobsViewAdapter == null) {
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//                    all_jobs_view.setLayoutManager(linearLayoutManager);
//                    vjobsViewAdapter = new VAllJobsViewAdapter(jobs, getActivity());
//                    all_jobs_view.setAdapter(vjobsViewAdapter);
//                    progressBar.setVisibility(View.GONE);
//                } else {
//                    vjobsViewAdapter.setJobs(jobs);
//                    vjobsViewAdapter.notifyDataSetChanged();
//                    progressBar.setVisibility(View.GONE);
//                }
//
//
//            }
//        });
//
//        jobsViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(@Nullable Boolean aBoolean) {
//                if (aBoolean) {
//                    progressBar.setVisibility(View.VISIBLE);
//                } else {
//                    progressBar.setVisibility(View.GONE);
////                    jobs_view.smoothScrollToPosition(jobs.size() - 1);
//                }
//            }
//        });
//    }
    }

    private void loadJobs() {
        try {

            jobsViewModel = ViewModelProviders.of(this).get(JobsViewModel.class);
            jobsViewModel.startListener();
            jobsViewModel.getJobs().observe(this, new Observer<List<Job>>() {
                @Override
                public void onChanged(@Nullable List<Job> jobs2) {
                    System.out.println("notifyDataSetChanged calling");
                    jobs = new ArrayList<>();
                    worker_jobs = new ArrayList<>();

                    ArrayList<Job> list1 = new ArrayList<>();
                    ArrayList<Job> listf = new ArrayList<>();

                    for (Job job : jobs2) {
//                    orderBy("status")
//                            .whereEqualTo("cid", ClientMain.mUser.getUid())
//                            .whereLessThanOrEqualTo("status", "4")
//                            .orderBy("job_date")
//                        if (job.getWid().equals(WorkerMain.mUser.getUid())) {

                        worker_jobs.add(job);
                        jobs.add(job);


//                        } else {
//                            System.out.println("Other Job : " + job.getJid());
//                        }
                        System.out.println("SIZE >>>>>>>>>>>>>>>>>>>>>>>>>>> " + worker_jobs.size());
                    }

                    System.out.println("POSITION >>>>>>>>>>>>>>>> " + spinner_status.getSelectedItemPosition());
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
                            } else if (status.equals("Declined by client")) {
                                statusno = "6";
                            } else if (status.equals("Declined by you")) {
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
                        System.out.println("DATE NULLLLLLLLL NA >>>>>>>>>>>>>>");
                        for (Job job : listf) {

                            if (date.equals(Validations.dateObjToString(job.getJob_date(), "yyyy-MM-dd"))) {
                                list1.add(job);
                            }

                        }

                        listf.removeAll(listf);
                        listf.addAll(list1);
                        list1.removeAll(list1);

                    } else {
                        System.out.println("DATE NULLLLLLLLL >>>>>>>>>>>>>>");

                    }

//                    if (listf.isEmpty()) {
//                        listf.addAll(jobs);
//                    }

                    if (vjobsViewAdapter == null) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        all_jobs_view.setLayoutManager(linearLayoutManager);
                        vjobsViewAdapter = new VAllJobsViewAdapter(listf, getActivity());
                        all_jobs_view.setAdapter(vjobsViewAdapter);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        vjobsViewAdapter.setJobs(listf);
                        vjobsViewAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    if (b) {

                        Toasty.info(getActivity(), "Jobs Loaded.!").show();
                    } else {
                        b = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (worker_jobs == null || worker_jobs.isEmpty()) {
                                    System.out.println("client_jobs EMPTY");
                                } else {
                                    list_jobstatus = new ArrayList<>();
                                    list_worktypes = new ArrayList<>();
                                    list_worktypes.add("Select job type");

                                    for (Job job : worker_jobs) {
                                        if (!list_jobstatus.contains(job.getStatus())) {
                                            list_jobstatus.add(job.getStatus());
                                        }
                                        if (!list_worktypes.contains(job.getJtype())) {
                                            list_worktypes.add(job.getJtype());
                                        }
                                    }
                                    addStatusToSpinner(list_jobstatus);
                                    addTypesToSpinner(list_worktypes);

//                        l

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
//                    jobs_view.smoothScrollToPosition(jobs.size() - 1);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Toasty.info(getActivity(), "onSaveInstanceState").show();
//        outState.putInt("status", spinner_status.getSelectedItemPosition());
//        outState.putInt("type", spinner_type.getSelectedItemPosition());
//
//        outState.putString("date", date);
//
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//
//        Toasty.info(getActivity(), "onViewStateRestored").show();
//
//        if (savedInstanceState != null) {
//            Toasty.info(getActivity(), "IN IF").show();
//            spinner_status.setSelection(savedInstanceState.getInt("status"));
//            spinner_type.setSelection(savedInstanceState.getInt("type"));
//
//            date = savedInstanceState.getString("date");
//            btn_s_date.setText(date);
//
//        } else {
//            Toasty.info(getActivity(), "IN Else").show();
//
//        }
//
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//
//    }
}

class VAllJobsViewAdapter extends RecyclerView.Adapter {

    ArrayList<Job> jobs;
    Activity activity;
    User client;
    Map<String, Double> job_location;
    FirebaseFirestore db;
    Dialog_Lodaing dialogLodaing;
    Notification notification;
    DocumentReference document;

    public VAllJobsViewAdapter(ArrayList<Job> jobs, Activity activity) {
        this.jobs = jobs;
        this.activity = activity;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.job_request_view, viewGroup, false);
        VAllJobsViewHolder wjobsViewHolder = new VAllJobsViewHolder(view);
        return wjobsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

        final Job job = jobs.get(position);

        System.out.println("JOB ID ONE >>>>>>>>>>>>>>> : " + job.getJid());

        db.collection("users").document(job.getCid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            System.out.println("IF AWAAAAAAAAAA");
                            client = documentSnapshot.toObject(User.class);


                            final VAllJobsViewHolder jobRequestsViewHolder = (VAllJobsViewHolder) viewHolder;


                            if (job.getStatus().equals("1")) {
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);

                                jobRequestsViewHolder.tv_l_con_date.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_s_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_payment.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_jobh.setVisibility(View.GONE);

                                jobRequestsViewHolder.tv_con_date.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_s_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_payment.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobh.setVisibility(View.GONE);


                            } else if (job.getStatus().equals("2")) {
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.VISIBLE);

                                jobRequestsViewHolder.tv_con_date.setText(Validations.dateObjToString(job.getConfirmed_date(), "yyyy-MM-dd"));

                                jobRequestsViewHolder.tv_l_con_date.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_l_s_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_payment.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_jobh.setVisibility(View.GONE);


                                jobRequestsViewHolder.tv_con_date.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_s_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_payment.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobh.setVisibility(View.GONE);


                            } else if (job.getStatus().equals("3")) {
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);

                                jobRequestsViewHolder.tv_con_date.setText(Validations.dateObjToString(job.getConfirmed_date(), "yyyy-MM-dd"));

                                jobRequestsViewHolder.tv_l_con_date.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_l_s_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_payment.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_jobh.setVisibility(View.GONE);


                                jobRequestsViewHolder.tv_con_date.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_s_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_payment.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobh.setVisibility(View.GONE);


                            } else if (job.getStatus().equals("4")) {
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);

                                jobRequestsViewHolder.tv_con_date.setText(Validations.dateObjToString(job.getConfirmed_date(), "yyyy-MM-dd"));
                                jobRequestsViewHolder.tv_s_time.setText(Validations.dateObjToString(job.getStart_time(), "hh:mm a"));

                                jobRequestsViewHolder.tv_l_con_date.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_l_s_time.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_payment.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_jobh.setVisibility(View.GONE);

                                jobRequestsViewHolder.tv_con_date.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_s_time.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_payment.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobh.setVisibility(View.GONE);

                            } else if (job.getStatus().equals("5")) {
                                jobRequestsViewHolder.tv_jobongoing.setText("Job Finished");
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_jobongoing.setTextColor(ContextCompat.getColor(activity, R.color.color_green));
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);

                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                System.out.println(Validations.dateObjToString(job.getEnd_time(), "yyyy-MM-dd hh:mm a") + " : " + job.getEnd_time().getTime());
                                System.out.println(Validations.dateObjToString(job.getStart_time(), "yyyy-MM-dd hh:mm a") + " : " + job.getStart_time().getTime());
                                System.out.println(job.getEnd_time().getTime() - job.getStart_time().getTime() + "");
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                                jobRequestsViewHolder.tv_con_date.setText(Validations.dateObjToString(job.getConfirmed_date(), "yyyy-MM-dd"));
                                jobRequestsViewHolder.tv_s_time.setText(Validations.dateObjToString(job.getStart_time(), "hh:mm a"));
                                jobRequestsViewHolder.tv_e_time.setText(Validations.dateObjToString(job.getEnd_time(), "hh:mm a"));
                                jobRequestsViewHolder.tv_payment.setText(job.getPayment() + "");
                                jobRequestsViewHolder.tv_jobh.setText(((((job.getEnd_time().getTime() - job.getStart_time().getTime()) / 1000) / 60) / 60) + "h" + ((((job.getEnd_time().getTime() - job.getStart_time().getTime()) / 1000) / 60) % 60) + "m");

                                jobRequestsViewHolder.tv_l_con_date.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_l_s_time.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_l_e_time.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_l_payment.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_l_jobh.setVisibility(View.VISIBLE);

                                jobRequestsViewHolder.tv_con_date.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_s_time.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_e_time.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_payment.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.tv_jobh.setVisibility(View.VISIBLE);


                            } else if (job.getStatus().equals("6")) {
                                jobRequestsViewHolder.tv_jobongoing.setText("JOB DECLINED BY CLIENT");
                                jobRequestsViewHolder.tv_jobongoing.setTextColor(ContextCompat.getColor(activity, R.color.warning_red));
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);

                                jobRequestsViewHolder.tv_l_con_date.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_s_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_payment.setVisibility(View.GONE);

                                jobRequestsViewHolder.tv_con_date.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_s_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_payment.setVisibility(View.GONE);

                            } else if (job.getStatus().equals("7")) {
                                jobRequestsViewHolder.tv_jobongoing.setText("JOB DECLINED BY YOU");
                                jobRequestsViewHolder.tv_jobongoing.setTextColor(ContextCompat.getColor(activity, R.color.warning_red));
                                jobRequestsViewHolder.btn_dec_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_acc_job.setVisibility(View.GONE);
                                jobRequestsViewHolder.btn_stjob.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_jobongoing.setVisibility(View.VISIBLE);
                                jobRequestsViewHolder.btn_gotojob.setVisibility(View.GONE);

                                jobRequestsViewHolder.tv_l_con_date.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_s_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_l_payment.setVisibility(View.GONE);

                                jobRequestsViewHolder.tv_con_date.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_s_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_e_time.setVisibility(View.GONE);
                                jobRequestsViewHolder.tv_payment.setVisibility(View.GONE);

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
                                                    try {
                                                        Picasso.with(activity)
                                                                .load(client.getImageUrl())
                                                                .into(jobRequestsViewHolder.circleImageView_c_img);
                                                    } catch (Exception e) {
                                                        System.out.println("ERRORERRORERRORERRORERRORERRORERROR");
                                                    }
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
                                        jobRequestsViewHolder.viewmore.setText("Hide More Details");


                                    } else {
                                        jobRequestsViewHolder.moredetails.setVisibility(View.GONE);
                                        jobRequestsViewHolder.viewmore.setText("View More Details");

                                    }

                                }
                            });

                            jobRequestsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TransitionManager.beginDelayedTransition(jobRequestsViewHolder.moredetails);
                                    if (jobRequestsViewHolder.moredetails.getVisibility() == View.GONE) {
                                        jobRequestsViewHolder.moredetails.setVisibility(View.VISIBLE);
                                        jobRequestsViewHolder.viewmore.setText("Hide More Details");
                                    } else {
                                        jobRequestsViewHolder.moredetails.setVisibility(View.GONE);
                                        jobRequestsViewHolder.viewmore.setText("View More Details");
                                    }
//                                    Snackbar.make(v.getRootView().findViewById(R.id.worker_coor_lay), ((TextView) v.findViewById(R.id.tv_w_job_type)).getText().toString(), Snackbar.LENGTH_LONG).show();
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
                                        System.out.println("JOB ID >>>>>>>>>>>>>>> : " + job.getJid());

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

class VAllJobsViewHolder extends RecyclerView.ViewHolder {

    public TextView type, viewmore, tv_added_date, tv_req_date, tv_cname, tv_ccno, tv_job_id, tv_jobongoing, tv_l_con_date, tv_con_date, tv_l_s_time, tv_s_time, tv_l_e_time, tv_e_time, tv_l_payment, tv_payment, tv_l_jobh, tv_jobh;
    public CircleImageView circleImageView_c_img;
    Button btn_view_location, btn_dec_job, btn_acc_job, btn_callc, btn_gotojob, btn_stjob;
    public CardView cardView;
    public ConstraintLayout moredetails;
    public ConstraintLayout mainlay;

    public VAllJobsViewHolder(@NonNull View itemView) {
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
        tv_l_con_date = itemView.findViewById(R.id.tv_l_con_date);
        tv_con_date = itemView.findViewById(R.id.tv_con_date);
        tv_l_s_time = itemView.findViewById(R.id.tv_l_s_time);
        tv_s_time = itemView.findViewById(R.id.tv_s_time);
        tv_l_e_time = itemView.findViewById(R.id.tv_l_e_time);
        tv_e_time = itemView.findViewById(R.id.tv_e_time);
        tv_l_payment = itemView.findViewById(R.id.tv_l_payment);
        tv_payment = itemView.findViewById(R.id.tv_payment);
        tv_l_jobh = itemView.findViewById(R.id.tv_l_jobh);
        tv_jobh = itemView.findViewById(R.id.tv_jobh);

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
