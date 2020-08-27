package com.app.wooker;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wooker.AsyncTask.DialogMapLoadAsyncTask;
import com.app.wooker.AsyncTask.SignUpAsyncTask;
import com.app.wooker.DBClasses.User;
import com.app.wooker.DBClasses.WorkCats;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CustomClasses.RunOnUIThread;
import CustomClasses.Validations;
import es.dmoral.toasty.Toasty;


public class Frag_Signup_Worker extends Fragment {

    Button btn_signup, btn_addlocation;
    Spinner spinner;
    Spinner spinner_cats;

    EditText et_fname, et_lname, et_cno;
    String city;

    RadioButton rb_male, rb_female;
    RadioGroup radioGroup;

    String gen ="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.where = "Signup User";

        View view = inflater.inflate(R.layout.frag_worker_signup, container, false);

        btn_signup = view.findViewById(R.id.btn_signup_w);
        btn_addlocation = view.findViewById(R.id.btn_addlocation);
//        spinner = view.findViewById(R.id.spinner);
        spinner_cats = view.findViewById(R.id.spinner_cats2);
        et_fname = view.findViewById(R.id.et_fname_w);
        et_lname = view.findViewById(R.id.et_lname_w);
        et_cno = view.findViewById(R.id.et_cno_w);

        rb_male = view.findViewById(R.id.ra_male);
        rb_female = view.findViewById(R.id.ra_female);
        radioGroup = view.findViewById(R.id.radioGroup);

        btn_addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DialogMapLoadAsyncTask(getActivity(), "main").execute();

            }
        });


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadCats();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                int id = radioGroup.getCheckedRadioButtonId();
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.ra_male:
                        // Your code
                        gen = rb_male.getText().toString();
                        break;
                    case R.id.ra_female:
                        // Your code
                        gen = rb_female.getText().toString();
                        break;
                }
                System.out.println(gen);
            }

        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println(spinner_cats.getSelectedItem().toString()+" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                if (et_cno.getText().toString().equals("") ||
                        gen.equals("") ||
                        et_fname.getText().toString().equals("") ||
                        et_lname.getText().toString().equals("") ||
                        DialogMap.location == null
                        || spinner_cats.getSelectedItemPosition() == 0
                        || spinner_cats.getSelectedItem().toString().equals("")
                        || spinner_cats.getSelectedItem().toString() == null) {
                    Toasty.error(getActivity(), "Some details are empty.!").show();
                } else {
                    if (Validations.conValidation(et_cno.getText().toString())) {

                        Map<String, Double> location = new HashMap<>();
                        location.put("latitude", DialogMap.location.latitude);
                        location.put("longitude", DialogMap.location.longitude);

                        try {
                            Geocoder geocoder = new Geocoder(getActivity());
                            List<Address> addresses = geocoder.getFromLocation(DialogMap.location.latitude, DialogMap.location.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            if (addresses.size() != 0) {
                                System.out.println(addresses.get(0).toString());

                                if (addresses.get(0).getLocality() != null) {
                                    city = addresses.get(0).getLocality();
                                } else if (addresses.get(0).getSubAdminArea() != null) {
                                    city = addresses.get(0).getSubAdminArea();
                                } else if (addresses.get(0).getFeatureName() != null) {
                                    city = addresses.get(0).getFeatureName();
                                }

                                Toasty.info(getActivity(), city, Toasty.LENGTH_LONG).show();

                                User user = new User(
                                        FirebaseAuth.getInstance().getUid()
                                        ,et_fname.getText().toString()
                                        , et_lname.getText().toString()
                                        , et_cno.getText().toString()
                                        ,gen
                                        , 1
                                        , location, city, "1"
                                        , "1", "", "", false, null);

                                new SignUpAsyncTask(getActivity(), user, "worker_main", spinner_cats.getSelectedItem().toString()).execute();

                            } else {
                                Toasty.error(getActivity(), "Invalid Location.!").show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toasty.error(getActivity(), "Invalid Contact Number.!").show();
                    }
                }

            }
        });

    }

    private void loadCats() {

        new Thread(new Runnable() {

            @Override
            public void run() {


                final List<String> cats = new ArrayList<>();
                cats.add("Select Worker Type");

                FirebaseFirestore.getInstance().collection("work_cats").orderBy("type").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                    cats.add(documentSnapshot.toObject(WorkCats.class).getType());
                                }

                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                RunOnUIThread.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_list_item1, cats);
                        adapter.setDropDownViewResource(R.layout.my_spinner);
//
                        spinner_cats.setAdapter(adapter);

                    }
                });

            }
        }).start();


    }

}
