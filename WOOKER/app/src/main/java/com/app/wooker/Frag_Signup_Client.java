package com.app.wooker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.wooker.AsyncTask.SignUpAsyncTask;
import com.app.wooker.DBClasses.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CustomClasses.Validations;
import es.dmoral.toasty.Toasty;


public class Frag_Signup_Client extends Fragment {

    Button btn_sign_up;
    EditText et_fname, et_lname, et_cno;
    String gen = "";

    RadioButton rb_male, rb_female;
    RadioGroup radioGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.where = "Signup Client";
        Toasty.info(getActivity(), "onCreateView").show();
        View view = inflater.inflate(R.layout.frag_client_signup, container, false);


        init(view);


        return view;
    }

    private void init(View view) {
        Toasty.info(getActivity(), "init").show();

        btn_sign_up = view.findViewById(R.id.btn_signup_c);
        et_fname = view.findViewById(R.id.et_fname_c);
        et_lname = view.findViewById(R.id.et_lname_c);
        et_cno = view.findViewById(R.id.et_cno_c);

        rb_male = view.findViewById(R.id.ra_malec);
        rb_female = view.findViewById(R.id.ra_femalec);
        radioGroup = view.findViewById(R.id.radioGroupc);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                int id = radioGroup.getCheckedRadioButtonId();
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.ra_malec:
                        // Your code
                        gen = "Male";
                        break;
                    case R.id.ra_femalec:
                        // Your code
                        gen = "Female";
                        break;
                }
                System.out.println(gen);
            }

        });


        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_cno.getText().toString().equals("") ||
                        gen.equals("") ||
                        et_fname.getText().toString().equals("") ||
                        et_lname.getText().toString().equals("")) {
                    Toasty.error(getActivity(), "Some details are empty.!").show();
                } else {
                    if (Validations.conValidation(et_cno.getText().toString())) {

                        User user = new User(
                                FirebaseAuth.getInstance().getUid()
                                , et_fname.getText().toString()
                                , et_lname.getText().toString()
                                , et_cno.getText().toString()
                                , gen
                                , "2"
                                , "1", "", "");
                        System.out.println("Start Task >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        new SignUpAsyncTask(getActivity(), user, "client_main").execute();

                    } else {
                        Toasty.error(getActivity(), "Invalid Contact Number.!").show();
                    }
                }

            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Toasty.info(getActivity(), "onSaveInstanceState").show();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toasty.info(getActivity(), "onActivityCreated").show();

        if (savedInstanceState != null) {

            et_fname.setText(savedInstanceState.getString("fname"));
            et_lname.setText(savedInstanceState.getString("lname"));
            et_cno.setText(savedInstanceState.getString("cno"));
            gen = savedInstanceState.getString("gen");

            rb_male.setChecked(savedInstanceState.getBoolean("male"));
            rb_female.setChecked(savedInstanceState.getBoolean("female"));
        }
    }

}
