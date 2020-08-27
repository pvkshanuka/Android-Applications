package com.app.wooker;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.User;
import com.app.wooker.DBClasses.WorkCats;
import com.app.wooker.DBClasses.WorkerTypes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

import CustomClasses.JobCategoryDetails;
import CustomClasses.RunOnUIThread;
import CustomClasses.Worker_List_Item;
import es.dmoral.toasty.Toasty;

public class Dialog_List_View extends AppCompatDialogFragment {


    String worker_type;
    String date;

    RecyclerView recyclerView;
    ProgressBar progressBar;

    ArrayList<Worker_List_Item> list_worker_list_item;
    ArrayList<Worker_List_Item> mlist_worker;
    ArrayList<Worker_List_Item> flist_worker;
    //    Worker_List_Item worker_list_item;
    WorkerListViewAdaptor workerListViewAdaptor;
    boolean isSuccess = false;
    User worker;
    Job job;
    int job_count = 0;
    double rating = 0.0;

    public Dialog_List_View() {

    }

    @SuppressLint("ValidFragment")
    public Dialog_List_View(String worker_type, String date) {
        this.worker_type = worker_type;
        this.date = date;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_list_view, null);

        builder.setView(view)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        init(view);

        doProcess();

        return builder.create();

    }

    private void init(View view) {

        recyclerView = view.findViewById(R.id.rv_list_view);
        progressBar = view.findViewById(R.id.progressBar_lv);
        list_worker_list_item = new ArrayList<>();
        mlist_worker = new ArrayList<>();
        flist_worker = new ArrayList<>();

    }

    private void doProcess() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaa >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1");


                RunOnUIThread.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        System.out.println("Size of list_worker_list_item >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + list_worker_list_item.size());
                        workerListViewAdaptor = new WorkerListViewAdaptor(list_worker_list_item, worker_type, date);
                        recyclerView.setAdapter(workerListViewAdaptor);


//

                    }
                });

                FirebaseFirestore.getInstance().collection("worker_types")
                        .whereEqualTo("type", worker_type)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaa >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 2");

                                isSuccess = true;

                                if (!queryDocumentSnapshots.isEmpty()) {
                                    System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaa >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 3");
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                        FirebaseFirestore.getInstance().collection("users").document(documentSnapshot.toObject(WorkerTypes.class).getWorkerid()).get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaa >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 4");
                                                            worker = documentSnapshot.toObject(User.class);
                                                            loadWorkerDetails(worker);
                                                            System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaa >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 5 " + worker.getUid());
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
                                    System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaa >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 6");

                                    recyclerView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                isSuccess = false;
                                System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaa >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 7");

                                Toasty.error(getActivity(), e.getMessage()).show();
                            }
                        });
                System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaa >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 8");
                System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaa >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 9");

                System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaa >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 10");

            }
        }).start();
    }

    private void loadWorkerDetails(final User worker) {
        System.out.println("loadWorkerDetails - Awaaaaaaaaaaaaaaaaaa >>>>>>>>>>>> 1 ");
        FirebaseFirestore.getInstance().collection("jobs")
                .whereEqualTo("wid", worker.getUid())
                .whereEqualTo("status", "5")
                .whereEqualTo("jtype", worker_type)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                        System.out.println("loadWorkerDetails - Awaaaaaaaaaaaaaaaaaa >>>>>>>>>>>> 2 ");


                        if (!queryDocumentSnapshots.isEmpty()) {
                            System.out.println("loadWorkerDetails - Awaaaaaaaaaaaaaaaaaa >>>>>>>>>>>> 3 ");

                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                System.out.println("loadWorkerDetails - Awaaaaaaaaaaaaaaaaaa >>>>>>>>>>>> 4 ");

                                job_count++;
                                rating += documentSnapshot.toObject(Job.class).getRating();


                            }
                            System.out.println("loadWorkerDetails - Awaaaaaaaaaaaaaaaaaa >>>>>>>>>>>> 5 ");

                        }
                        System.out.println("loadWorkerDetails - Awaaaaaaaaaaaaaaaaaa >>>>>>>>>>>> 6 ");

                        list_worker_list_item.add(new Worker_List_Item(worker, job_count, rating));
                        notifyAdapter();

                        job_count = 0;
                        rating = 0.0;
                        System.out.println("loadWorkerDetails - Awaaaaaaaaaaaaaaaaaa list_worker_list_item >>>>>>>>>>>> 7 " + list_worker_list_item.size() + "-" + workerListViewAdaptor.getItemCount());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("loadWorkerDetails - Awaaaaaaaaaaaaaaaaaa >>>>>>>>>>>> 8 ");

                        Toasty.error(getActivity(), e.getMessage()).show();
                        e.printStackTrace();
                    }
                });

    }

    private void notifyAdapter() {
        Collections.sort(list_worker_list_item);
//        if (ClientMain.mUser.getGender().equals("Female")) {
//
//            progressBar.setVisibility(View.VISIBLE);
//            for (Worker_List_Item worker_list_item : list_worker_list_item) {
//                if (worker_list_item.getWorker().getGender().equals("Male")) {
//                    mlist_worker.add(worker_list_item);
//                } else {
//                    flist_worker.add(worker_list_item);
//                }
//            }
//            progressBar.setVisibility(View.GONE);
//
//            list_worker_list_item.removeAll(list_worker_list_item);
//            System.out.println("LIST SIZE LIST SIZE LIST SIZE LIST SIZE LIST SIZE LIST SIZE >>>>>>>> : " + list_worker_list_item.size());
//            list_worker_list_item.addAll(mlist_worker);
//            list_worker_list_item.addAll(flist_worker);
//            System.out.println("LIST SIZE LIST SIZE LIST SIZE LIST SIZE LIST SIZE LIST SIZE >>>>>>>> : " + list_worker_list_item.size());
//        }
        workerListViewAdaptor.notifyDataSetChanged();
    }

}

