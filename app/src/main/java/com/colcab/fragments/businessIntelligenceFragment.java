package com.colcab.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.colcab.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class businessIntelligenceFragment extends Fragment {


    public businessIntelligenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_intelligence, container, false);


//        Pie pie = AnyChart.pie();
//
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("John", 10000));
//        data.add(new ValueDataEntry("Jake", 12000));
//        data.add(new ValueDataEntry("Peter", 18000));
//
//        pie.data(data);
//
//        AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.any_chart_view);
//        anyChartView.setChart(pie);

        List<PieEntry> entry1 = new ArrayList<PieEntry>();
        List<PieEntry> entry2 = new ArrayList<PieEntry>();

        entry1.add(new PieEntry(5, "Scheduled"));
        entry1.add(new PieEntry(3, "Unscheduled"));

        PieDataSet dataSet = new PieDataSet(entry1, "Tickets");

//        LineDataSet dataSet = new LineDataSet(entries, "Names");

        dataSet.setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        dataSet.addColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        dataSet.setValueTextSize(24);

        PieData pieData = new PieData();

        pieData.addDataSet(dataSet);

        PieChart chart = (PieChart) view.findViewById(R.id.chart);

        chart.setData(pieData);
        chart.invalidate();

        return view;
    }
}