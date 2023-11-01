package com.example.wastelocator.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.enums.Align;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.TooltipPositionMode;
import com.example.wastelocator.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DashboardActivity extends AppCompatActivity {
    private TextView ReportTextView;
    private TextView binTextView;
    private ArrayList<Bin> bins = new ArrayList<>();
    private BinAdapter binAdapter;
    private RecyclerView binRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bindingViews();
        initialising();
        fetchBinData();
        handleClickOnEventItem();





    }

    private void bindingViews() {
        //ReportTextView = findViewById(R.id.reportTextView);
        //reportRecyclerView = findViewById(R.id.reportRecyclerView);

        binTextView = findViewById(R.id.binTextView);
        binRecyclerView = findViewById(R.id.binRecyclerView);

    }

    private void initialising() {
        binAdapter = new BinAdapter(bins);
        binRecyclerView.setAdapter(binAdapter);
        binRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void fetchBinData() {
        String url = "http://192.168.0.142:4567/bins";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
                setUpBarChart(response);

                try {
                    bins.clear();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject binJson = response.getJSONObject(i);

                        int id = binJson.getInt("id");
                        double latitude = binJson.getDouble("lat");
                        double longitude = binJson.getDouble("long");
                        int fillLevel = binJson.getInt("fill_level");

                        if(fillLevel == 100) { // Only add full bins
                            Bin bin = new Bin(id, latitude, longitude, fillLevel);
                            bins.add(bin);
                        }
                    }

                    binAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error here
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    private void setUpBarChart(JSONArray jsonArray) {
        AnyChartView barChart = findViewById(R.id.barChartView);
        barChart.setProgressBar(findViewById(R.id.progress_bar));
        Cartesian binBarChart = AnyChart.column();
        binBarChart.animation(true);
        List<DataEntry> data = new ArrayList<>();

        int[] binsInRanges  = new int[10]; // fill level ranges (0-9, 10-19, ..., 90-100).

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bin = jsonArray.getJSONObject(i);
                int fill_level = bin.getInt("fill_level");
                int rangeIndex = fill_level == 100 ? 9 : fill_level / 10;
                binsInRanges [rangeIndex]++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Adding the counts
        for (int i = 0; i < binsInRanges .length; i++) {
            String rangeLabel = (i * 10) + "-" + (i * 10 + 9);
            if (i == 9) rangeLabel = i * 10 + "-" + 100;
            data.add(new ValueDataEntry(rangeLabel, binsInRanges[i]));
        }

        binBarChart.column(data).name("Bins");
        binBarChart.labels(true);
        binBarChart.legend().enabled(true)
                .padding(0d, 0d, 20d, 0d)
                .position("top")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        binBarChart.title("Distribution of Bins by Fill Level");
        binBarChart.yScale().minimum(0d);
        binBarChart.xAxis(0).title("Fill Level Percentile Ranges");
        binBarChart.yAxis(0).title("Number of Bins");

        binBarChart.tooltip().positionMode(TooltipPositionMode.POINT);
        binBarChart.interactivity().hoverMode(HoverMode.BY_X);

        barChart.setChart(binBarChart);
    }

    private void handleClickOnEventItem() {
        binAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(this, MapActivity.class);
            Bin selectedBin = bins.get(position);
            intent.putExtra("lat", selectedBin.getLatitude());
            intent.putExtra("long", selectedBin.getLongitude());
            startActivity(intent);
        });
    }
}