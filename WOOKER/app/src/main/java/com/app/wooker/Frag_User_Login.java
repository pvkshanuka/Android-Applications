package com.app.wooker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wooker.AsyncTask.FbGoogleSignInAsyncTask;
import com.app.wooker.DBClasses.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Frag_User_Login extends Fragment {

    private static final int MY_REQUEST_CODE = 1212;
//    List<AuthUI.IdpConfig> providers;

    Button btn_signin;
    TextView tv_signup;
//    ProgressDialog progressDialog;

    private LoginButton loginButton;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;

    private Button fb_login;
    private Button goo_login;
    private Button email_login;

    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());


        MainActivity.where = "Login";

        View view = inflater.inflate(R.layout.frag_user_login, container, false);


        init(view);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                signInWithFirebase(credential);
            } catch (ApiException e) {
                Toasty.error(getContext(), "Google Authentication Failed", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FACBOOK LOGIN", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FACBOOK LOGIN", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void init(View view) {

        mAuth = FirebaseAuth.getInstance();

        getActivity().findViewById(R.id.main_loading).setVisibility(View.GONE);
        tv_signup = view.findViewById(R.id.tv_sign_up);
        btn_signin = view.findViewById(R.id.button_signin);
        goo_login = view.findViewById(R.id.btn_signin_google);
        fb_login = view.findViewById(R.id.btn_signin_facebook);
        email_login = view.findViewById(R.id.btn_signin_email);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        facebookLoginConfig();
        googleLoginConfig();


        email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Email_Signin dialog_email_signin = new Dialog_Email_Signin();

                MainActivity.fragmentController.setupDialog(dialog_email_signin);
            }
        });

        goo_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, MY_REQUEST_CODE);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setMessage("Please Watit.!");
//                progressDialog.show();
                Intent intent = new Intent(getActivity(), ClientMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
//                progressDialog.dismiss();

            }
        });

        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        btn_signin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setMessage("Please Watit.!");
//                progressDialog.show();
                Intent intent = new Intent(getActivity(), WorkerMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();

//                progressDialog.dismiss();

                return false;
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_AC_Type_Select dialog_ac_type_select = new Dialog_AC_Type_Select();

                MainActivity.fragmentController.setupDialog(dialog_ac_type_select);

            }
        });

    }

    private void googleLoginConfig() {
//        if (gso == null) {
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                    .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
//                        @Override
//                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                            Toasty.error(getContext(), "Error occurred while setting up the Google SIgn In", Toast.LENGTH_SHORT, true).show();
//                        }
//                    })
//                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                    .build();
//        }
//        if (gso == null) {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
//        }

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            try {

                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Toasty.error(getContext(), "Error occurred while setting up the Google SIgn In", Toast.LENGTH_SHORT, true).show();
                            }
                        })
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void facebookLoginConfig() {
        if (mCallbackManager == null) {
            mCallbackManager = CallbackManager.Factory.create();

            loginButton = getView().findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList("email"));
            loginButton.setFragment(this);
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                    signInWithFirebase(credential);
                }

                @Override
                public void onCancel() {
                    Toasty.error(getContext(), "Facebook Login Cancelled", Toast.LENGTH_SHORT, true).show();
                }

                @Override
                public void onError(FacebookException error) {
                    error.printStackTrace();
                    Toasty.error(getContext(), "Facebook Login Error", Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    private void signInWithFirebase(final AuthCredential credential) {
//        FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                FirebaseUser user = authResult.getUser();
//                Toasty.success(getContext(), "Login Sucess", Toast.LENGTH_SHORT, true).show();
//                signIN();
        Dialog_Lodaing dialog_lodaing = new Dialog_Lodaing("Pricessing..!", "Signin In.!");
        MainActivity.fragmentController.setupDialog(dialog_lodaing);
        new FbGoogleSignInAsyncTask(getActivity(), credential, dialog_lodaing).execute();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                e.printStackTrace();
//                Toasty.error(getContext(), "Login failed", Toast.LENGTH_SHORT, true).show();
//            }
//        });
    }

//    private void signIN() {
//    Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("Processing." ,"Signin In..!");
//    MainActivity.fragmentController.setupDialog(dialogLodaing);
//        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//
//                            User user = documentSnapshot.toObject(User.class);
//
//                            if (user.getType().equals("1")) {
//                                WorkerMain.mUser = user;
//                                Intent intent = new Intent(getActivity(), WorkerMain.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                getActivity().startActivity(intent);
//                                getActivity().finish();
//                            } else if (user.getType().equals("2")) {
//                                ClientMain.mUser = user;
//                                Intent intent = new Intent(getActivity(), ClientMain.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                getActivity().startActivity(intent);
//                                getActivity().finish();
//                            } else {
//                                Toasty.error(getActivity(), "Signing In Failed.! Please Sign In Againn.").show();
//                                Frag_User_Login fragLogin = new Frag_User_Login();
//
//                                MainActivity.fragmentController.setupFragment(fragLogin, false);
//                            }
//
//                        } else {
//                            MainActivity.fragmentController.setupDialog(new Dialog_AC_Type_Select());
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toasty.error(getActivity(), "Signing In Failed.! Please Sign In Againn.").show();
//                        Frag_User_Login fragLogin = new Frag_User_Login();
//                        MainActivity.fragmentController.setupFragment(fragLogin, false);
//                    }
//                });

//        dialogLodaing.dismiss();

//    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
