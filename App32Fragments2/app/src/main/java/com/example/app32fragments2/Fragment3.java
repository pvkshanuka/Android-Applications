package com.example.app32fragments2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment3 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment3_layout, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_set = getView().findViewById(R.id.button7);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView textView = getView().findViewById(R.id.textView5);
                textView.setText("Fragment 2 SET");


//                Activity ekei fragment ekei dekema ekama namin compornent thiyanawanm  me widiha use karanna ona
//                FragmentManager manager = getActivity().getSupportFragmentManager();
//                Fragment f1 = manager.findFragmentByTag("f1");
//                View view1 = f1.getView();
//                TextView textView2 = view1.findViewById(R.id.textView4);
//                textView2.setText("Fragment 2 SET");

                FragmentActivity activity = getActivity();
                TextView textView2 = activity.findViewById(R.id.textView4);
                textView2.setText("Fragment 2 SET");

            }
        });

    }
}
