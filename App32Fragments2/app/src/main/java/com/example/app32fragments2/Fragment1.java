package com.example.app32fragments2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment1 extends android.support.v4.app.Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_layout, container, false);

        Button btnfragset = view.findViewById(R.id.button3);
        btnfragset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((TextView) view.findViewById(R.id.textView2)).setText("From Fragment");

                ((TextView) getActivity().findViewById(R.id.textView)).setText("From Fragment");

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            String string = savedInstanceState.getString("frag_txt");

            ((TextView) getView().findViewById(R.id.textView2)).setText(string);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString ("frag_txt",((TextView) getView().findViewById(R.id.textView2)).getText().toString());
    }
}
