package com.example.micraapp;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.micraapp.databinding.ActivityBarChartBinding;
import com.example.micraapp.databinding.ActivityCrime2Binding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BarChartActivity extends drawerbase implements OnChartValueSelectedListener, View.OnClickListener {

    ActivityBarChartBinding activityBarChartBinding;
    BarChart barChart;
    ProgressDialog progressDialog;
    public XAxis xAxis;
    private List<String> crimecategory;
    private List<Integer> count;
    private List<String> newcount;
    private ArrayList<BarEntry> ArrayBarEntry;
    BarEntry barEntry;
    private BarDataSet barDataSet;
    private String ANALYTICS_URL = "http://www.micra.services/FetchAnalytics.php";


    BroadcastReceiver broadcastReceiver;


    SharedPreferences C1, C2, C3;
    SharedPreferences.Editor C1editor;
    SharedPreferences.Editor C2editor;
    SharedPreferences.Editor C3editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        activityBarChartBinding = activityBarChartBinding.inflate(getLayoutInflater());
        allocateActivitytitle("BarChartActivity");

        setContentView(activityBarChartBinding.getRoot());

        barChart = findViewById(R.id.barchart);
        barChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) BarChartActivity.this);

        ArrayBarEntry = new ArrayList<BarEntry>();
        crimecategory = new ArrayList<String>();
        count = new ArrayList<Integer>();
        newcount = new ArrayList<String>();


        broadcastReceiver = new NetworkListener();
        restorecon();





        //SHARED REF
        C1 = getSharedPreferences("SPC1", Context.MODE_PRIVATE);
        C2 = getSharedPreferences("SPC2", Context.MODE_PRIVATE);
        C3 = getSharedPreferences("SPC3", Context.MODE_PRIVATE);

        C1editor = C1.edit();
        C2editor = C2.edit();
        C3editor = C3.edit();
        C1editor.clear().commit();
        C2editor.clear().commit();
        C3editor.clear().commit();

        LoadAnalytics();


    }


    private void LoadAnalytics() {
        progressDialog = progressDialog.show(this, "Please wait", "loading data..");


        StringRequest stringRequest = new StringRequest(Request.Method.GET, ANALYTICS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {

                            Log.d("Response",""+response);
                            try {
                                JSONObject jsonobject = new JSONObject(response);
                                JSONArray jsonarray = jsonobject.getJSONArray("reports");


                                for (int i = 0; i < jsonarray.length(); i++) {

                                    JSONObject report = jsonarray.getJSONObject(i);

                                    crimecategory.add(report.getString("crimecategory"));
                                    count.add(report.getInt("COUNT(crimecategory)"));

                                }


                                String counting;

                                for (int i = 0; i < count.size(); i++) {
                                    int in = count.get(i);

                                    counting = String.format(Locale.US, "%%%df", in);
                                    counting = counting.replaceAll("[^a-zA-Z0-9]", "");

                                    barEntry = new BarEntry(i, Float.parseFloat(counting));

                                    ArrayBarEntry.add(barEntry);


                                }


                                barDataSet = new BarDataSet(ArrayBarEntry, "Daily Cases");
                                barDataSet.setValueFormatter(new ValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value) {
                                        return String.valueOf((int) value);
                                    }
                                });
                                barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

                                barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                barDataSet.setValueTextSize(12f);
                                BarData barData = new BarData(barDataSet);


                                barChart.setData(barData);
                                barChart.setFitBars(true);
                                barChart.getDescription().setEnabled(false);
                                barChart.setScaleEnabled(false);
                                barChart.getAxisRight().setEnabled(false);
                                barChart.animateY(5000);
                                YAxis yAxis = barChart.getAxisLeft();
                                barChart.getAxisLeft().setGranularityEnabled(true);

                                yAxis.setAxisMaxValue(count.size() + 100f);
                                yAxis.setAxisMinValue(0f);

                                xAxis = barChart.getXAxis();


                                xAxis.setCenterAxisLabels(false);
                                barChart.getXAxis().setSpaceMax(1);

                                // Setting position of xAxis
                                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                                xAxis.setLabelRotationAngle(-45);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(crimecategory));


                                // xAxis customization
                                barChart.notifyDataSetChanged();
                                barChart.invalidate();
                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(BarChartActivity.this, "No crime reported", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(BarChartActivity.this).add(stringRequest);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        BarEntry pe = (BarEntry) e;
        Log.e("LABEL", String.valueOf(pe.getX()));

    }

    @Override
    public void onNothingSelected() {
        // do nothing
    }

    @Override
    public void onClick(View v) {

    }


    protected void restorecon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter((ConnectivityManager.CONNECTIVITY_ACTION)));
        }
    }

    protected void unrestorecon() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unrestorecon();
    }
}

