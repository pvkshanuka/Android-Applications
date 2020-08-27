package com.app.wooker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.wooker.DBClasses.Job;

import java.util.ArrayList;

import CustomClasses.Test;
import CustomClasses.Validations;

public class Dialog_Job_History extends AppCompatDialogFragment {

    ArrayList<Job> jobs;
    RecyclerView recyclerView;
    TextView tv_j_h_title;

    public Dialog_Job_History() {

    }

    @SuppressLint("ValidFragment")
    public Dialog_Job_History(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_job_history, null);

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

    private void doProcess() {

        if (jobs.isEmpty()) {
            tv_j_h_title.setText("No jobs");
        } else {
            tv_j_h_title.setText(jobs.get(0).getJtype() + " job history");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        JobHistoryViewAdapter jobHistoryViewAdapter = new JobHistoryViewAdapter(jobs);
        recyclerView.setAdapter(jobHistoryViewAdapter);


    }


    private void init(View view) {
        recyclerView = view.findViewById(R.id.rv_j_h_details);
        tv_j_h_title = view.findViewById(R.id.tv_j_h_title);
    }
}


class JobHistoryViewAdapter extends RecyclerView.Adapter {

    ArrayList<Job> jobs;

    public JobHistoryViewAdapter(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.job_history_view, viewGroup, false);
        JobHistoryViewHolder jobHistoryViewHolder = new JobHistoryViewHolder(view);
        return jobHistoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final Job job = jobs.get(i);

        final JobHistoryViewHolder jobHistoryViewHolder = (JobHistoryViewHolder) viewHolder;

        jobHistoryViewHolder.tv_added_date.setText(Validations.dateObjToString(job.getJob_date(), "yyyy-MM-dd"));
        jobHistoryViewHolder.tv_worked_h.setText(((((job.getEnd_time().getTime() - job.getStart_time().getTime()) / 1000) / 60) / 60) + "h" + ((((job.getEnd_time().getTime() - job.getStart_time().getTime()) / 1000) / 60) % 60) + "m");
        jobHistoryViewHolder.tv_payment.setText(job.getPayment() + "");

    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
}

class JobHistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_added_date, tv_worked_h, tv_payment;

    public JobHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_added_date = itemView.findViewById(R.id.tv_j_h_added_date);
        tv_worked_h = itemView.findViewById(R.id.tv_j_h_worked_h);
        tv_payment = itemView.findViewById(R.id.tv_j_h_payment);


    }


}