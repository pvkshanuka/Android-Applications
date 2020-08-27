package com.app.wooker.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.app.wooker.ClientMain;
import com.app.wooker.DBClasses.Job;
import com.app.wooker.MainActivity;
import com.app.wooker.WorkerMain;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

public class JobsViewModel extends ViewModel {

    private MutableLiveData<List<Job>> mutableLiveData;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();
    public ListenerRegistration listenerRegistration;

    public MutableLiveData<List<Job>> getJobs() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            loadJobs();
        }
        return mutableLiveData;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }

    private void loadJobs() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            System.out.println("listenerRegistration REMOVED");
        }

        if (WorkerMain.mUser != null) {
            System.out.println("WorkerMain");
            listenerRegistration = FirebaseFirestore.getInstance().collection("jobs").whereEqualTo("wid", WorkerMain.mUser.getUid()).orderBy("job_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    mIsUpdating.setValue(true);
                    if (queryDocumentSnapshots != null) {
                        mutableLiveData.setValue(processJobs(queryDocumentSnapshots));
                    } else if (e != null) {
                        e.printStackTrace();
                    }
                    mIsUpdating.postValue(false);
                }
            });
        } else if (ClientMain.mUser != null) {
            System.out.println("ClientMain");
            listenerRegistration = FirebaseFirestore.getInstance().collection("jobs").whereEqualTo("cid", ClientMain.mUser.getUid()).orderBy("job_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    mIsUpdating.setValue(true);
                    if (queryDocumentSnapshots != null) {
                        mutableLiveData.setValue(processJobs(queryDocumentSnapshots));
                    } else if (e != null) {
                        e.printStackTrace();
                    }
                    mIsUpdating.postValue(false);
                }
            });
        }

    }

    private List<Job> processJobs(QuerySnapshot queryDocumentSnapshots) {
        List<Job> jobList = new LinkedList<>();

        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
            Job job = documentSnapshot.toObject(Job.class);
            System.out.println("JOBSVIEWHOLDER >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> :"+job.getCid()+" - "+job.getWid());
            jobList.add(job);
        }

        return jobList;
    }

    public void startListener() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            loadJobs();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}
