package com.app.wooker.AsyncTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.app.wooker.ClientMain;
import com.app.wooker.DBClasses.User;
import com.app.wooker.Dialog_AC_Type_Select;
import com.app.wooker.Dialog_Lodaing;
import com.app.wooker.Frag_User_Login;
import com.app.wooker.MainActivity;
import com.app.wooker.WorkerMain;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class FbGoogleSignInAsyncTask extends AsyncTask {

    Activity activity;
    AuthCredential credential;
    Dialog_Lodaing dialog_lodaing;

    public FbGoogleSignInAsyncTask(Activity activity, AuthCredential credential, Dialog_Lodaing dialog_lodaing) {
        this.activity = activity;
        this.credential = credential;
        this.dialog_lodaing = dialog_lodaing;
    }

    @Override
    protected void onPreExecute() {
        System.out.println("Preeeeeeeeeeeeeeeeeeee");

    }

    @Override
    protected void onPostExecute(Object o) {
        System.out.println("postttttttttttttttttttttttttttt");
        dialog_lodaing.dismiss();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        System.out.println("Middleeeeeeeeeeeeeeeeeeeeeeeee");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = authResult.getUser();

                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {

                                    User user = documentSnapshot.toObject(User.class);

                                    if (user.getType().equals("1")) {
                                        WorkerMain.mUser = user;
                                        Intent intent = new Intent(activity, WorkerMain.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        activity.startActivity(intent);
                                        activity.finish();
                                    } else if (user.getType().equals("2")) {
                                        ClientMain.mUser = user;
                                        Intent intent = new Intent(activity, ClientMain.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        activity.startActivity(intent);
                                        activity.finish();
                                    } else {
                                        Toasty.error(activity, "Signing In Failed.! Please Sign In Againn.").show();
                                        Frag_User_Login fragLogin = new Frag_User_Login();

                                        MainActivity.fragmentController.setupFragment(fragLogin, false);
                                    }

                                } else {
                                    MainActivity.fragmentController.setupDialog(new Dialog_AC_Type_Select());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(activity, "Signing In Failed.! " + e.getMessage()).show();
                                Frag_User_Login fragLogin = new Frag_User_Login();
                                MainActivity.fragmentController.setupFragment(fragLogin, false);
                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toasty.error(activity, "Login failed", Toast.LENGTH_SHORT, true).show();
            }
        });

        return null;
    }
}
