package com.app.wooker;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wooker.AsyncTask.PasswordResetAsyncTask;
import com.app.wooker.DBClasses.User;
import com.app.wooker.SQLLignt.SQLightHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import CustomClasses.Validations;
import es.dmoral.toasty.Toasty;

public class Dialog_Email_Signin extends AppCompatDialogFragment {

    EditText et_email, et_pword;

    FirebaseAuth mAuth;

    Button btn_sign_up, btn_sign_in;
    TextView tv_forg_pw;
    CheckBox cb_rem_me;

    Dialog_Lodaing dialogLodaing;
    SQLightHelper helper;
    SQLiteDatabase database;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_email_signin, null);

        init(view);

        builder.setView(view)
                .setTitle("Please Enter Email & Password")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog_Email_Signin.this.dismiss();
//                        Toast.makeText(getActivity(), "Email Sign In Failed.!", Toast.LENGTH_SHORT).show();
                        Toasty.error(getActivity(), "Email Login Failed.!").show();
                    }
                });


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailLoginValidation()) {
                    new EmailSignInProcess(getActivity()).execute();
                }
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailLoginValidation()) {
                    new EmailSignUpProcess(getActivity()).execute();
                }
            }
        });

//        bc_rem_me.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rb_rem_me.toggle();
//            }
//        });

        tv_forg_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(et_email.getText().toString())) {
                    Toasty.error(getActivity(), "Please enter your Email to send password reset link.").show();
                } else {
                    new PasswordResetAsyncTask(getActivity(), et_email.getText().toString()).execute();
                    Dialog_Email_Signin.this.dismiss();
                }

            }
        });


        loadSavedLoginDetails();

        return builder.create();
    }

    private void loadSavedLoginDetails() {
        try {
            System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa loadSavedLoginDetails");
            database = helper.getWritableDatabase();

            Cursor cursor = database.rawQuery("SELECT * FROM login", null);

            if (cursor.moveToNext()) {
                System.out.println("Load data pw - "+cursor.getString(2));
                System.out.println("_________________________");
                System.out.println(cursor.getString(0));
                System.out.println(cursor.getString(1));
                System.out.println(cursor.getString(2));

                et_email.setText(cursor.getString(1));
                et_pword.setText(cursor.getString(2));
                cb_rem_me.setChecked(true);
            }
        } catch (Exception e) {
            if (!(e.getMessage().contains("no such table"))) {
                e.printStackTrace();
            }
        }
    }


    private boolean emailLoginValidation() {

        if (et_email.getText().toString().equals("") || et_pword.getText().toString().equals("")) {
//            Dialog_Email_Signin.this.dismiss();
//            Toast.makeText(getActivity(), "Some details are empty.!", Toast.LENGTH_SHORT).show();
            Toasty.error(getActivity(), "Some details are empty.!").show();
            return false;
        } else {
            if (Validations.pwValidation(et_pword.getText().toString())) {
//                Dialog_Email_Signin.this.dismiss();
//                Toast.makeText(getActivity(), "Invalid password.!", Toast.LENGTH_SHORT).show();
                return true;
            } else {
//                Dialog_Email_Signin.this.dismiss();
                Toasty.error(getActivity(), "Invalid password.!").show();
                return false;
            }
        }

    }

    private void init(View view) {

        mAuth = FirebaseAuth.getInstance();

        helper = new SQLightHelper(getActivity());

        et_email = view.findViewById(R.id.pt_email_sign_in);
        et_pword = view.findViewById(R.id.pt_pword_sign_in);

        btn_sign_in = view.findViewById(R.id.btn_signin_email);
        btn_sign_up = view.findViewById(R.id.btn_signup_email);

        tv_forg_pw = view.findViewById(R.id.tv_forgot_pw);

        cb_rem_me = view.findViewById(R.id.cb_rem_me);

    }

    class EmailSignUpProcess extends AsyncTask<String, String, String> {


        Activity activity;

        public EmailSignUpProcess(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... strings) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(et_email.getText().toString(), et_pword.getText().toString())
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialogLodaing.dismiss();
                            Toasty.error(activity, e.getMessage(), Toasty.LENGTH_LONG).show();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            dialogLodaing.dismiss();
                            Toasty.success(activity, "User Created Successfuly.!").show();
                            authResult.getUser().sendEmailVerification();
                            Toasty.success(activity, "Email verification code sent to your email. Email - " + authResult.getUser().getEmail()).show();

                        }
                    });


            return null;
        }

        @Override
        protected void onPreExecute() {

            dialogLodaing = new Dialog_Lodaing("Processing.!");
            MainActivity.fragmentController.setupDialog(dialogLodaing);

        }

    }

    private void selectUserType() {

        Dialog_Email_Signin.this.dismiss();
        dialogLodaing.dismiss();

        Dialog_AC_Type_Select dialogAcTypeSelect = new Dialog_AC_Type_Select();
        MainActivity.fragmentController.setupDialog(dialogAcTypeSelect);

    }

    class EmailSignInProcess extends AsyncTask<String, String, String> implements OnFailureListener, OnSuccessListener {


        Activity activity;

        public EmailSignInProcess(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {

            dialogLodaing = new Dialog_Lodaing("Processing.!");
            MainActivity.fragmentController.setupDialog(dialogLodaing);

        }

        @Override
        protected String doInBackground(String... strings) {


            FirebaseAuth.getInstance().signInWithEmailAndPassword(et_email.getText().toString(), et_pword.getText().toString())
                    .addOnFailureListener(this)
                    .addOnSuccessListener(this);


            return null;
        }


        @Override
        public void onFailure(@NonNull Exception e) {
            e.printStackTrace();
            dialogLodaing.dismiss();

            Toasty.error(activity, e.getMessage(), Toasty.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(Object o) {

            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {

                if (cb_rem_me.isChecked()) {
                    database = helper.getWritableDatabase();
                    System.out.println("Set data pw - "+et_pword.getText().toString());

                    database.execSQL("INSERT INTO login(email,password) VALUES ('" + et_email.getText().toString() + "','" + et_pword.getText().toString() + "')");
                }else{
                    database = helper.getWritableDatabase();
                    database.execSQL("DELETE FROM login");
                }

                checkIsUserAdded();

            } else {
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                Toasty.error(activity, "Please Verify Email.! New Veification link sent to your email. Email-" + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toasty.LENGTH_LONG).show();
            }
        }
    }


    private void checkIsUserAdded() {

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user.getType().equals("1")) {
                                WorkerMain.mUser = user;
                                Intent intent = new Intent(getActivity(), WorkerMain.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            } else if (user.getType().equals("2")) {
                                ClientMain.mUser = user;
                                Intent intent = new Intent(getActivity(), ClientMain.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }
                            Toasty.success(getActivity(), "Signed In Successfuly.!").show();
                        } else {
                            selectUserType();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toasty.error(getActivity(), e.getMessage(), Toasty.LENGTH_LONG).show();
                    }
                });
    }

}
