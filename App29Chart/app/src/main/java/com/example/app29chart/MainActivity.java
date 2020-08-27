package com.example.app29chart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.Legend;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BarChart barChart = findViewById(R.id.chart1);

        ArrayList<BarEntry> allEntries = new ArrayList<>();

//        BarEntry entry1 = new BarEntry(0,100);
//        allEntries.add(entry1);

        allEntries.add(new BarEntry(0,100));
        allEntries.add(new BarEntry(10,120));
        allEntries.add(new BarEntry(20,90));
        allEntries.add(new BarEntry(30,80));
        allEntries.add(new BarEntry(40,110));
        allEntries.add(new BarEntry(50,60));
        allEntries.add(new BarEntry(60,90));


        BarDataSet barDataSet = new BarDataSet(allEntries,"Weekly Sellings");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

//        Customizw The Chart


        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(getColor(R.color.color1));
        colors.add(getColor(R.color.color2));
        colors.add(getColor(R.color.color3));
        colors.add(getColor(R.color.color4));
        colors.add(getColor(R.color.color5));
        colors.add(getColor(R.color.color6));
        colors.add(getColor(R.color.color7));

        barDataSet.setColors(colors);


        barChart.setPinchZoom(false);
        barChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);

        barChart.setDescription(null);

        barChart.animateY(2000,Easing.EaseOutSine);



        barData.setBarWidth(8);
        barChart.setFitBars(true);

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

        barChart.getLegend().setCustom(legendEntries);
        barChart.getLegend().setWordWrapEnabled(true);
        barChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

//        Customizw The Chart


        barChart.invalidate();

    }
}
