package com.example.app32fragments2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2_layout, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_set = getView().findViewById(R.id.button6);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = getView().findViewById(R.id.textView4);
                textView.setText("Fragment 1 SET");

//                FragmentManager manager = getActivity().getSupportFragmentManager();
//                Fragment f2 = manager.findFragmentByTag("f2");
//                View view1 = f2.getView();
//                TextView textView2 = view1.findViewById(R.id.textView5);
//                textView2.setText("Fragment 1 SET");

                FragmentActivity activity = getActivity();
                TextView textView2 = activity.findViewById(R.id.textView5);
                textView2.setText("Fragment 1 SET");

            }
        });

    }
}
