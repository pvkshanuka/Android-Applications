package com.app.wooker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

import static CustomClasses.RunOnUIThread.runOnUiThread;

public class Dialog_AC_Type_Select extends AppCompatDialogFragment {

    Button btn_clac, btn_woac;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_ac_type_select, null);

        builder.setView(view)
                .setTitle("Please Choose Account Type")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            logout();
                    }
                });

        btn_clac = view.findViewById(R.id.btn_client);
        btn_woac = view.findViewById(R.id.btn_worker);

//        fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();

        btn_clac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.fragmentController.setupFragment(new Frag_Signup_Client(),true);
                Dialog_AC_Type_Select.this.dismiss();

            }
        });

        btn_woac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentController.setupFragment(new Frag_Signup_Worker(),true);
                Dialog_AC_Type_Select.this.dismiss();
            }
        });

        return builder.create();
    }

    private void logout() {
        final Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("Processing.!", "Signing Out..");

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("facebook.com")) {

                        AccessToken.setCurrentAccessToken(null);
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();

                    } else if (FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("google.com")) {
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();
                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                        googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseAuth.getInstance().signOut();
                            }
                        });
                    }

                }
                FirebaseAuth.getInstance().signOut();


//            AuthUI.getInstance()
//                    .signOut(this)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//
    }

}
