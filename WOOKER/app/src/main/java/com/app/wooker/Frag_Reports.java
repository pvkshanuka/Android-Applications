package com.app.wooker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.wooker.DBClasses.Job;
import com.app.wooker.ViewModels.JobsViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CustomClasses.Validations;
import es.dmoral.toasty.Toasty;

public class Frag_Reports extends Fragment {

    JobsViewModel jobsViewModel;
    ArrayList<Job> jobs;
    Map<Integer, Integer> yearData;
    BarChart barChart;

    public Frag_Reports() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("AWA onCreate >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("AWA onCreateView >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        View view = inflater.inflate(R.layout.frag__reports, container, false);

        MainActivity.where = "Settings Worker";


//        yearData = new HashMap<>();
//        yearData.put(1, 0);
//        yearData.put(2, 0);
//        yearData.put(3, 0);
//        yearData.put(4, 0);
//        yearData.put(5, 0);
//        yearData.put(6, 0);
//        yearData.put(7, 0);
//        yearData.put(8, 0);
//        yearData.put(9, 0);
//        yearData.put(10, 0);
//        yearData.put(11, 0);
//        yearData.put(12, 0);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("AWA onViewCreated >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("AWA onResume >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        jobsViewModel = ViewModelProviders.of(this).get(JobsViewModel.class);
        jobsViewModel.startListener();
        jobsViewModel.getJobs().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(@Nullable List<Job> jobs2) {
                System.out.println("notifyDataSetChanged calling");
                yearData = new HashMap<>();
                yearData.put(1, 0);
                yearData.put(2, 0);
                yearData.put(3, 0);
                yearData.put(4, 0);
                yearData.put(5, 0);
                yearData.put(6, 0);
                yearData.put(7, 0);
                yearData.put(8, 0);
                yearData.put(9, 0);
                yearData.put(10, 0);
                yearData.put(11, 0);
                yearData.put(12, 0);
                for (Job job : jobs2) {
                    if (job.getStatus().equals("5")) {
                        if (Validations.dateObjToString(new Date(), "yyyy").equals(Validations.dateObjToString(job.getEnd_time(), "yyyy"))) {
                            yearData.put(Integer.parseInt(Validations.dateObjToString(job.getEnd_time(), "MM")), yearData.get(Integer.parseInt(Validations.dateObjToString(job.getEnd_time(), "MM"))) + 1);
                        }
                    }
                }
                try {

                    drawChart(getView());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }

    private void drawChart(View view) {

        System.out.println("AWA drawChart >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


        barChart = view.findViewById(R.id.chart1);
        System.out.println("CHECK LEVEL 1");


//            ArrayList NoOfEmp = new ArrayList();
//
//            for (Map.Entry me : yearData.entrySet()) {
//            System.out.println("Key: " + me.getKey() + " & Value: " + me.getValue());
//                NoOfEmp.add(new BarEntry(Float.parseFloat(me.getKey() + ""), Float.parseFloat(me.getValue() + "")));
//        }
//
////            ArrayList year = new ArrayList();
////
////            year.add("2008");
////            year.add("2009");
////            year.add("2010");
////            year.add("2011");
////            year.add("2012");
////            year.add("2013");
////            year.add("2014");
////            year.add("2015");
////            year.add("2016");
////            year.add("2017");
//
//            BarDataSet bardataset = new BarDataSet(NoOfEmp, "Annual Jobs");
//            barChart.animateY(5000);
//            BarData data = new BarData(bardataset);
//            bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
//
//
//        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
////            System.out.println("CHECK LEVEL 10");
//
//            LegendEntry legendEntry1 = new LegendEntry("Jan", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color1));
//            legendEntries.add(legendEntry1);
//            LegendEntry legendEntry2 = new LegendEntry("Feb", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color2));
//            legendEntries.add(legendEntry2);
//            LegendEntry legendEntry3 = new LegendEntry("Mar", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color3));
//            legendEntries.add(legendEntry3);
//            LegendEntry legendEntry4 = new LegendEntry("Apr", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color4));
//            legendEntries.add(legendEntry4);
//            LegendEntry legendEntry5 = new LegendEntry("May", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color5));
//            legendEntries.add(legendEntry5);
//            LegendEntry legendEntry6 = new LegendEntry("Jun", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color6));
//            legendEntries.add(legendEntry6);
//            LegendEntry legendEntry7 = new LegendEntry("Jul", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color7));
//            legendEntries.add(legendEntry7);
//            LegendEntry legendEntry8 = new LegendEntry("Aug", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color8));
//            legendEntries.add(legendEntry8);
//            LegendEntry legendEntry9 = new LegendEntry("Sep", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color9));
//            legendEntries.add(legendEntry9);
//            LegendEntry legendEntry10 = new LegendEntry("Oct", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color10));
//            legendEntries.add(legendEntry10);
//            LegendEntry legendEntry11 = new LegendEntry("Nov", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color11));
//            legendEntries.add(legendEntry11);
//            LegendEntry legendEntry12 = new LegendEntry("Dec", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color12));
////            System.out.println("CHECK LEVEL 11");
//            legendEntries.add(legendEntry12);
//
//            barChart.getLegend().setCustom(legendEntries);
//            barChart.getLegend().setWordWrapEnabled(true);
//            barChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        barChart.setData(data);

        ArrayList<BarEntry> allEntries = new ArrayList<>();
        System.out.println("CHECK LEVEL 2");

//        BarEntry entry1 = new BarEntry(0,100);
//        allEntries.add(entry1);

        for (Map.Entry me : yearData.entrySet()) {
            System.out.println("Key: " + me.getKey() + " & Value: " + me.getValue());
            allEntries.add(new BarEntry(Float.parseFloat(me.getKey() + ""), Float.parseFloat(me.getValue() + "")));
        }
        System.out.println("CHECK LEVEL 3");

//            allEntries.add(new BarEntry(1, 100));
//            allEntries.add(new BarEntry(2, 120));
//            allEntries.add(new BarEntry(3, 90));
//            allEntries.add(new BarEntry(4, 80));
//            allEntries.add(new BarEntry(5, 110));
//            allEntries.add(new BarEntry(6, 20));
//            allEntries.add(new BarEntry(7, 40));
//            allEntries.add(new BarEntry(8, 80));
//            allEntries.add(new BarEntry(9, 112));
//            allEntries.add(new BarEntry(10, 75));
//            allEntries.add(new BarEntry(11, 68));
//            allEntries.add(new BarEntry(12, 68));


        BarDataSet barDataSet = new BarDataSet(allEntries, "Annual Jobs");
        BarData barData = new BarData(barDataSet);
//            barChart.setData(barData);
        System.out.println("CHECK LEVEL 4");

//        Customizw The Chart


        ArrayList<Integer> colors = new ArrayList<>();
        System.out.println("CHECK LEVEL 5");

        colors.add(getActivity().getColor(R.color.color1));
        colors.add(getActivity().getColor(R.color.color2));
        colors.add(getActivity().getColor(R.color.color3));
        colors.add(getActivity().getColor(R.color.color4));
        colors.add(getActivity().getColor(R.color.color5));
        colors.add(getActivity().getColor(R.color.color6));
        colors.add(getActivity().getColor(R.color.color7));
        colors.add(getActivity().getColor(R.color.color8));
        colors.add(getActivity().getColor(R.color.color9));
        colors.add(getActivity().getColor(R.color.color10));
        colors.add(getActivity().getColor(R.color.color11));
        colors.add(getActivity().getColor(R.color.color12));
        System.out.println("CHECK LEVEL 6");

        barDataSet.setColors(colors);
        System.out.println("CHECK LEVEL 7");


//        barChart.setPinchZoom(false);
//        barChart.setScaleXEnabled(false);
//        barChart.setScaleYEnabled(false);

        barChart.setDescription(null);

        barChart.animateY(2000, Easing.EaseOutSine);

        System.out.println("CHECK LEVEL 8");

        barData.setBarWidth(0.8f);
        barChart.setFitBars(true);
        System.out.println("CHECK LEVEL 9");

        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        System.out.println("CHECK LEVEL 10");

        LegendEntry legendEntry1 = new LegendEntry("Jan", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color1));
        legendEntries.add(legendEntry1);
        LegendEntry legendEntry2 = new LegendEntry("Feb", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color2));
        legendEntries.add(legendEntry2);
        LegendEntry legendEntry3 = new LegendEntry("Mar", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color3));
        legendEntries.add(legendEntry3);
        LegendEntry legendEntry4 = new LegendEntry("Apr", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color4));
        legendEntries.add(legendEntry4);
        LegendEntry legendEntry5 = new LegendEntry("May", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color5));
        legendEntries.add(legendEntry5);
        LegendEntry legendEntry6 = new LegendEntry("Jun", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color6));
        legendEntries.add(legendEntry6);
        LegendEntry legendEntry7 = new LegendEntry("Jul", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color7));
        legendEntries.add(legendEntry7);
        LegendEntry legendEntry8 = new LegendEntry("Aug", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color8));
        legendEntries.add(legendEntry8);
        LegendEntry legendEntry9 = new LegendEntry("Sep", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color9));
        legendEntries.add(legendEntry9);
        LegendEntry legendEntry10 = new LegendEntry("Oct", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color10));
        legendEntries.add(legendEntry10);
        LegendEntry legendEntry11 = new LegendEntry("Nov", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color11));
        legendEntries.add(legendEntry11);
        LegendEntry legendEntry12 = new LegendEntry("Dec", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getActivity().getColor(R.color.color12));
        System.out.println("CHECK LEVEL 11");
        legendEntries.add(legendEntry12);

        barChart.getLegend().setCustom(legendEntries);
        barChart.getLegend().setWordWrapEnabled(true);
        barChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        System.out.println("CHECK LEVEL 12");

//        Customizw The Chart
//        barChart.setDrawValueAboveBar(true);
//        barChart.setDrawBarShadow(true);
        barChart.setData(barData);
        barChart.invalidate();
        System.out.println("CHECK LEVEL 13");


    }
}
