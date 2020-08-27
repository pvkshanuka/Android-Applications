package com.app.wooker.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.app.wooker.ClientMain;
import com.app.wooker.DBClasses.Job;
import com.app.wooker.DBClasses.Notification;
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

public class NotificationsViewModel extends ViewModel {


    private Notification notification;
    private MutableLiveData<List<Notification>> mutableLiveData;
    public ListenerRegistration listenerRegistration;

    public MutableLiveData<List<Notification>> getNotifications() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            loadNotifications();
        }
        return mutableLiveData;
    }

    private void loadNotifications() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            System.out.println("listenerRegistration REMOVED");
        }

        System.out.println("WorkerMain Notofication >>>>>>>>>>>>>>>>>");
        if (WorkerMain.mUser == null && ClientMain.mUser != null) {
            listenerRegistration = FirebaseFirestore.getInstance().collection("notifications").whereEqualTo("to", ClientMain.mUser.getUid()).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                //        listenerRegistration = FirebaseFirestore.getInstance().collection("notifications").whereEqualTo("to", WorkerMain.mUser.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null) {
                        mutableLiveData.setValue(processNotofications(queryDocumentSnapshots));
                    } else if (e != null) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (WorkerMain.mUser != null && ClientMain.mUser == null) {
            listenerRegistration = FirebaseFirestore.getInstance().collection("notifications").whereEqualTo("to", WorkerMain.mUser.getUid()).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                //        listenerRegistration = FirebaseFirestore.getInstance().collection("notifications").whereEqualTo("to", WorkerMain.mUser.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null) {
                        mutableLiveData.setValue(processNotofications(queryDocumentSnapshots));
                    } else if (e != null) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private List<Notification> processNotofications(QuerySnapshot queryDocumentSnapshots) {
        List<Notification> notificationList = new LinkedList<>();

        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
            notification = documentSnapshot.toObject(Notification.class);
            System.out.println("NOTIFICATIONVIEWHOLDER >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> :" + notification.getTo() + " - " + notification.getData());
            notificationList.add(notification);
        }

        return notificationList;
    }

    public void startListener() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            loadNotifications();
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
