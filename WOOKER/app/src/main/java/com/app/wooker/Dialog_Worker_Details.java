package com.app.wooker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.wooker.AsyncTask.ManageJobAsyncTask;
import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.User;
import com.app.wooker.DBClasses.WorkerTypes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import CustomClasses.JobCategoryDetails;
import CustomClasses.RunOnUIThread;
import CustomClasses.Test;
import CustomClasses.Validations;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Dialog_Worker_Details extends AppCompatDialogFragment {

    String uid;
    String worker_type;
    String date;
    ArrayList<JobCategoryDetails> list_cat_job;
    //    ArrayList<Test> tests;
    RecyclerView ratings_view;
    TextView viewmore, tv_name, tv_cno, tv_cats, tv_gender, tv_or_rating, tv_job_count_w_p, tv_worked_hs;
    RatingBar ratingBar;
    ScrollView scrollView;
    CircleImageView circleImageView;
    Button btn_call, btn_viewweb, btn_send_jobreq;
    String weburl = null;
    Map<String, Double> job_location;

    Job job;
    double rating = 0.0;
    int job_count = 0;
    long worked_mills = 0;

    WorkerTypes workerTypes;

    public Dialog_Worker_Details() {
    }

    @SuppressLint("ValidFragment")
    public Dialog_Worker_Details(String uid, String worker_type, String date) {
        this.uid = uid;
        this.worker_type = worker_type;
        this.date = date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_worker_details, null);

        builder.setView(view)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        System.out.println(uid + "   <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");


        init(view);

        doProcess();

        return builder.create();
    }

    private void doProcess() {

        loadWorkerDetails();
//        tests = new ArrayList<>();


        btn_send_jobreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("jobs")
                        .whereEqualTo("wid", uid)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Toasty.success(getActivity(), "Check").show();
                                if (queryDocumentSnapshots.isEmpty()) {
                                    System.out.println("queryDocumentSnapshots EMPTYYYYYYYYY >>......>>>>>>>>>>>>>>>>>>>>>");
                                    job_location = new HashMap<>();
                                    job_location.put("latitude", DialogMap.location.latitude);
                                    job_location.put("longitude", DialogMap.location.longitude);
                                    Job job = new Job(
                                            uid
                                            , ClientMain.mUser.getUid()
                                            , worker_type
                                            , new Date()
                                            , Validations.stringDateToDateObj(date, "yyyy-MM-dd")
                                            , ClientMain.mUser.getGender()
                                            , job_location
                                            , "1");
                                    Dialog_Worker_Details.this.dismiss();
                                    Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("Request Sending..!");
                                    dialogLodaing.setCancelable(false);
                                    ClientMain.fragmentController.setupDialog(dialogLodaing);
                                    new ManageJobAsyncTask(getActivity(), "client", job, dialogLodaing).execute();
                                } else {
                                    System.out.println("queryDocumentSnapshots EMPTYYYYYYYYY NA >>......>>>>>>>>>>>>>>>>>>>>>");
                                    Job job2;
                                    boolean isFound = false;
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        job2 = documentSnapshot.toObject(Job.class);
                                        if (job2.getStatus().equals("5") || job2.getStatus().equals("6") || job2.getStatus().equals("7")) {
                                        } else {
                                            System.out.println(Validations.dateObjToString(job2.getJob_date(), "yyyy-MM-dd") + "-" + date);
                                            System.out.println("IF CONDITION - " + Validations.dateObjToString(job2.getJob_date(), "yyyy-MM-dd").equals(date));
                                            if (Validations.dateObjToString(job2.getJob_date(), "yyyy-MM-dd").equals(date)) {
                                                isFound = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (isFound) {
                                        Toasty.info(getActivity(), "Worker has another job on your job date.!").show();
                                    } else {
                                        job_location = new HashMap<>();
                                        job_location.put("latitude", DialogMap.location.latitude);
                                        job_location.put("longitude", DialogMap.location.longitude);
                                        Job job = new Job(
                                                uid
                                                , ClientMain.mUser.getUid()
                                                , worker_type
                                                , new Date()
                                                , Validations.stringDateToDateObj(date, "yyyy-MM-dd")
                                                , ClientMain.mUser.getGender()
                                                , job_location
                                                , "1");
                                        Dialog_Worker_Details.this.dismiss();
                                        Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("Request Sending..!");
                                        dialogLodaing.setCancelable(false);
                                        ClientMain.fragmentController.setupDialog(dialogLodaing);
                                        new ManageJobAsyncTask(getActivity(), "client", job, dialogLodaing).execute();
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(getActivity(), e.getMessage()).show();
                                e.printStackTrace();
                            }
                        });


            }
        });


        btn_viewweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weburl != null) {
                    Dialog_Worker_Details.this.dismiss();
                    ClientMain.fragmentController.setupDialog(new Dialog_Web_View(uid, worker_type, date, weburl));
                } else {
                    Toasty.info(getActivity(), "No website for this worker.!").show();
                }
            }
        });
