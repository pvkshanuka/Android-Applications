package com.app.wooker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.WorkCats;
import com.app.wooker.DBClasses.WorkerTypes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import CustomClasses.JobCategoryDetails;
import CustomClasses.RunOnUIThread;
import CustomClasses.Test;
import CustomClasses.Validations;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Frag_Worker_Profile extends Fragment {

    //    ArrayList<Test> tests;
    ArrayList<JobCategoryDetails> list_cat_job;
    RecyclerView ratings_view;
    RatingBar ratingBar;
    TextView viewmore, tv_name, tv_cno, tv_cats, tv_or_rating, tv_job_count_w_p, tv_worked_hs;
    ScrollView scrollView;
    Job job;
    double rating = 0.0;
    int job_count = 0;
    long worked_mills = 0;

    WorkerTypes workerTypes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.worker_profile, container, false);

        init(view);


        return view;
    }

    private void init(final View view) {
        ratings_view = view.findViewById(R.id.rv_worker_rating_details);
        viewmore = view.findViewById(R.id.tv_view_more_ratings);
        tv_name = view.findViewById(R.id.tv_ps_w_name);
        tv_cno = view.findViewById(R.id.tv_ps_w_cno);
        tv_cats = view.findViewById(R.id.tv_ps_w_cats);
        scrollView = view.findViewById(R.id.scrollView3);
        tv_or_rating = view.findViewById(R.id.tv_or_rating);
        tv_job_count_w_p = view.findViewById(R.id.tv_job_count_w_p12);
        tv_worked_hs = view.findViewById(R.id.tv_worked_hs);

        ratingBar = view.findViewById(R.id.ratingBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                RunOnUIThread.runOnUiThread(new Runnable() {
                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                    CircleImageView circleImageView = navigationView.getHeaderView(0).findViewById(R.id.circleImageViewheaderw);
                    CircleImageView circleImageView2 = view.findViewById(R.id.circleImageViewpw);

                    @Override
                    public void run() {

                        circleImageView2.setImageDrawable(circleImageView.getDrawable());
                    }
                });

            }
        }).start();


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        tests = new ArrayList<>();

        loadWorkerDetails();

        tv_name.setText(WorkerMain.mUser.getFname() + " " + WorkerMain.mUser.getLname());
        tv_cno.setText(WorkerMain.mUser.getCno());

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(WorkerMain.firebaseUser.getUid() + "     >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                FirebaseFirestore.getInstance().collection("worker_types")
                        .whereEqualTo("workerid", WorkerMain.firebaseUser.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                if (queryDocumentSnapshots.isEmpty()) {

                                    tv_cats.setText("No Category's");
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


        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratings_view.getVisibility() == View.GONE) {
                    viewmore.setText("Hide More Ratings");
                    ratings_view.setVisibility(View.VISIBLE);
                    scrollView.smoothScrollTo(0, 0);
                } else {
                    viewmore.setText("View More Ratings");
                    ratings_view.setVisibility(View.GONE);
                    scrollView.smoothScrollTo(0, 0);
                }
            }
        });

    }

    private void loadWorkerDetails() {

        FirebaseFirestore.getInstance().collection("jobs")
                .whereEqualTo("wid", WorkerMain.mUser.getUid())
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

}

class WorkerRatingsViewAdapter extends RecyclerView.Adapter {

    ArrayList<JobCategoryDetails> list_cat_job;
    double rating;
    int job_count;
    long worked_mills;

    public WorkerRatingsViewAdapter(ArrayList<JobCategoryDetails> list_cat_job) {
        this.list_cat_job = list_cat_job;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.worker_rating_details, viewGroup, false);
        WorkerRatingsViewHolder workerRatingsViewHolder = new WorkerRatingsViewHolder(view);
        return workerRatingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final JobCategoryDetails details = list_cat_job.get(i);

        final WorkerRatingsViewHolder workerRatingsViewHolder = (WorkerRatingsViewHolder) viewHolder;


        workerRatingsViewHolder.tv_category.setText(details.getCat_name());

        job_count = 0;
        rating = 0.0;
        worked_mills = 0;

        for (Job job : details.getList_jobs()) {

            job_count++;
            rating += job.getRating();
            worked_mills += job.getEnd_time().getTime() - job.getStart_time().getTime();

        }

        workerRatingsViewHolder.tv_o_rating.setText(String.format("%.2f", (rating / job_count)));
        workerRatingsViewHolder.ratingBar.setRating(Float.parseFloat((rating / job_count) + ""));
        workerRatingsViewHolder.tv_j_count.setText(job_count + "");
        workerRatingsViewHolder.tv_w_hrs.setText(((((worked_mills) / 1000) / 60) / 60) + "h" + ((((worked_mills) / 1000) / 60) % 60) + "m");

        workerRatingsViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_Job_History dialog_job_history = new Dialog_Job_History(details.getList_jobs());

                if (WorkerMain.mUser != null && ClientMain.mUser == null) {
                    WorkerMain.fragmentController.setupDialog(dialog_job_history);
                } else if (WorkerMain.mUser == null && ClientMain.mUser != null) {
                    ClientMain.fragmentController.setupDialog(dialog_job_history);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list_cat_job.size();
    }
}

class WorkerRatingsViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_category, tv_j_count, tv_w_hrs, tv_o_rating;
    RatingBar ratingBar;
    ConstraintLayout layout;

    public WorkerRatingsViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_category = itemView.findViewById(R.id.tv_job_cat_det);
        tv_j_count = itemView.findViewById(R.id.tv_job_count_det);
        tv_w_hrs = itemView.findViewById(R.id.tv_worked_hs_det);
        tv_o_rating = itemView.findViewById(R.id.tv_or_rating_det);
        ratingBar = itemView.findViewById(R.id.ratingBar_det);
        layout = itemView.findViewById(R.id.cons_lay_rat_details);

    }


}
