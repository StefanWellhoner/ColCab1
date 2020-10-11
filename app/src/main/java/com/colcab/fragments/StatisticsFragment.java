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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
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

        List<PieEntry> pieEntry = new ArrayList<PieEntry>();

        pieEntry.add(new PieEntry(5, "Scheduled"));
        pieEntry.add(new PieEntry(3, "Unscheduled"));

        PieDataSet dataSet = new PieDataSet(pieEntry, "Tickets");

//        LineDataSet dataSet = new LineDataSet(entries, "Names");

        dataSet.setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        dataSet.addColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        dataSet.setValueTextSize(24);

        PieData pieData = new PieData();

        pieData.addDataSet(dataSet);

        PieChart chart = (PieChart) view.findViewById(R.id.chart);
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

        BarChart barChart = (BarChart) view.findViewById(R.id.chart1);

        Description desc = barChart.getDescription();
        desc.setText("");

        barChart.setData(barData);
        barChart.invalidate();

//        LineChart lineChart = (LineChart) view.findViewById(R.id.lineChart);
//        lineChart.setTouchEnabled(true);
//        lineChart.setPinchZoom(true);
//
//        LimitLine ll1 = new LimitLine(30f, "Title");
//
//        ll1.setLineColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
//        ll1.setLineWidth(4f);
//        ll1.enableDashedLine(10f, 10f, 0f);
//
//        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        ll1.setTextSize(10f);
//
//        LimitLine ll2 = new LimitLine(35f, "");
//        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
//
//        XAxis xAxis = lineChart.getXAxis();
//        YAxis leftAxis = lineChart.getAxisLeft();
//
//        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
//        xAxis.setPosition(position);
//
//        lineChart.getDescription().setEnabled(true);
//        Description description = new Description();
//        description.setText("Week");
//        description.setTextSize(15f);

        return view;
    }
}