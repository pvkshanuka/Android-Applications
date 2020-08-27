package com.app.wooker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class Frag_Login extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frag__login, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Button btn_signup = view.findViewById(R.id.btn_signup_l);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                for (Fragment fragment : fragmentManager.getFragments()) {
//                    fragmentTransaction.remove(fragment);
//                }
//
//
//                fragmentTransaction.add(R.id.main_cons_lay,new Frag_Signup_Client(),"fragSignup");
//                fragmentTransaction.commit();

                Dialog_AC_Type_Select dialog_ac_type_select = new Dialog_AC_Type_Select();
                dialog_ac_type_select.show(getActivity().getSupportFragmentManager(),"Dialog");


            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
