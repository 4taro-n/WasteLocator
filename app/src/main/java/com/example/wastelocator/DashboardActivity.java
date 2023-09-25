package com.example.wastelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarData;


public class DashboardActivity extends AppCompatActivity {
    private TextView ReportTextView;
    private TextView binTextView;
    private RecyclerView reportRecyclerView;
    private ReportAdapter reportAdapter;
    private RecyclerView binRecyclerView;

    private BinAdapter binAdapter;

    //chart

    private BarChart barChart;
    private ArrayList<BarEntry> entries;
    private BarDataSet dataSet;
    private BarData barData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setViewIds();

        generateChart();

        generateBinList();

        generateReportList();
    }
    private ArrayList<Bin> generateBinDummyData() {
        ArrayList<Bin> binList = new ArrayList<>();
        binList.add(new Bin("1", "100%", "0.1", "0.2"));
        binList.add(new Bin("1f", "100%", "0.1", "0.2"));
        binList.add(new Bin("1", "100%", "0.1", "0.2"));

        return binList;
    }
    private ArrayList<Report> generateReportDummyData() {
        ArrayList<Report> reportList = new ArrayList<>();
        reportList.add(new Report("Bin Location Accuracy", "test"));
        reportList.add(new Report("Bin Location Accuracy", "test"));
        reportList.add(new Report("Bin Location Accuracy", "test"));

        return reportList;
    }
    private void generateBinList() {
        binRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        ArrayList<Bin> binList = generateBinDummyData();

        binAdapter = new BinAdapter(binList);
        binRecyclerView.setAdapter(binAdapter);

        binAdapter.setOnItemClickListener(position -> {
            // Handle recyclerview item click here
            // For example, you can open a new activity
            Toast.makeText(DashboardActivity.this, binList.get(position).getId(), Toast.LENGTH_SHORT).show();

            //get report object when user clicked the item
            Bin clickedBin = binList.get(position);

            Intent intent = new Intent(DashboardActivity.this, MapActivity.class);

//            intent.putExtra("reportTitle", clickedReport.getTitle());
//            intent.putExtra("reportDescription", clickedReport.getDescription());

            startActivity(intent);
        });
    }
    private void generateReportList() {
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        ArrayList<Report> reportList = generateReportDummyData();

        reportAdapter = new ReportAdapter(reportList);
        reportRecyclerView.setAdapter(reportAdapter);

        reportAdapter.setOnItemClickListener(position -> {
            // Handle recyclerview item click here
            // For example, you can open a new activity
            Toast.makeText(DashboardActivity.this, reportList.get(position).getTitle(), Toast.LENGTH_SHORT).show();

            //get report object when user clicked the item
            Report clickedReport = reportList.get(position);

            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);

            intent.putExtra("reportTitle", clickedReport.getTitle());
            intent.putExtra("reportDescription", clickedReport.getDescription());

            startActivity(intent);
        });
    }

    private void generateChart() {
        entries = new ArrayList<>();
        entries.add(new BarEntry(1f, 10));
        entries.add(new BarEntry(2f, 20));
        entries.add(new BarEntry(3f, 30));

        dataSet = new BarDataSet(entries, "Test Data");
        barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();
    }

    private void setViewIds() {
        ReportTextView = findViewById(R.id.reportTextView);
        reportRecyclerView = findViewById(R.id.reportRecyclerView);
        binTextView = findViewById(R.id.binTextView);
        binRecyclerView = findViewById(R.id.binRecyclerView);
        barChart = findViewById(R.id.barChart);
    }
}