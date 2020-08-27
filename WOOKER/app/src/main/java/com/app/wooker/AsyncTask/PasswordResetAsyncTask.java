package com.app.wooker.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.app.wooker.Dialog_Lodaing;
import com.app.wooker.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.SQLOutput;

import es.dmoral.toasty.Toasty;

public class PasswordResetAsyncTask extends AsyncTask {

    Dialog_Lodaing dialog_lodaing;
    String email;
    Activity activity;

    public PasswordResetAsyncTask( Activity activity,String email) {
        this.activity = activity;
        this.email = email;
    }

    @Override
    protected void onPreExecute() {
        dialog_lodaing = new Dialog_Lodaing("Password reset link is sending to your Email..!","Email Sending.!");
        MainActivity.fragmentController.setupDialog(dialog_lodaing);
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

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            System.out.println("Ok Success");
                            Toasty.success(activity,"Password reset link was sent to your Email.!").show();
                        }else{
                            Toasty.error(activity,"Email sending failed.!",Toasty.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(activity,e.getMessage(),Toasty.LENGTH_LONG).show();

            }
        });

        return null;
    }
}
