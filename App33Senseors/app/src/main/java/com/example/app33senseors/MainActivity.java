package com.example.app33senseors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SensorManager manager;
    Sensor sensor;
    Sensor sensor2;
    Sensor sensor3;
    SensorEventListener sensorEventListener;
    SensorEventListener sensorEventListener2;
    SensorEventListener sensorEventListener3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);

        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAllSensors(v);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSensors(v);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSensor2(v);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSensor3(v);
            }
        });

        if (manager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            Toast.makeText(this, "Light Sensor Found", Toast.LENGTH_SHORT).show();

            sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float values[] = event.values;
                    System.out.println("Sensor Changed - " + values[0]);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    System.out.println("Sensor Accuracy Changed");
                }
            };

            manager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            Toast.makeText(this, "Light Sensor Not Found", Toast.LENGTH_SHORT).show();
        }

    }

    private void addSensor3(View v) {

        if (manager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            Toast.makeText(this, "Sensor Found", Toast.LENGTH_SHORT).show();

            sensor3 = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

            sensorEventListener3 = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float values[] = event.values;
                    float x = values[0];

                    System.out.println("Distance - " + x);
                    TextView textView = findViewById(R.id.textView);
                    textView.setText("Distance - " + x);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }


            };

            manager.registerListener(sensorEventListener3, sensor3, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Sensor Not Found", Toast.LENGTH_SHORT).show();
        }

    }

    private void addSensor2(View v) {

        if (manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {


            sensor2 = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            sensorEventListener2 = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float values[] = event.values;
                    float x = values[0];
                    float y = values[1];
                    float z = values[2];

                    System.out.println(x + "-" + y + "-" + z);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }


            };

            manager.registerListener(sensorEventListener2, sensor2, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Sensor Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (sensorEventListener != null) {
            manager.unregisterListener(sensorEventListener);
        }

    }

    private void checkSensors(View v) {

        if (manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            Toast.makeText(this, "Magnetometer Found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Magnetometer Not Found", Toast.LENGTH_SHORT).show();

        }

    }

    private void viewAllSensors(View view) {

        List<Sensor> sensorList = manager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor sensor : sensorList) {
            System.out.println(sensor.getName());
        }

    }
}