//        tv_name.setText(WorkerMain.mUser.getFname() + " " + WorkerMain.mUser.getLname());
//        tv_cno.setText(WorkerMain.mUser.getCno());

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(uid + "     >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                FirebaseFirestore.getInstance().collection("worker_types")
                        .whereEqualTo("workerid", uid)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    tv_cats.setText("No Categories");
                                } else {

                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                        workerTypes = documentSnapshot.toObject(WorkerTypes.class);

                                        if (!(tv_cats.getText().equals(""))) {
                                            tv_cats.setText(tv_cats.getText() + ",");
                                        }
                                        tv_cats.setText(tv_cats.getText() + workerTypes.getType());
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(getActivity(), e.getMessage()).show();
                            }
                        });


                FirebaseFirestore.getInstance().collection("users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {

                                    User worker = documentSnapshot.toObject(User.class);

                                    tv_name.setText(worker.getFname() + " " + worker.getLname());
                                    tv_cno.setText(worker.getCno());
                                    tv_gender.setText(worker.getGender());
                                    weburl = worker.getWeburl();

                                    if (circleImageView != null) {
                                        System.out.println("AWA 1");
                                        if (!(worker.getImageUrl().equals(""))) {
                                            System.out.println("AWA 2");
                                            Picasso.with(getActivity())
                                                    .load(worker.getImageUrl())
                                                    .into(circleImageView);
                                        }
                                    }
                                    if (circleImageView.getDrawable() == null) {
                                        circleImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.img_user_image));
                                    }

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(getActivity(), e.getMessage());
                            }
                        });

            }
        }).start();

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


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        ratings_view.setLayoutManager(linearLayoutManager);
//
//        WorkerRatingsViewAdapterWD workerRatingsViewAdapterwd = new WorkerRatingsViewAdapterWD(tests);
//        ratings_view.setAdapter(workerRatingsViewAdapterwd);

        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratings_view.getVisibility() == View.GONE) {
                    viewmore.setText("Hide More Ratings");
                    ratings_view.setVisibility(View.VISIBLE);
//                    scrollView.smoothScrollTo(0, 0);
                } else {
                    viewmore.setText("View More Ratings");
                    ratings_view.setVisibility(View.GONE);
//                    scrollView.smoothScrollTo(0, 0);
                }
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

    }

    private void loadWorkerDetails() {

        FirebaseFirestore.getInstance().collection("jobs")
                .whereEqualTo("wid", uid)
                .whereEqualTo("status", "5").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                System.out.println("In Thread Run................................>>>>");
                                if (queryDocumentSnapshots.isEmpty()) {
                                    RunOnUIThread.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            viewmore.setVisibility(View.GONE);
                                        }
                                    });
                                } else {

                                    JobCategoryDetails categoryDetails;
                                    System.out.println("before loop................................>>>>");

                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        System.out.println("in loop................................>>>>");

                                        job = documentSnapshot.toObject(Job.class);

                                        if (list_cat_job == null) {
                                            System.out.println("list null................................>>>>");

                                            list_cat_job = new ArrayList<>();
                                            categoryDetails = new JobCategoryDetails(job.getJtype(), new ArrayList<Job>());
                                            categoryDetails.getList_jobs().add(job);

                                            list_cat_job.add(categoryDetails);
                                            System.out.println("cat job added................................>>>>");

                                        } else {
                                            System.out.println("list null NA................................>>>>");

                                            boolean found = false;
                                            System.out.println("list_cat_job Size >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " + list_cat_job.size());
                                            for (int counter = 0; counter < list_cat_job.size(); counter++) {
                                                System.out.println("Round >>>>>>>>>>>>>>>>>>> " + counter);

                                                if (list_cat_job.get(counter).getCat_name().equals(job.getJtype())) {
                                                    found = true;
                                                    System.out.println("in if cat job eq................................>>>>");

                                                    categoryDetails = list_cat_job.get(counter);

                                                    categoryDetails.getList_jobs().add(job);

                                                    list_cat_job.set(counter, categoryDetails);

                                                    System.out.println("in if cat job added................................>>>>");
                                                    break;

                                                }

                                            }

                                            if (!found) {
                                                System.out.println("in if found cat job eq................................>>>>");

                                                categoryDetails = new JobCategoryDetails(job.getJtype(), new ArrayList<Job>());
                                                categoryDetails.getList_jobs().add(job);
                                                list_cat_job.add(categoryDetails);

                                                System.out.println("in if found cat job added................................>>>>");


                                            }


                                        }

                                        job_count++;
                                        rating += job.getRating();
                                        worked_mills += job.getEnd_time().getTime() - job.getStart_time().getTime();

                                    }

                                    for (JobCategoryDetails details : list_cat_job) {
                                        System.out.println("Details >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + details.getCat_name());
                                        System.out.println("Details >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + details.getList_jobs().size());

                                        for (Job job : details.getList_jobs()) {
                                            System.out.println("Details List >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + job.getJid());
                                            System.out.println("Details List >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + job.getJtype());
                                        }

                                    }


                                    RunOnUIThread.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                                            ratings_view.setLayoutManager(linearLayoutManager);

                                            WorkerRatingsViewAdapter workerRatingsViewAdapter = new WorkerRatingsViewAdapter(list_cat_job);
                                            ratings_view.setAdapter(workerRatingsViewAdapter);

                                            viewmore.setVisibility(View.VISIBLE);
                                            tv_or_rating.setText(String.format("%.2f", (rating / job_count)));
                                            ratingBar.setRating(Float.parseFloat((rating / job_count) + ""));
                                            tv_job_count_w_p.setText(job_count + "");
                                            tv_worked_hs.setText(((((worked_mills) / 1000) / 60) / 60) + "h" + ((((worked_mills) / 1000) / 60) % 60) + "m");
                                        }
                                    });

                                }
                            }
                        }).start();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getActivity(), e.getMessage()).show();
                        e.printStackTrace();
                    }
                });

    }

    private void makeCall() {
        if (Validations.conValidation(tv_cno.getText().toString())) {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MainActivity.CALL_PERMISSION_CODE);

            } else {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_cno.getText().toString())));
            }

        } else {
            Toasty.error(getActivity(), "Invalid number.!").show();
        }
    }

    private void init(final View view) {

        ratings_view = view.findViewById(R.id.rv_worker_rating_detailswd);
        viewmore = view.findViewById(R.id.tv_view_more_ratingswd);
        tv_name = view.findViewById(R.id.tv_ps_w_namewd);
        tv_cno = view.findViewById(R.id.tv_ps_w_cnowd);
        tv_cats = view.findViewById(R.id.tv_ps_w_catswd);
//        scrollView = view.findViewById(R.id.scrollViewwd);
        circleImageView = view.findViewById(R.id.circleImageViewwdc);
        btn_call = view.findViewById(R.id.btn_callwd);
        btn_viewweb = view.findViewById(R.id.btn_view_web);
        tv_gender = view.findViewById(R.id.tv_gender);
        btn_send_jobreq = view.findViewById(R.id.btn_send_jobreq);

        tv_or_rating = view.findViewById(R.id.tv_or_rating_wd_d);
        tv_job_count_w_p = view.findViewById(R.id.tv_job_count_wd_d);
        tv_worked_hs = view.findViewById(R.id.tv_worked_hs_wd_d);

        ratingBar = view.findViewById(R.id.ratingBarwd);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//
//                RunOnUIThread.runOnUiThread(new Runnable() {
//                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
//                    CircleImageView circleImageView = navigationView.getHeaderView(0).findViewById(R.id.circleImageViewheaderw);
//                    CircleImageView circleImageView2 = view.findViewById(R.id.circleImageViewpw);
//
//                    @Override
//                    public void run() {
//
//                        circleImageView2.setImageDrawable(circleImageView.getDrawable());
//                    }
//                });
//
//            }
//        }).start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MainActivity.CALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toasty.error(getActivity(), "Permission denied").show();
            }
        }

    }
}

class WorkerRatingsViewAdapterWD extends RecyclerView.Adapter {

    ArrayList<Test> tests;

    public WorkerRatingsViewAdapterWD(ArrayList<Test> tests) {
        this.tests = tests;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.worker_rating_details, viewGroup, false);
        WorkerRatingsViewHolderWD workerRatingsViewHolderwd = new WorkerRatingsViewHolderWD(view);
        return workerRatingsViewHolderwd;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final Test test = tests.get(i);

        final WorkerRatingsViewHolderWD workerRatingsViewHolderwd = (WorkerRatingsViewHolderWD) viewHolder;

        workerRatingsViewHolderwd.category.setText(test.getType());

    }

    @Override
    public int getItemCount() {
        return tests.size();
    }
}

class WorkerRatingsViewHolderWD extends RecyclerView.ViewHolder {

    public TextView category;
    public TextView viewmore;

    public WorkerRatingsViewHolderWD(@NonNull View itemView) {
        super(itemView);

        category = itemView.findViewById(R.id.tv_job_cat_det);


    }


}
