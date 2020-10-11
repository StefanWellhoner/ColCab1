package com.colcab.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colcab.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {


    public StatisticsFragment() {
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

        List<PieEntry> pieEntry = new ArrayList<PieEntry>();

        pieEntry.add(new PieEntry(5, "Scheduled"));
        pieEntry.add(new PieEntry(3, "Unscheduled"));

        PieDataSet dataSet = new PieDataSet(pieEntry, "Tickets");

        dataSet.setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        dataSet.addColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        dataSet.setValueTextSize(24);

        PieData pieData = new PieData();

        pieData.addDataSet(dataSet);

        PieChart chart = view.findViewById(R.id.chart);
        chart.setData(pieData);
        chart.invalidate();

        List<BarEntry> barEntry = new ArrayList<BarEntry>();

        barEntry.add(new BarEntry(0f, 30f));
        barEntry.add(new BarEntry(1f, 80f));
        barEntry.add(new BarEntry(2f, 60f));
        barEntry.add(new BarEntry(3f, 50f));
        barEntry.add(new BarEntry(4f, 70f));
        barEntry.add(new BarEntry(5f, 60f));

        BarDataSet barSet = new BarDataSet(barEntry, "Fault Graph");
        barSet.setColor(Color.rgb(155, 155, 155));
        barSet.setValueTextColor(Color.rgb(155,155,155));

        BarData barData = new BarData();

        barData.addDataSet(barSet);

        BarChart barChart = view.findViewById(R.id.chart1);

        Description desc = barChart.getDescription();
        desc.setText("");

        barChart.setData(barData);
        barChart.invalidate();

        return view;
    }
}