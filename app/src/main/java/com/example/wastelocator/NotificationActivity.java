package com.example.wastelocator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.Toast;
import android.content.Intent;


public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Report> reportList = generateDummyData();

        reportAdapter = new ReportAdapter(reportList);
        recyclerView.setAdapter(reportAdapter);

        reportAdapter.setOnItemClickListener(position -> {
            // Handle recyclerview item click here
            // For example, you can open a new activity
            //Toast.makeText(NotificationActivity.this, reportList.get(position).getTitle(), Toast.LENGTH_SHORT).show();

            //get report object when user clicked the item
            Report clickedReport = reportList.get(position);

            Intent intent = new Intent(NotificationActivity.this, NotificationDetailActivity.class);

            intent.putExtra("reportTitle", clickedReport.getTitle());
            intent.putExtra("reportDescription", clickedReport.getDescription());

            startActivity(intent);
        });
    }

    // Replace this method with your actual vehicle data
    private ArrayList<Report> generateDummyData() {
        ArrayList<Report> reportList = new ArrayList<>();
        reportList.add(new Report("Begin with the basics: Waste Segregation", "Description"));
        reportList.add(new Report("Reduce Singe-Use Plastic", "Description"));
        reportList.add(new Report("Proper Disposal of Hazardous Waste", "Description"));
        reportList.add(new Report("Begin with the basics: Waste Segregation", "Description"));
        reportList.add(new Report("Reduce Singe-Use Plastic", "Description"));
        reportList.add(new Report("Proper Disposal of Hazardous Waste", "Description"));
        reportList.add(new Report("Begin with the basics: Waste Segregation", "Description"));
        reportList.add(new Report("Reduce Singe-Use Plastic", "Description"));
        reportList.add(new Report("Proper Disposal of Hazardous Waste", "Description"));

        // Add more vehicles as needed
        return reportList;
    }
}