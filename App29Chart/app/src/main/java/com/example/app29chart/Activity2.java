package com.example.app29chart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.Legend;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        LineChart lineChart = findViewById(R.id.chart2);

        ArrayList<Entry> entries = new ArrayList<>();

        entries.add(new Entry(0,100,"Kusal"));
        entries.add(new Entry(10,120));
        entries.add(new Entry(20,90));
        entries.add(new Entry(30,80));
        entries.add(new Entry(40,110));
        entries.add(new Entry(50,60));
        entries.add(new Entry(60,90));

        LineDataSet lineDataSet = new LineDataSet(entries,"Weekly Sellings");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);


//        Customizw The Chart


        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(getColor(R.color.color1));
        colors.add(getColor(R.color.color2));
        colors.add(getColor(R.color.color3));
        colors.add(getColor(R.color.color4));
        colors.add(getColor(R.color.color5));
        colors.add(getColor(R.color.color6));
        colors.add(getColor(R.color.color7));

        lineDataSet.setColors(colors);

        lineChart.setPinchZoom(false);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);

        lineChart.setDescription(null);

        lineChart.animateX(2000,Easing.EaseOutSine);

//        lineDataSet.setColor(getColor(R.color.color6));
        lineDataSet.setLineWidth(5);



//        lineChart.setBorderWidth(10);
//        lineChart.setFitsSystemWindows(););

        ArrayList<LegendEntry> legendEntries = new ArrayList<>();

        LegendEntry legendEntry1 = new LegendEntry("Monday",Legend.LegendForm.CIRCLE,Float.NaN,Float.NaN,null,getColor(R.color.color1));
        legendEntries.add(legendEntry1);
        LegendEntry legendEntry2 = new LegendEntry("Tuesday",Legend.LegendForm.CIRCLE,Float.NaN,Float.NaN,null,getColor(R.color.color2));
        legendEntries.add(legendEntry2);
        LegendEntry legendEntry3 = new LegendEntry("Wednesday",Legend.LegendForm.CIRCLE,Float.NaN,Float.NaN,null,getColor(R.color.color3));
        legendEntries.add(legendEntry3);
        LegendEntry legendEntry4 = new LegendEntry("Thursday",Legend.LegendForm.CIRCLE,Float.NaN,Float.NaN,null,getColor(R.color.color4));
        legendEntries.add(legendEntry4);
        LegendEntry legendEntry5 = new LegendEntry("Friday",Legend.LegendForm.CIRCLE,Float.NaN,Float.NaN,null,getColor(R.color.color5));
        legendEntries.add(legendEntry5);
        LegendEntry legendEntry6 = new LegendEntry("Saturday",Legend.LegendForm.CIRCLE,Float.NaN,Float.NaN,null,getColor(R.color.color6));
        legendEntries.add(legendEntry6);
        LegendEntry legendEntry7 = new LegendEntry("Sunday",Legend.LegendForm.CIRCLE,Float.NaN,Float.NaN,null,getColor(R.color.color7));
        legendEntries.add(legendEntry7);

        lineChart.getLegend().setCustom(legendEntries);
        lineChart.getLegend().setWordWrapEnabled(true);
        lineChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

//        Customizw The Chart


        lineChart.invalidate();

    }
}
