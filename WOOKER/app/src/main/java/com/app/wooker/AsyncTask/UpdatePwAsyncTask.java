package com.app.wooker.AsyncTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.app.wooker.ClientMain;
import com.app.wooker.Dialog_Lodaing;
import com.app.wooker.MainActivity;
import com.app.wooker.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class UpdatePwAsyncTask extends AsyncTask {


    Activity activity;
    String email;
    Dialog_Lodaing dialog_lodaing;

    public UpdatePwAsyncTask(Activity activity, String email, Dialog_Lodaing dialog_lodaing) {
        this.activity = activity;
        this.email = email;
        this.dialog_lodaing = dialog_lodaing;
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
                            Toasty.success(activity,"Password Change link was sent to your Email.!",Toasty.LENGTH_LONG).show();

                            if (FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("facebook.com")) {

                                AccessToken.setCurrentAccessToken(null);
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();

                            } else if (FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("google.com")) {
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(activity.getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();
                                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, gso);
                                googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                });
                            }else{
                                FirebaseAuth.getInstance().signOut();
                            }


//            AuthUI.getInstance()
//                    .signOut(this)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.finish();
                            Toasty.success(activity, "Sign Out Successfuly.!");

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