class WorkerListViewAdaptor extends RecyclerView.Adapter {

    ArrayList<Worker_List_Item> worker_list_items;
    WorkerListViewHolder workerListViewHolder;
    Worker_List_Item worker_list_item;
    String worker_type;
    String date;

    public WorkerListViewAdaptor(ArrayList<Worker_List_Item> worker_list_items, String worker_type, String date) {
        this.worker_list_items = worker_list_items;
        this.worker_type = worker_type;
        this.date = date;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.worker_view_list, viewGroup, false);
        WorkerListViewHolder workerListViewHolder = new WorkerListViewHolder(view);
        return workerListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        workerListViewHolder = (WorkerListViewHolder) viewHolder;
        worker_list_item = worker_list_items.get(i);
        System.out.println("onBindViewHolder >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DATA :" + worker_list_item.getWorker().getFname() + "-" + worker_list_item.getJob_count() + "-" + worker_list_item.getRating() + "-" + worker_list_item.getRating() / worker_list_item.getJob_count());

        workerListViewHolder.tv_name.setText(worker_list_item.getWorker().getFname() + " " + worker_list_item.getWorker().getLname());
        workerListViewHolder.tv_gen.setText(worker_list_item.getWorker().getGender());
        workerListViewHolder.tv_city.setText(worker_list_item.getWorker().getCity());
        workerListViewHolder.uid = worker_list_item.getWorker().getUid();
        workerListViewHolder.worker_type = worker_type;
        workerListViewHolder.date =date;

        if (worker_list_item.getJob_count() == 0) {
            workerListViewHolder.tv_rating.setText("0.0");
            workerListViewHolder.ratingBar.setRating(0.0f);
        } else {
            workerListViewHolder.tv_rating.setText((worker_list_item.getRating() / worker_list_item.getJob_count()) + "");
            workerListViewHolder.ratingBar.setRating(Float.parseFloat(String.valueOf(worker_list_item.getRating() / worker_list_item.getJob_count())));
        }


        System.out.println("WORKER ID DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD : "+worker_list_item.getWorker().getFname()+" - "+worker_list_item.getWorker().getUid());

//        workerListViewHolder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("WORKER ID BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB : "+worker_list_item.getWorker().getFname()+" - "+worker_list_item.getWorker().getUid());
//                System.out.println(workerListViewHolder.uid);
//                ClientMain.fragmentController.setupDialog(new Dialog_Worker_Details(workerListViewHolder.uid, worker_type, date));
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return worker_list_items.size();
    }
}

class WorkerListViewHolder extends RecyclerView.ViewHolder {

    TextView tv_name, tv_rating, tv_city, tv_gen;
    RatingBar ratingBar;
    ConstraintLayout layout;
    String uid,worker_type,date;

    public WorkerListViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_name = itemView.findViewById(R.id.tv_lv_wname);
        tv_gen = itemView.findViewById(R.id.tv_lv_gen);
        tv_city = itemView.findViewById(R.id.tv_lv_wcity);
        tv_rating = itemView.findViewById(R.id.tv_lv_wrate);

        ratingBar = itemView.findViewById(R.id.ratingBarlv);

        layout = itemView.findViewById(R.id.conslay_worker_list);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("WORKER ID BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB : "+uid);
                System.out.println(uid);
                ClientMain.fragmentController.setupDialog(new Dialog_Worker_Details(uid, worker_type, date));

            }
        });

    }


}

