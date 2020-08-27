package com.app.wooker;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.wooker.DBClasses.Notification;
import com.app.wooker.ViewModels.NotificationsViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import CustomClasses.RVNotificationAdapter;
import CustomClasses.RecyclerItemTouchHelper;
import CustomClasses.RunOnUIThread;
import es.dmoral.toasty.Toasty;

public class Frag_Notifications extends Fragment implements
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public Frag_Notifications() {
    }

    @SuppressLint("ValidFragment")
    public Frag_Notifications(String uid) {
        this.uid = uid;
    }

    String uid;
    RecyclerView notification_view;
    LinearLayoutManager linearLayoutManager;
    RVNotificationAdapter notificationViewAdapter;
    NotificationsViewModel notificationsViewModel;
    List<Notification> notificationsf;
    boolean b = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_notifications, container, false);
        setupNotificationListener(view);
        return view;
    }

    private void setupNotificationListener(final View view) {

        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        notificationsViewModel.startListener();
        notificationsViewModel.getNotifications().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(@Nullable List<Notification> notifications) {
                notificationsf = notifications;

                for (Notification notification : notifications) {
                    if (notification.getStatus().equals("5")) {
                        notificationsf.remove(notification);
                    }
                }
                init(view, notificationsf);
            }
        });
    }

    private void init(View view, List<Notification> notifications) {
        Toasty.info(getActivity(), uid);
        notification_view = view.findViewById(R.id.rv_client_notifications);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        notificationViewAdapter = new RVNotificationAdapter(getActivity(), notifications);
        notification_view.setLayoutManager(linearLayoutManager);
        notification_view.setAdapter(notificationViewAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, Frag_Notifications.this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(notification_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RVNotificationAdapter.DataObjectHolder) {
            String type = notificationViewAdapter.getNortificationList().get(viewHolder.getAdapterPosition()).getMessage();
            final Notification notification = notificationViewAdapter.getNortificationList().get(viewHolder.getAdapterPosition());
            final int deletedIntex = viewHolder.getAdapterPosition();
            notificationViewAdapter.removeItem(viewHolder.getAdapterPosition());
            recoverNotificationDelete(viewHolder, type, notification, deletedIntex);
        }
    }

    private void recoverNotificationDelete(RecyclerView.ViewHolder viewHolder, String type, final Notification notification, final int deletedIntex) {
        Snackbar snackbar = Snackbar.make(((RVNotificationAdapter.DataObjectHolder) viewHolder).notification_content,
                type + " Cleared", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationViewAdapter.restoreItem(notification, deletedIntex);
                b = false;
            }
        });

        snackbar.setActionTextColor(Color.GREEN);
        snackbar.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (b) {
                    notification.setStatus("5");
                    FirebaseFirestore.getInstance().collection("notifications").document(notification.getNotid()).set(notification)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull final Exception e) {
                                    RunOnUIThread.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toasty.error(getActivity(), e.getMessage()).show();
                                        }
                                    });
                                }
                            });
                }
            }
        }, 6000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (notificationsf != null) {

            for (Notification notification : notificationsf) {
                if (notification.getStatus().equals("2")) {
                    notification.setStatus("3");
                    FirebaseFirestore.getInstance().collection("notifications").document(notification.getNotid()).set(notification)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull final Exception e) {
                                    RunOnUIThread.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toasty.error(getActivity(), e.getMessage()).show();
                                        }
                                    });
                                }
                            });
                }
            }

        }
    }
}
