package com.example.app29chart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.Legend;

import java.util.ArrayList;

public class Activity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        PieChart pieChart = findViewById(R.id.chart3);

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(8,"Monday"));
        entries.add(new PieEntry(10,"Tuesday"));
        entries.add(new PieEntry(11,"Wednesday"));
        entries.add(new PieEntry(29,"Thursday"));
        entries.add(new PieEntry(21,"Friday"));
        entries.add(new PieEntry(32,"Saturday"));
        entries.add(new PieEntry(5,"Sunday"));

        PieDataSet pieDataSet = new PieDataSet(entries,"Weekly Sellings");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);


//        Customizw The Chart

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(getColor(R.color.color1));
        colors.add(getColor(R.color.color2));
        colors.add(getColor(R.color.color3));
        colors.add(getColor(R.color.color4));
        colors.add(getColor(R.color.color5));
        colors.add(getColor(R.color.color6));
        colors.add(getColor(R.color.color7));

        pieDataSet.setColors(colors);

//        pieChart.setPinchZoom(false);
//        pieChart.setScaleXEnabled(false);
//        pieChart.setScaleYEnabled(false);

        pieChart.setDescription(null);

        pieChart.animateY(2000,Easing.EaseOutSine);



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

        pieChart.getLegend().setCustom(legendEntries);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        pieChart.getLegend().setEnabled(false);

        pieChart.setCenterText("Weekly Sellings");
        pieChart.setCenterTextColor(getColor(R.color.colorPrimaryDark));
        pieChart.setCenterTextSizePixels(60);

        pieDataSet.setValueTextSize(15);
        pieDataSet.setValueTextColor(getColor(R.color.white));

//        Customizw The Chart


        pieChart.invalidate();

    }
}
