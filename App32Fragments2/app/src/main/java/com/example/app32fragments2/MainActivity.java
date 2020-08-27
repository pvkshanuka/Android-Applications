package com.example.app32fragments2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_addfrag = findViewById(R.id.button2);
        Button btn_actset = findViewById(R.id.button);

        btn_addfrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment1 fragment1 = new Fragment1();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                List<Fragment> fragmentList = manager.getFragments();

                for (Fragment fragm:fragmentList){
                    System.out.println("FR");
                    transaction.remove(fragm);
                }


                transaction.add(R.id.cons, fragment1,"f1");
                transaction.commit();

                View view = findViewById(R.id.cons);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.anim1);
                animation.setDuration(500);
                view.startAnimation(animation);


            }
        });

        btn_actset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView_act = findViewById(R.id.textView);

                textView_act.setText("From Activity");

                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentByTag("f1");



                if (fragment != null ){
                    ((TextView) fragment.getView().findViewById(R.id.textView2)).setText("From Activity");
                }

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        textView_act = findViewById(R.id.textView);

        outState.putString("tv_act",textView_act.getText().toString());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        textView_act = findViewById(R.id.textView);

        textView_act.setText(savedInstanceState.getString("tv_act"));

    }
}
