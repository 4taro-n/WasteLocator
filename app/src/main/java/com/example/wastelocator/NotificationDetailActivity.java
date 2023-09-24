package com.example.wastelocator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
public class NotificationDetailActivity extends AppCompatActivity {

    private TextView notifiDetailTitle, notifiDetailDescription;
    private String reportTitle, reportDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        setViewIds();

        reportTitle = getIntent().getStringExtra("reportTitle");
        reportDescription = getIntent().getStringExtra("reportDescription");

        //set dummy data
        notifiDetailTitle.setText(reportTitle);
        notifiDetailDescription.setText(reportDescription);
    }

    private void setViewIds() {
        notifiDetailTitle = findViewById(R.id.notifiDetailTitle);
        notifiDetailDescription = findViewById(R.id.notifiDetailDescription);
    }
}