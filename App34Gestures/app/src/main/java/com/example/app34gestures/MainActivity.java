package com.example.app34gestures;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView.setText("Start");

        GestureDetector.OnDoubleTapListener doubleTapListener = new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                textView.setText("onSingleTapConfirmed");
                System.out.println("onSingleTapConfirmed");
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                textView.setText("onDoubleTap");
                System.out.println("onDoubleTap");
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                textView.setText("onDoubleTapEvent");
                System.out.println("onDoubleTapEvent");
                return false;
            }
        };

        GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                textView.setText("onDown");
                System.out.println("onDown");
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                textView.setText("onShowPress");
                System.out.println("onShowPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                textView.setText("onSingleTapUp");
                System.out.println("onSingleTapUp");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                textView.setText("onScroll X="+distanceX+" Y="+distanceY);
                System.out.println("onScroll");
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                textView.setText("onLongPress");
                System.out.println("onLongPress");
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                textView.setText("onFling");
                System.out.println("onFling");
                return false;
            }
        };

        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);
        gestureDetectorCompat.setOnDoubleTapListener(doubleTapListener);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(gestureDetectorCompat.onTouchEvent(event)){
            return true;
        }

        return super.onTouchEvent(event);
    }
}
