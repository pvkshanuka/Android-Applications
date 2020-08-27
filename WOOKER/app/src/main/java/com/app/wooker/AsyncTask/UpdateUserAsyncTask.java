package com.app.wooker.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import com.app.wooker.ClientMain;
import com.app.wooker.DBClasses.User;
import com.app.wooker.Dialog_Lodaing;
import com.app.wooker.R;
import com.app.wooker.WorkerMain;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class UpdateUserAsyncTask extends AsyncTask {

    Activity activity;
    String from;
    Dialog_Lodaing dialog_lodaing;
    User user;

    public UpdateUserAsyncTask(Activity activity, String from, Dialog_Lodaing dialog_lodaing, User user) {
        this.activity = activity;
        this.from = from;
        this.dialog_lodaing = dialog_lodaing;
        this.user = user;
    }

    @Override
    protected void onPostExecute(Object o) {
        dialog_lodaing.dismiss();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (from.equals("client")) {
                            ClientMain.setUserData((NavigationView) activity.findViewById(R.id.nav_view));
                        }else if (from.equals("worker")){
                            WorkerMain.setUserData((NavigationView) activity.findViewById(R.id.nav_view));
                        }
                        Toasty.success(activity,"Details updated successfully.!").show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(activity,e.getMessage()).show();
                    }
                });

        return null;
    }
}
